package edu.course.lab03;

public final class EffortPriorityPolicy implements PriorityPolicy {

    @Override
    public int calculatePriority(ProjectTask task) {
        if (task == null) {
            throw new IllegalArgumentException("task must not be null");
        }

        // Fewer estimated hours means a higher priority.
        return -task.getEstimatedHours();
    }
}
