package edu.course.lab02;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTaskTest {

    @Test
    void createsValidTask() {
        ProjectTask task = new ProjectTask("T-1", "Implement login", TaskStatus.TODO, 5);

        assertEquals("T-1", task.getId());
        assertEquals("Implement login", task.getTitle());
        assertEquals(TaskStatus.TODO, task.getStatus());
        assertEquals(5, task.getEstimatedHours());
        assertFalse(task.isCompleted());
    }

    @Test
    void rejectsNullOrBlankId() {
        assertThrows(IllegalArgumentException.class,
                () -> new ProjectTask(null, "Title", TaskStatus.TODO, 1));
        assertThrows(IllegalArgumentException.class,
                () -> new ProjectTask("  ", "Title", TaskStatus.TODO, 1));
    }

    @Test
    void rejectsNullOrBlankTitle() {
        assertThrows(IllegalArgumentException.class,
                () -> new ProjectTask("T-1", null, TaskStatus.TODO, 1));
        assertThrows(IllegalArgumentException.class,
                () -> new ProjectTask("T-1", "  ", TaskStatus.TODO, 1));
    }

    @Test
    void rejectsNullStatusAndNonPositiveEstimate() {
        assertThrows(IllegalArgumentException.class,
                () -> new ProjectTask("T-1", "Title", null, 1));
        assertThrows(IllegalArgumentException.class,
                () -> new ProjectTask("T-1", "Title", TaskStatus.TODO, 0));
        assertThrows(IllegalArgumentException.class,
                () -> new ProjectTask("T-1", "Title", TaskStatus.TODO, -1));
    }

    @Test
    void changesStatusAndDetectsCompletion() {
        ProjectTask task = new ProjectTask("T-1", "Title", TaskStatus.TODO, 2);

        task.changeStatus(TaskStatus.IN_PROGRESS);
        assertFalse(task.isCompleted());

        task.changeStatus(TaskStatus.DONE);
        assertTrue(task.isCompleted());
    }

    @Test
    void rejectsNullStatusChangeWithoutChangingState() {
        ProjectTask task = new ProjectTask("T-1", "Title", TaskStatus.TODO, 2);

        assertThrows(IllegalArgumentException.class, () -> task.changeStatus(null));

        assertEquals(TaskStatus.TODO, task.getStatus());
    }

    @Test
    void increasesEstimate() {
        ProjectTask task = new ProjectTask("T-1", "Title", TaskStatus.TODO, 2);

        task.increaseEstimatedHours(3);

        assertEquals(5, task.getEstimatedHours());
    }

    @Test
    void rejectsInvalidIncreaseWithoutChangingEstimate() {
        ProjectTask task = new ProjectTask("T-1", "Title", TaskStatus.TODO, 2);

        assertThrows(IllegalArgumentException.class, () -> task.increaseEstimatedHours(0));
        assertThrows(IllegalArgumentException.class, () -> task.increaseEstimatedHours(-1));

        assertEquals(2, task.getEstimatedHours());
    }

    @Test
    void rejectsEstimateOverflowWithoutChangingValue() {
        ProjectTask task = new ProjectTask(
                "T-1", "Title", TaskStatus.TODO, Integer.MAX_VALUE);

        assertThrows(IllegalArgumentException.class, () -> task.increaseEstimatedHours(1));

        assertEquals(Integer.MAX_VALUE, task.getEstimatedHours());
    }
}
