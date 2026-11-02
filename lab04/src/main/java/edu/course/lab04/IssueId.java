package edu.course.lab04;

import java.util.Objects;

/**
 * Неизменяемый ключ задачи: код проекта и номер задачи.
 * Оба поля участвуют и в equals, и в hashCode.
 */
public final class IssueId {
    private final String projectCode;
    private final int number;

    public IssueId(String projectCode, int number) {
        Objects.requireNonNull(projectCode, "projectCode must not be null");
        if (projectCode.isBlank()) {
            throw new IllegalArgumentException("projectCode must not be blank");
        }
        if (number <= 0) {
            throw new IllegalArgumentException("number must be positive");
        }
        this.projectCode = projectCode;
        this.number = number;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof IssueId other)) {
            return false;
        }
        return number == other.number && projectCode.equals(other.projectCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectCode, number);
    }

    @Override
    public String toString() {
        return projectCode + "-" + number;
    }
}
