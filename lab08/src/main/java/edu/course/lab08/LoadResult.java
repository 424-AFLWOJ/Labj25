package edu.course.lab08;


import java.util.List;
public final class LoadResult {
    private final List<ProjectTask> tasks;
    private final List<ValidationError> errors;
    public LoadResult(List<ProjectTask> tasks, List<ValidationError> errors) {
        this.tasks = List.copyOf(tasks);
        this.errors = List.copyOf(errors);
    }
    public List<ProjectTask> getTasks() { return tasks; }
    public List<ValidationError> getErrors() { return errors; }
}
