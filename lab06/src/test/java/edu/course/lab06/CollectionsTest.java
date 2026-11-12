package edu.course.lab06;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class CollectionsTest {
    @Test void boxesPreserveTypes() {
        Box<String> text = new Box<>("java");
        assertEquals("java", text.get());
        text.set("git");
        assertEquals("git", text.get());
        // text.set(10); // Does not compile: Box<String> only accepts strings.
        Box<Integer> number = new Box<>(10);
        assertEquals(10, number.get());
        number.set(20);
        assertEquals(20, number.get());
    }
    @Test void integerStackIsLifo() {
        Stack<Integer> stack = new Stack<>();
        assertTrue(stack.isEmpty());
        stack.push(10); stack.push(20);
        assertEquals(2, stack.size());
        assertEquals(20, stack.peek());
        assertEquals(2, stack.size());
        assertEquals(20, stack.pop());
        assertEquals(10, stack.pop());
        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }
    @Test void stringStackAndInvalidOperations() {
        Stack<String> stack = new Stack<>();
        assertThrows(NoSuchElementException.class, stack::pop);
        assertThrows(NoSuchElementException.class, stack::peek);
        assertThrows(NullPointerException.class, () -> stack.push(null));
        assertTrue(stack.isEmpty());
        stack.push("first"); stack.push("second");
        assertEquals("second", stack.pop());
        assertEquals("first", stack.peek());
    }
    @Test void analyticsPreservesInputAndSortsTies() {
        ProjectTask b = new ProjectTask("B", "Second", "backend", 2, TaskStatus.READY);
        ProjectTask a = new ProjectTask("A", "First", "qa", 2, TaskStatus.DONE);
        ProjectTask c = new ProjectTask("C", "Third", "backend", Integer.MAX_VALUE, TaskStatus.READY);
        List<ProjectTask> tasks = new ArrayList<>(List.of(c, b, a));
        List<ProjectTask> copy = new ArrayList<>(tasks);
        DataAnalytics analytics = new DataAnalytics();
        assertEquals(Map.of(TaskStatus.READY, 2, TaskStatus.DONE, 1), analytics.countByStatus(tasks));
        assertEquals(List.of("backend", "qa"), new ArrayList<>(analytics.uniqueCategories(tasks)));
        List<ProjectTask> sorted = analytics.sortByHoursAndId(tasks);
        assertEquals(List.of(a, b, c), sorted);
        assertNotSame(tasks, sorted);
        assertEquals(copy, tasks);
    }
    @Test void emptyAnalytics() {
        DataAnalytics analytics = new DataAnalytics();
        assertTrue(analytics.countByStatus(List.of()).isEmpty());
        assertTrue(analytics.uniqueCategories(List.of()).isEmpty());
        assertTrue(analytics.sortByHoursAndId(List.of()).isEmpty());
    }
}
