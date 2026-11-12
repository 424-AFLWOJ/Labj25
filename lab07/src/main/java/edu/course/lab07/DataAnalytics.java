package edu.course.lab07;


import java.util.*;
import java.util.stream.Collectors;

public final class DataAnalytics {
    public List<ProjectTask> filterByStatus(List<ProjectTask> tasks, TaskStatus status) {
        return tasks.stream().filter(task -> task.getStatus() == status).toList();
    }
    public List<String> normalizedCategories(List<ProjectTask> tasks) {
        return tasks.stream().map(ProjectTask::getCategory)
                .map(category -> category.trim().toLowerCase(Locale.ROOT)).sorted().toList();
    }
    public List<String> titlesByStatus(List<ProjectTask> tasks, TaskStatus status) {
        return tasks.stream().filter(task -> task.getStatus() == status)
                .map(ProjectTask::getTitle).sorted().toList();
    }
    public Map<String, Long> countByCategory(List<ProjectTask> tasks) {
        return tasks.stream().collect(Collectors.groupingBy(
                task -> task.getCategory().trim().toLowerCase(Locale.ROOT),
                TreeMap::new, Collectors.counting()));
    }
    public Optional<ProjectTask> maxByHours(List<ProjectTask> tasks) {
        return tasks.stream().max(Comparator.comparingInt(ProjectTask::getEstimatedHours)
                .thenComparing(ProjectTask::getId));
    }
    public List<ProjectTask> sortByHoursAndId(List<ProjectTask> tasks) {
        return tasks.stream().sorted(Comparator.comparingInt(ProjectTask::getEstimatedHours)
                .thenComparing(ProjectTask::getId)).toList();
    }
}
