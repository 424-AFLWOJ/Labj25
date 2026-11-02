package edu.course.lab04;

import java.util.Objects;

/** Значение, которое хранится в IssueRegistry. */
public final class ProjectTask {
    private final String title;
    private final String description;

    public ProjectTask(String title) {
        this(title, "");
    }

    public ProjectTask(String title, String description) {
        Objects.requireNonNull(title, "title must not be null");
        Objects.requireNonNull(description, "description must not be null");
        if (title.isBlank()) {
            throw new IllegalArgumentException("title must not be blank");
        }
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "ProjectTask{title='" + title + "', description='" + description + "'}";
    }
}
