package edu.course.lab03;

public final class StatusPriorityPolicy implements PriorityPolicy {

    @Override
    public int calculatePriority(ProjectTask task) {
        if (task == null) {
            throw new IllegalArgumentException("task must not be null");
        }

        // This exercise uses the following priority order:
        // IN_PROGRESS > TODO > DONE.
        return switch (task.getStatus()) {
            case IN_PROGRESS -> 3;
            case TODO -> 2;
            case DONE -> 1;
        };
    }
}
