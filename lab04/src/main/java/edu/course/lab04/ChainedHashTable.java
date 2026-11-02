package edu.course.lab04;

import java.util.Objects;

/**
 * Дополнительная часть: учебная хеш-таблица с цепочками для конкретных типов
 * IssueId и ProjectTask. Класс намеренно не является generic.
 */
public final class ChainedHashTable {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node[] buckets;
    private int size;

    public ChainedHashTable() {
        this(DEFAULT_CAPACITY);
    }

    public ChainedHashTable(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("initialCapacity must be positive");
        }
        buckets = new Node[initialCapacity];
    }

    /** Добавляет или заменяет значение. Возвращает предыдущее значение, если оно было. */
    public ProjectTask addOrReplace(IssueId issueId, ProjectTask task) {
        Objects.requireNonNull(issueId, "issueId must not be null");
        Objects.requireNonNull(task, "task must not be null");

        int index = indexFor(issueId, buckets.length);
        Node current = buckets[index];
        while (current != null) {
            if (current.issueId.equals(issueId)) {
                ProjectTask previous = current.task;
                current.task = task;
                return previous;
            }
            current = current.next;
        }

        buckets[index] = new Node(issueId, task, buckets[index]);
        size++;
        if ((double) size / buckets.length > LOAD_FACTOR) {
            resize();
        }
        return null;
    }

    public ProjectTask get(IssueId issueId) {
        Objects.requireNonNull(issueId, "issueId must not be null");
        Node current = buckets[indexFor(issueId, buckets.length)];
        while (current != null) {
            if (current.issueId.equals(issueId)) {
                return current.task;
            }
            current = current.next;
        }
        return null;
    }

    public boolean contains(IssueId issueId) {
        return get(issueId) != null;
    }

    public ProjectTask remove(IssueId issueId) {
        Objects.requireNonNull(issueId, "issueId must not be null");
        int index = indexFor(issueId, buckets.length);
        Node current = buckets[index];
        Node previous = null;

        while (current != null) {
            if (current.issueId.equals(issueId)) {
                if (previous == null) {
                    buckets[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
                return current.task;
            }
            previous = current;
            current = current.next;
        }
        return null;
    }

    public int size() {
        return size;
    }

    private static int indexFor(IssueId issueId, int capacity) {
        return Math.floorMod(issueId.hashCode(), capacity);
    }

    private void resize() {
        if (buckets.length > Integer.MAX_VALUE / 2) {
            return;
        }
        Node[] newBuckets = new Node[buckets.length * 2];
        for (Node bucket : buckets) {
            Node current = bucket;
            while (current != null) {
                Node next = current.next;
                int newIndex = indexFor(current.issueId, newBuckets.length);
                current.next = newBuckets[newIndex];
                newBuckets[newIndex] = current;
                current = next;
            }
        }
        buckets = newBuckets;
    }

    private static final class Node {
        private final IssueId issueId;
        private ProjectTask task;
        private Node next;

        private Node(IssueId issueId, ProjectTask task, Node next) {
            this.issueId = issueId;
            this.task = task;
            this.next = next;
        }
    }
}
