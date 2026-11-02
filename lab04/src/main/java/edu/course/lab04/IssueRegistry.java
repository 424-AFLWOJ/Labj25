package edu.course.lab04;

import java.util.HashMap;
import java.util.Objects;

/** Реестр задач проекта, построенный на композиции с HashMap. */
public final class IssueRegistry {
    private final HashMap<IssueId, ProjectTask> tasks = new HashMap<>();

    /** Добавляет задачу или заменяет значение для уже существующего логически равного ключа. */
    public ProjectTask addOrReplace(IssueId issueId, ProjectTask task) {
        Objects.requireNonNull(issueId, "issueId must not be null");
        Objects.requireNonNull(task, "task must not be null");
        return tasks.put(issueId, task);
    }

    /** Возвращает задачу или null, если ключ не найден. */
    public ProjectTask get(IssueId issueId) {
        Objects.requireNonNull(issueId, "issueId must not be null");
        return tasks.get(issueId);
    }

    /** Удаляет задачу и возвращает ее; если ключа не было, возвращает null. */
    public ProjectTask remove(IssueId issueId) {
        Objects.requireNonNull(issueId, "issueId must not be null");
        return tasks.remove(issueId);
    }

    public boolean contains(IssueId issueId) {
        Objects.requireNonNull(issueId, "issueId must not be null");
        return tasks.containsKey(issueId);
    }

    public int size() {
        return tasks.size();
    }
}
