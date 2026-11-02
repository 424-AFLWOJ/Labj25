package edu.course.lab04;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class IssueRegistryTest {
    @Test
    void findsTaskByAnotherLogicallyEqualKey() {
        IssueRegistry registry = new IssueRegistry();
        registry.addOrReplace(new IssueId("APP", 15), new ProjectTask("Исправить вход"));

        ProjectTask found = registry.get(new IssueId("APP", 15));

        assertNotNull(found);
        assertEquals("Исправить вход", found.getTitle());
    }

    @Test
    void replacingValueDoesNotIncreaseRegistrySize() {
        IssueRegistry registry = new IssueRegistry();
        IssueId firstKey = new IssueId("APP", 15);
        IssueId equalKey = new IssueId("APP", 15);

        assertNull(registry.addOrReplace(firstKey, new ProjectTask("Старое название")));
        ProjectTask previous = registry.addOrReplace(equalKey, new ProjectTask("Новое название"));

        assertEquals("Старое название", previous.getTitle());
        assertEquals(1, registry.size());
        assertEquals("Новое название", registry.get(firstKey).getTitle());
    }

    @Test
    void differentKeysWithSameHashCodeKeepDifferentValues() {
        IssueId first = new IssueId("Aa", 7);
        IssueId second = new IssueId("BB", 7);
        IssueRegistry registry = new IssueRegistry();

        assertNotEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());

        registry.addOrReplace(first, new ProjectTask("Первая задача"));
        registry.addOrReplace(second, new ProjectTask("Вторая задача"));

        assertEquals(2, registry.size());
        assertEquals("Первая задача", registry.get(first).getTitle());
        assertEquals("Вторая задача", registry.get(second).getTitle());
    }

    @Test
    void supportsContainsRemoveAndSize() {
        IssueRegistry registry = new IssueRegistry();
        IssueId id = new IssueId("APP", 3);
        ProjectTask task = new ProjectTask("Обновить документацию");

        assertFalse(registry.contains(id));
        assertEquals(0, registry.size());

        registry.addOrReplace(id, task);
        assertTrue(registry.contains(new IssueId("APP", 3)));
        assertEquals(1, registry.size());
        assertSame(task, registry.remove(new IssueId("APP", 3)));
        assertFalse(registry.contains(id));
        assertEquals(0, registry.size());
        assertNull(registry.remove(id));
    }

    @Test
    void mutableKeyCanBecomeUnreachableAfterItsHashCodeChanges() {
        Map<MutableIssueId, String> map = new HashMap<>();
        MutableIssueId key = new MutableIssueId("one", 1);
        int originalHash = key.hashCode();
        map.put(key, "задача");

        key.setProjectCode("two");

        assertNotEquals(originalHash, key.hashCode());
        assertNull(map.get(key));
        assertFalse(map.containsKey(key));
        // Демонстрационный изменяемый ключ существует только в тесте.
        // В IssueRegistry используются неизменяемые IssueId.
    }

    private static final class MutableIssueId {
        private String projectCode;
        private final int number;

        private MutableIssueId(String projectCode, int number) {
            this.projectCode = projectCode;
            this.number = number;
        }

        private void setProjectCode(String projectCode) {
            this.projectCode = projectCode;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (!(object instanceof MutableIssueId other)) {
                return false;
            }
            return number == other.number && projectCode.equals(other.projectCode);
        }

        @Override
        public int hashCode() {
            return 31 * projectCode.hashCode() + number;
        }
    }
}
