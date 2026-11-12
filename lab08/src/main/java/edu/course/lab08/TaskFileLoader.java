package edu.course.lab08;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class TaskFileLoader {
    public static final String HEADER = "id;title;category;estimatedHours;status";

    public LoadResult load(Path path) {
        List<String> lines;
        try {
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new DataLoadingException("Cannot read " + path, e);
        }
        List<ProjectTask> tasks = new ArrayList<>();
        List<ValidationError> errors = new ArrayList<>();
        if (lines.isEmpty() || !HEADER.equals(lines.get(0))) {
            errors.add(new ValidationError(1, "header", "Unexpected or missing header"));
            return new LoadResult(tasks, errors);
        }
        String[] fields = {"id", "title", "category", "estimatedHours", "status"};
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.isBlank()) {
                continue;
            }
            // -1 preserves an empty last field.
            String[] parts = line.split(";", -1);
            if (parts.length != 5) {
                errors.add(new ValidationError(i + 1, "row", "Expected 5 fields"));
                continue;
            }
            boolean empty = false;
            for (int j = 0; j < parts.length; j++) {
                parts[j] = parts[j].trim();
                if (parts[j].isEmpty()) {
                    errors.add(new ValidationError(i + 1, fields[j], "Field is empty"));
                    empty = true;
                }
            }
            if (empty) {
                continue;
            }
            int hours;
            try {
                hours = Integer.parseInt(parts[3]);
            } catch (NumberFormatException e) {
                errors.add(new ValidationError(i + 1, "estimatedHours", "Expected an integer"));
                continue;
            }
            if (hours <= 0) {
                errors.add(new ValidationError(i + 1, "estimatedHours", "Hours must be positive"));
                continue;
            }
            TaskStatus status;
            try {
                status = TaskStatus.valueOf(parts[4]);
            } catch (IllegalArgumentException e) {
                errors.add(new ValidationError(i + 1, "status", "Unknown status"));
                continue;
            }
            tasks.add(new ProjectTask(parts[0], parts[1], parts[2], hours, status));
        }
        return new LoadResult(tasks, errors);
    }
}
