package edu.course.lab03;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskPrioritizerTest {

    private ProjectTask task(String id, TaskStatus status, int hours) {
        return new ProjectTask(id, "Task " + id, status, hours);
    }

    @Test
    void effortPolicyPrefersTaskWithFewerHours() {
        ProjectTask shortTask = task("1", TaskStatus.TODO, 2);
        ProjectTask longTask = task("2", TaskStatus.TODO, 8);
        TaskPrioritizer prioritizer = new TaskPrioritizer(new EffortPriorityPolicy());

        assertSame(shortTask, prioritizer.chooseHigherPriority(shortTask, longTask));
    }

    @Test
    void effortPolicyKeepsFirstTaskWhenHoursAreEqual() {
        ProjectTask first = task("1", TaskStatus.TODO, 4);
        ProjectTask second = task("2", TaskStatus.DONE, 4);
        TaskPrioritizer prioritizer = new TaskPrioritizer(new EffortPriorityPolicy());

        assertSame(first, prioritizer.chooseHigherPriority(first, second));
    }

    @Test
    void statusPolicyPrefersTaskInProgress() {
        ProjectTask todo = task("1", TaskStatus.TODO, 2);
        ProjectTask inProgress = task("2", TaskStatus.IN_PROGRESS, 10);
        TaskPrioritizer prioritizer = new TaskPrioritizer(new StatusPriorityPolicy());

        assertSame(inProgress, prioritizer.chooseHigherPriority(todo, inProgress));
    }

    @Test
    void statusPolicyPrefersTodoOverDone() {
        ProjectTask todo = task("1", TaskStatus.TODO, 2);
        ProjectTask done = task("2", TaskStatus.DONE, 10);
        TaskPrioritizer prioritizer = new TaskPrioritizer(new StatusPriorityPolicy());

        assertSame(todo, prioritizer.chooseHigherPriority(todo, done));
    }

    @Test
    void implementationCanBeReplacedWithoutChangingClient() {
        ProjectTask shortTodo = task("1", TaskStatus.TODO, 1);
        ProjectTask longInProgress = task("2", TaskStatus.IN_PROGRESS, 10);
        TaskPrioritizer byEffort = new TaskPrioritizer(new EffortPriorityPolicy());
        TaskPrioritizer byStatus = new TaskPrioritizer(new StatusPriorityPolicy());

        assertSame(shortTodo, byEffort.chooseHigherPriority(shortTodo, longInProgress));
        assertSame(longInProgress, byStatus.chooseHigherPriority(shortTodo, longInProgress));
    }

    @Test
    void selectedTaskInputsAreNotModified() {
        ProjectTask first = task("1", TaskStatus.TODO, 2);
        ProjectTask second = task("2", TaskStatus.IN_PROGRESS, 10);
        TaskPrioritizer prioritizer = new TaskPrioritizer(new StatusPriorityPolicy());

        prioritizer.chooseHigherPriority(first, second);

        assertEquals(TaskStatus.TODO, first.getStatus());
        assertEquals(2, first.getEstimatedHours());
        assertEquals(TaskStatus.IN_PROGRESS, second.getStatus());
        assertEquals(10, second.getEstimatedHours());
    }

    @Test
    void nullArgumentsAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> new TaskPrioritizer(null));

        TaskPrioritizer prioritizer = new TaskPrioritizer(new EffortPriorityPolicy());
        ProjectTask valid = task("1", TaskStatus.TODO, 1);
        assertThrows(IllegalArgumentException.class,
                () -> prioritizer.chooseHigherPriority(null, valid));
        assertThrows(IllegalArgumentException.class,
                () -> prioritizer.chooseHigherPriority(valid, null));
        assertThrows(IllegalArgumentException.class,
                () -> new EffortPriorityPolicy().calculatePriority(null));
        assertThrows(IllegalArgumentException.class,
                () -> new StatusPriorityPolicy().calculatePriority(null));
    }
}
