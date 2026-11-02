package edu.course.lab04;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {
    @Test
    void booksWithSameIsbnAreEqualEvenWhenTitlesDiffer() {
        Book first = new Book("978-5-00000-001-1", "Java: начало");
        Book second = new Book("978-5-00000-001-1", "Другое название");

        assertNotSame(first, second);
        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void bookCanBeFoundInHashMapByAnotherInstanceWithSameIsbn() {
        HashMap<Book, String> catalogue = new HashMap<>();
        catalogue.put(new Book("978-5-00000-002-8", "Java для начинающих"), "полка A-3");

        assertEquals("полка A-3",
                catalogue.get(new Book("978-5-00000-002-8", "Java и ООП")));
    }

    @Test
    void booksWithDifferentIsbnAreNotEqual() {
        Book first = new Book("978-5-00000-003-5", "Первая книга");
        Book second = new Book("978-5-00000-004-2", "Первая книга");

        assertNotEquals(first, second);
    }
}
