package edu.course.lab02;

/**
 * Доменная модель задачи проекта с защищённым состоянием.
 */
public final class ProjectTask {
    private final String id;
    private final String title;
    private TaskStatus status;
    private int estimatedHours;

    public ProjectTask(String id, String title, TaskStatus status, int estimatedHours) {
        this.id = requireText(id, "id");
        this.title = requireText(title, "title");

        if (status == null) {
            throw new IllegalArgumentException("status must not be null");
        }
        if (estimatedHours <= 0) {
            throw new IllegalArgumentException("estimatedHours must be positive");
        }

        this.status = status;
        this.estimatedHours = estimatedHours;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public int getEstimatedHours() {
        return estimatedHours;
    }

    public void changeStatus(TaskStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("newStatus must not be null");
        }
        status = newStatus;
    }

    public boolean isCompleted() {
        return status == TaskStatus.DONE;
    }

    public void increaseEstimatedHours(int additionalHours) {
        if (additionalHours <= 0) {
            throw new IllegalArgumentException("additionalHours must be positive");
        }
        if (estimatedHours > Integer.MAX_VALUE - additionalHours) {
            throw new IllegalArgumentException("estimatedHours overflow");
        }
        estimatedHours += additionalHours;
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be null or blank");
        }
        return value;
    }
}
