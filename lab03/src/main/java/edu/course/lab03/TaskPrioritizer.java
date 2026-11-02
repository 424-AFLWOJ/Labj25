package edu.course.lab03;

public final class TaskPrioritizer {
    private final PriorityPolicy priorityPolicy;

    public TaskPrioritizer(PriorityPolicy priorityPolicy) {
        if (priorityPolicy == null) {
            throw new IllegalArgumentException("priorityPolicy must not be null");
        }
        this.priorityPolicy = priorityPolicy;
    }

    public ProjectTask chooseHigherPriority(ProjectTask first, ProjectTask second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("tasks must not be null");
        }

        int firstPriority = priorityPolicy.calculatePriority(first);
        int secondPriority = priorityPolicy.calculatePriority(second);
        return firstPriority >= secondPriority ? first : second;
    }
}
