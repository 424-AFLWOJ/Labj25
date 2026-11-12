package edu.course.lab08;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

@Timeout(10)
class BatchProcessorTest {
    @TempDir Path directory;
    final BatchProcessor<Integer, Integer> processor = new BatchProcessor<>();

    @Test void preparation() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            List<Callable<Integer>> tasks = List.of(() -> 10, () -> 20, () -> 30);
            List<Future<Integer>> futures = new ArrayList<>();
            for (Callable<Integer> task : tasks) { futures.add(executor.submit(task)); }
            List<Integer> values = new ArrayList<>();
            for (Future<Integer> future : futures) { values.add(future.get()); }
            assertEquals(List.of(10, 20, 30), values);
        } finally { executor.shutdown(); }
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        assertTrue(executor.isTerminated());
    }
    @Test void emptyAndSuccessfulInputs() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            assertTrue(processor.processSequentially(List.of(), x -> x).isEmpty());
            assertTrue(processor.processConcurrently(List.of(), x -> x, executor).isEmpty());
            List<Integer> input = new ArrayList<>(List.of(3, 1, 2));
            List<Integer> copy = new ArrayList<>(input);
            checkValues(processor.processSequentially(input, x -> x * 2), List.of(6, 2, 4));
            checkValues(processor.processConcurrently(input, x -> x * 2, executor), List.of(6, 2, 4));
            assertEquals(copy, input);
            assertFalse(executor.isShutdown());
            assertEquals(42, executor.submit(() -> 42).get());
        } finally { executor.shutdownNow(); }
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
    }
    void checkValues(List<TaskResult<Integer>> results, List<Integer> expected) {
        assertEquals(expected.size(), results.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(i, results.get(i).index());
            assertTrue(results.get(i).isSuccess());
            assertNull(results.get(i).error());
            assertEquals(expected.get(i), results.get(i).value());
        }
    }
    @Test void keepsOrderWhenLaterTaskCompletesFirst() throws Exception {
        CountDownLatch secondFinished = new CountDownLatch(1);
        // Release the first task only after the second Future is completed.
        ExecutorService executor = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>()) {
            @Override
            protected void afterExecute(Runnable task, Throwable error) {
                secondFinished.countDown();
            }
        };
        try {
            TaskFunction<Integer, Integer> function = x -> {
                if (x == 1) { secondFinished.await(); }
                return x * 10;
            };
            checkValues(processor.processConcurrently(List.of(1, 2), function, executor), List.of(10, 20));
        } finally { executor.shutdownNow(); }
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
    }
    @Test void retainsSuccessfulResultsAroundFailure() throws Exception {
        IOException error = new IOException("broken item");
        TaskFunction<Integer, Integer> function = x -> {
            if (x == 2) { throw error; }
            return x;
        };
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            for (List<TaskResult<Integer>> results : List.of(
                    processor.processSequentially(List.of(1, 2, 3), function),
                    processor.processConcurrently(List.of(1, 2, 3), function, executor))) {
                assertEquals(3, results.size());
                assertEquals(1, results.get(0).value());
                assertEquals(3, results.get(2).value());
                assertEquals(1, results.get(1).index());
                assertFalse(results.get(1).isSuccess());
                assertSame(error, results.get(1).error());
                assertNull(results.get(1).value());
            }
        } finally { executor.shutdownNow(); }
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
    }
    @Test void countsValidRecordsInIndependentFiles() throws Exception {
        Path good = directory.resolve("good.txt");
        Path mixed = directory.resolve("mixed.txt");
        Path badHeader = directory.resolve("header.txt");
        Files.write(good, List.of(TaskFileLoader.HEADER, "1;Релиз;qa;1;READY", "2;Test;qa;2;DONE"), StandardCharsets.UTF_8);
        Files.write(mixed, List.of(TaskFileLoader.HEADER, "bad", "3;Test;qa;1;READY"), StandardCharsets.UTF_8);
        Files.writeString(badHeader, "wrong", StandardCharsets.UTF_8);
        List<Path> paths = new ArrayList<>(List.of(good, directory.resolve("missing.txt"), mixed, badHeader));
        List<Path> copy = new ArrayList<>(paths);
        BatchProcessor<Path, Integer> files = new BatchProcessor<>();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            for (List<TaskResult<Integer>> result : List.of(
                    files.processSequentially(paths, new TaskFileCounter()),
                    files.processConcurrently(paths, new TaskFileCounter(), executor))) {
                assertEquals(2, result.get(0).value());
                assertInstanceOf(DataLoadingException.class, result.get(1).error());
                assertInstanceOf(IOException.class, result.get(1).error().getCause());
                assertEquals(1, result.get(2).value());
                assertInstanceOf(IllegalArgumentException.class, result.get(3).error());
            }
            assertEquals(copy, paths);
        } finally { executor.shutdownNow(); }
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
    }
    @Test void sequentialInterruptionRestoresFlagAndStops() {
        try {
            IllegalStateException error = assertThrows(IllegalStateException.class,
                    () -> processor.processSequentially(List.of(1, 2), x -> { throw new InterruptedException(); }));
            assertInstanceOf(InterruptedException.class, error.getCause());
            assertTrue(Thread.currentThread().isInterrupted());
        } finally { Thread.interrupted(); }
    }
    @Test void workerInterruptionIsPropagated() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            assertThrows(InterruptedException.class, () -> processor.processConcurrently(
                    List.of(1), x -> { throw new InterruptedException("stop"); }, executor));
            assertFalse(executor.isShutdown());
        } finally { executor.shutdownNow(); }
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
    }
    @Test void callerInterruptionCancelsWaitingTask() throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CountDownLatch started = new CountDownLatch(1);
        CountDownLatch blocked = new CountDownLatch(1);
        Thread caller = Thread.currentThread();
        try {
            assertThrows(InterruptedException.class, () -> processor.processConcurrently(List.of(1), x -> {
                started.countDown();
                caller.interrupt();
                blocked.await();
                return x;
            }, executor));
            assertEquals(0, started.getCount());
            assertFalse(executor.isShutdown());
        } finally {
            Thread.interrupted();
            executor.shutdownNow();
        }
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
    }
}
