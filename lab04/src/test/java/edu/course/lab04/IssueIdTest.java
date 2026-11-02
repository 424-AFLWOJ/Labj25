package edu.course.lab04;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IssueIdTest {
    @Test
    void equalIdsHaveSameHashCodeAndAreDifferentObjects() {
        IssueId first = new IssueId("APP", 15);
        IssueId second = new IssueId("APP", 15);

        assertNotSame(first, second);
        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void eachFieldParticipatesInEquality() {
        assertNotEquals(new IssueId("APP", 15), new IssueId("WEB", 15));
        assertNotEquals(new IssueId("APP", 15), new IssueId("APP", 16));
    }

    @Test
    void equalsReturnsFalseForNullAndAnotherType() {
        IssueId id = new IssueId("APP", 15);

        assertFalse(id.equals(null));
        assertFalse(id.equals("APP-15"));
    }

    @Test
    void rejectsInvalidValues() {
        assertThrows(NullPointerException.class, () -> new IssueId(null, 1));
        assertThrows(IllegalArgumentException.class, () -> new IssueId("  ", 1));
        assertThrows(IllegalArgumentException.class, () -> new IssueId("APP", 0));
    }
}
