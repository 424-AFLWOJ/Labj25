package edu.course.lab06;


import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Objects;

public final class Stack<T> {
    private final ArrayList<T> values = new ArrayList<>();
    public void push(T value) { values.add(Objects.requireNonNull(value, "value")); }
    public T pop() {
        if (isEmpty()) { throw new NoSuchElementException("Stack is empty"); }
        return values.remove(values.size() - 1);
    }
    public T peek() {
        if (isEmpty()) { throw new NoSuchElementException("Stack is empty"); }
        return values.get(values.size() - 1);
    }
    public int size() { return values.size(); }
    public boolean isEmpty() { return values.isEmpty(); }
}
