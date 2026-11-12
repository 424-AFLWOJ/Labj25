package edu.course.lab05;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class TaskFileLoaderTest {
    @TempDir Path directory;
    final TaskFileLoader loader = new TaskFileLoader();
    Path file(String... rows) throws IOException {
        Path path = directory.resolve("tasks.txt");
        Files.write(path, List.of(rows), StandardCharsets.UTF_8);
        return path;
    }
    @Test void readsAndConvertsNumbers() throws IOException {
        List<String> lines = Files.readAllLines(file("10", "bad", "20"), StandardCharsets.UTF_8);
        assertEquals(List.of("10", "bad", "20"), lines);
        List<Integer> numbers = new ArrayList<>();
        for (String line : lines) {
            try { numbers.add(Integer.parseInt(line)); }
            catch (NumberFormatException e) { /* Skip the invalid training row. */ }
        }
        assertEquals(List.of(10, 20), numbers);
    }
    @Test void badNumberThrows() {
        assertThrows(NumberFormatException.class, () -> Integer.parseInt("bad"));
    }
    @Test void readsCorrectUnicodeFile() throws IOException {
        LoadResult result = loader.load(file(TaskFileLoader.HEADER,
                "T-1;Подготовить релиз;сервер;8;READY", "T-2;Test;qa;2;DONE"));
        assertEquals(2, result.getTasks().size());
        assertEquals("Подготовить релиз", result.getTasks().get(0).getTitle());
        assertEquals("сервер", result.getTasks().get(0).getCategory());
        assertEquals(8, result.getTasks().get(0).getEstimatedHours());
        assertEquals(TaskStatus.READY, result.getTasks().get(0).getStatus());
        assertTrue(result.getErrors().isEmpty());
    }
    @Test void keepsGoodRowsAndReportsPhysicalLineNumbers() throws IOException {
        LoadResult result = loader.load(file(TaskFileLoader.HEADER,
                "T-1;Good;qa;2;READY", "", "T-2;;qa;2;READY",
                "T-3;Bad;qa;bad;READY", "T-4;Bad;qa;2;UNKNOWN",
                "too;few", "T-5;Good;qa;3;DONE", "T-6;Bad;qa;2;"));
        assertEquals(List.of("T-1", "T-5"), List.of(result.getTasks().get(0).getId(), result.getTasks().get(1).getId()));
        assertEquals(5, result.getErrors().size());
        int[] lines = {4, 5, 6, 7, 9};
        String[] fields = {"title", "estimatedHours", "status", "row", "status"};
        for (int i = 0; i < lines.length; i++) {
            assertEquals(lines[i], result.getErrors().get(i).getLineNumber());
            assertEquals(fields[i], result.getErrors().get(i).getField());
            assertFalse(result.getErrors().get(i).getMessage().isBlank());
        }
    }
    @Test void rejectsNonPositiveAndOverflowHours() throws IOException {
        LoadResult result = loader.load(file(TaskFileLoader.HEADER,
                "1;A;qa;0;READY", "2;B;qa;-1;READY", "3;C;qa;999999999999;READY"));
        assertTrue(result.getTasks().isEmpty());
        assertEquals(3, result.getErrors().size());
    }
    @Test void checksHeaderSeparately() throws IOException {
        LoadResult result = loader.load(file("wrong", "1;A;qa;1;READY"));
        assertTrue(result.getTasks().isEmpty());
        assertEquals("header", result.getErrors().get(0).getField());
        assertEquals(1, result.getErrors().get(0).getLineNumber());
        assertEquals("header", loader.load(file()).getErrors().get(0).getField());
        assertTrue(loader.load(file(TaskFileLoader.HEADER)).getTasks().isEmpty());
    }
    @Test void preservesIoCause() {
        DataLoadingException error = assertThrows(DataLoadingException.class,
                () -> loader.load(directory.resolve("missing.txt")));
        assertInstanceOf(IOException.class, error.getCause());
        assertTrue(error.getMessage().contains("missing.txt"));
    }
    @Test void rejectsEmptyIdCategoryAndExtraColumn() throws IOException {
        LoadResult result = loader.load(file(TaskFileLoader.HEADER,
                ";A;qa;1;READY", "1;A; ;1;READY", "2;A;qa;1;READY;extra"));
        assertTrue(result.getTasks().isEmpty());
        assertEquals("id", result.getErrors().get(0).getField());
        assertEquals("category", result.getErrors().get(1).getField());
        assertEquals("row", result.getErrors().get(2).getField());
    }
}
