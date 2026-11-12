package edu.course.lab05;


import java.util.Objects;

public final class ProjectTask {
    private final String id;
    private final String title;
    private final String category;
    private final int estimatedHours;
    private final TaskStatus status;

    public ProjectTask(String id, String title, String category, int estimatedHours, TaskStatus status) {
        if (id == null || id.isBlank() || title == null || title.isBlank()
                || category == null || category.isBlank()) {
            throw new IllegalArgumentException("Text fields must not be blank");
        }
        if (estimatedHours <= 0) {
            throw new IllegalArgumentException("Hours must be positive");
        }
        this.id = id.trim();
        this.title = title.trim();
        this.category = category.trim();
        this.estimatedHours = estimatedHours;
        this.status = Objects.requireNonNull(status, "status");
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public int getEstimatedHours() { return estimatedHours; }
    public TaskStatus getStatus() { return status; }
}
