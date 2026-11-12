package edu.course.lab08;

import java.util.Objects;

/** Результат одного элемента входного списка. */
public final class TaskResult<R> {
    private final int index;
    private final R value;
    private final Exception error;

    private TaskResult(int index, R value, Exception error) {
        if (index < 0) {
            throw new IllegalArgumentException("index must be non-negative");
        }
        this.index = index;
        this.value = value;
        this.error = error;
    }

    public static <R> TaskResult<R> success(int index, R value) {
        return new TaskResult<>(index, value, null);
    }

    public static <R> TaskResult<R> failure(int index, Exception error) {
        return new TaskResult<>(index, null, Objects.requireNonNull(error, "error"));
    }

    public int index() {
        return index;
    }

    public R value() {
        return value;
    }

    public Exception error() {
        return error;
    }

    public boolean isSuccess() {
        return error == null;
    }
}
