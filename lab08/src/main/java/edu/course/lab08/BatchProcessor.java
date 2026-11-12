package edu.course.lab08;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

public final class BatchProcessor<T, R> {
    public List<TaskResult<R>> processSequentially(List<T> inputs, TaskFunction<T, R> function) {
        Objects.requireNonNull(inputs, "inputs");
        Objects.requireNonNull(function, "function");
        List<TaskResult<R>> results = new ArrayList<>();
        for (int i = 0; i < inputs.size(); i++) {
            try {
                results.add(TaskResult.success(i, function.apply(inputs.get(i))));
            } catch (InterruptedException e) {
                // This method has no throws clause: restore the flag and stop.
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Processing interrupted", e);
            } catch (Exception e) {
                // The supplied function may throw any checked Exception.
                results.add(TaskResult.failure(i, e));
            }
        }
        return results;
    }

    public List<TaskResult<R>> processConcurrently(List<T> inputs, TaskFunction<T, R> function,
                                                  ExecutorService executor) throws InterruptedException {
        Objects.requireNonNull(inputs, "inputs");
        Objects.requireNonNull(function, "function");
        Objects.requireNonNull(executor, "executor");
        List<Future<R>> futures = new ArrayList<>();
        try {
            for (T input : inputs) {
                Callable<R> task = () -> function.apply(input);
                futures.add(executor.submit(task));
            }
            List<TaskResult<R>> results = new ArrayList<>();
            for (int i = 0; i < futures.size(); i++) {
                try {
                    results.add(TaskResult.success(i, futures.get(i).get()));
                } catch (ExecutionException e) {
                    Throwable cause = e.getCause();
                    if (cause instanceof InterruptedException) {
                        throw (InterruptedException) cause;
                    }
                    if (cause instanceof Error) { throw (Error) cause; }
                    results.add(TaskResult.failure(i, (Exception) cause));
                }
            }
            return results;
        } catch (InterruptedException | RuntimeException | Error e) {
            for (Future<R> future : futures) { future.cancel(true); }
            throw e;
        }
    }
}
