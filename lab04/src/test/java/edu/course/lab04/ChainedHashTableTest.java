package edu.course.lab04;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChainedHashTableTest {
    @Test
    void storesValuesForCollidingKeys() {
        ChainedHashTable table = new ChainedHashTable();
        IssueId first = new IssueId("Aa", 7);
        IssueId second = new IssueId("BB", 7);

        assertEquals(first.hashCode(), second.hashCode());
        table.addOrReplace(first, new ProjectTask("Первая"));
        table.addOrReplace(second, new ProjectTask("Вторая"));

        assertEquals(2, table.size());
        assertEquals("Первая", table.get(first).getTitle());
        assertEquals("Вторая", table.get(second).getTitle());
    }

    @Test
    void replacesAnEqualKeyAndSupportsRemoval() {
        ChainedHashTable table = new ChainedHashTable(2);
        IssueId first = new IssueId("APP", 1);
        IssueId equal = new IssueId("APP", 1);

        assertNull(table.addOrReplace(first, new ProjectTask("Старая")));
        ProjectTask previous = table.addOrReplace(equal, new ProjectTask("Новая"));

        assertEquals("Старая", previous.getTitle());
        assertEquals(1, table.size());
        assertEquals("Новая", table.get(first).getTitle());
        assertEquals("Новая", table.remove(equal).getTitle());
        assertEquals(0, table.size());
        assertFalse(table.contains(first));
    }

    @Test
    void remainsUsableAfterResize() {
        ChainedHashTable table = new ChainedHashTable(2);

        for (int number = 1; number <= 50; number++) {
            table.addOrReplace(new IssueId("APP", number), new ProjectTask("Задача " + number));
        }

        assertEquals(50, table.size());
        for (int number = 1; number <= 50; number++) {
            assertEquals("Задача " + number,
                    table.get(new IssueId("APP", number)).getTitle());
        }
    }
}
