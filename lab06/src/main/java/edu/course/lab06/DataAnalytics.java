package edu.course.lab06;


import java.util.*;

public final class DataAnalytics {
    public Map<TaskStatus, Integer> countByStatus(List<ProjectTask> tasks) {
        Map<TaskStatus, Integer> counts = new EnumMap<>(TaskStatus.class);
        for (ProjectTask task : tasks) {
            TaskStatus status = task.getStatus();
            counts.put(status, counts.getOrDefault(status, 0) + 1);
        }
        return counts;
    }
    public Set<String> uniqueCategories(List<ProjectTask> tasks) {
        Set<String> categories = new LinkedHashSet<>();
        for (ProjectTask task : tasks) { categories.add(task.getCategory()); }
        return categories;
    }
    public List<ProjectTask> sortByHoursAndId(List<ProjectTask> tasks) {
        List<ProjectTask> sorted = new ArrayList<>(tasks);
        sorted.sort(new Comparator<ProjectTask>() {
            @Override
            public int compare(ProjectTask a, ProjectTask b) {
                int result = Integer.compare(a.getEstimatedHours(), b.getEstimatedHours());
                if (result != 0) { return result; }
                return a.getId().compareTo(b.getId());
            }
        });
        return sorted;
    }
}
