package edu.course.lab08;

@FunctionalInterface
public interface TaskFunction<T, R> {
    R apply(T input) throws Exception;
}
