package edu.course.lab07;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class DataAnalyticsTest {
    final DataAnalytics analytics = new DataAnalytics();
    @Test void preparation() {
        List<String> words = new ArrayList<>(List.of("java", "", "git", "maven", "api"));
        List<String> copy = new ArrayList<>(words);
        assertEquals(List.of("API", "GIT", "JAVA", "MAVEN"), new WordProcessor().process(words));
        assertEquals(copy, words);
    }
    @Test void emptyInput() {
        assertTrue(analytics.filterByStatus(List.of(), TaskStatus.READY).isEmpty());
        assertTrue(analytics.normalizedCategories(List.of()).isEmpty());
        assertTrue(analytics.countByCategory(List.of()).isEmpty());
        assertTrue(analytics.maxByHours(List.of()).isEmpty());
        assertTrue(analytics.sortByHoursAndId(List.of()).isEmpty());
        assertTrue(analytics.titlesByStatus(List.of(), TaskStatus.READY).isEmpty());
    }
    @Test void singleTask() {
        ProjectTask task = new ProjectTask("A", "Релиз", "Backend", 8, TaskStatus.READY);
        List<ProjectTask> tasks = List.of(task);
        assertEquals(List.of(task), analytics.filterByStatus(tasks, TaskStatus.READY));
        assertTrue(analytics.filterByStatus(tasks, TaskStatus.DONE).isEmpty());
        assertEquals(List.of("backend"), analytics.normalizedCategories(tasks));
        assertEquals(Map.of("backend", 1L), analytics.countByCategory(tasks));
        assertSame(task, analytics.maxByHours(tasks).orElseThrow());
        assertEquals(tasks, analytics.sortByHoursAndId(tasks));
        assertEquals(List.of("Релиз"), analytics.titlesByStatus(tasks, TaskStatus.READY));
    }
    @Test void groupsOrderTiesAndInputPreservation() {
        ProjectTask b = new ProjectTask("B", "Beta", " QA ", 2, TaskStatus.READY);
        ProjectTask a = new ProjectTask("A", "Alpha", "qa", 2, TaskStatus.DONE);
        ProjectTask c = new ProjectTask("C", "Gamma", "Backend", 9, TaskStatus.READY);
        List<ProjectTask> tasks = new ArrayList<>(List.of(c, b, a));
        List<ProjectTask> copy = new ArrayList<>(tasks);
        assertEquals(List.of(c, b), analytics.filterByStatus(tasks, TaskStatus.READY));
        assertEquals(List.of("backend", "qa", "qa"), analytics.normalizedCategories(tasks));
        assertEquals(Map.of("qa", 2L, "backend", 1L), analytics.countByCategory(tasks));
        assertEquals(List.of("backend", "qa"), new ArrayList<>(analytics.countByCategory(tasks).keySet()));
        assertSame(c, analytics.maxByHours(tasks).orElseThrow());
        assertSame(b, analytics.maxByHours(List.of(a, b)).orElseThrow());
        assertEquals(List.of(a, b, c), analytics.sortByHoursAndId(tasks));
        assertEquals(List.of("Beta", "Gamma"), analytics.titlesByStatus(tasks, TaskStatus.READY));
        assertEquals(copy, tasks);
        assertEquals("QA", b.getCategory());
    }
}
