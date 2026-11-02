package edu.course.lab03;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTaskTest {

    @Test
    void createsTaskWithValidData() {
        ProjectTask task = new ProjectTask("T-1", "Implement feature", TaskStatus.TODO, 4);

        assertEquals("T-1", task.getId());
        assertEquals("Implement feature", task.getTitle());
        assertEquals(TaskStatus.TODO, task.getStatus());
        assertEquals(4, task.getEstimatedHours());
    }

    @Test
    void rejectsInvalidTaskData() {
        assertThrows(IllegalArgumentException.class,
                () -> new ProjectTask(null, "Title", TaskStatus.TODO, 1));
        assertThrows(IllegalArgumentException.class,
                () -> new ProjectTask("T-1", "  ", TaskStatus.TODO, 1));
        assertThrows(IllegalArgumentException.class,
                () -> new ProjectTask("T-1", "Title", null, 1));
        assertThrows(IllegalArgumentException.class,
                () -> new ProjectTask("T-1", "Title", TaskStatus.TODO, 0));
    }

    @Test
    void taskStateIsImmutableAfterConstruction() {
        ProjectTask task = new ProjectTask("T-1", "Task", TaskStatus.IN_PROGRESS, 5);

        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
        assertEquals(5, task.getEstimatedHours());
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
        assertEquals(5, task.getEstimatedHours());
    }
}
