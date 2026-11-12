package edu.course.lab08;


import java.nio.file.Path;

public final class TaskFileCounter implements TaskFunction<Path, Integer> {
    @Override
    public Integer apply(Path path) {
        LoadResult result = new TaskFileLoader().load(path);
        if (!result.getErrors().isEmpty() && result.getErrors().get(0).getField().equals("header")) {
            throw new IllegalArgumentException("Invalid header: " + path);
        }
        return result.getTasks().size();
    }
}
