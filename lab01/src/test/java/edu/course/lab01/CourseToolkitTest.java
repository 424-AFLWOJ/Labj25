package edu.course.lab01;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseToolkitTest {

    @Test
    void identifiesEvenAndOddNumbers() {
        assertTrue(CourseToolkit.isEven(8));
        assertFalse(CourseToolkit.isEven(7));
        assertTrue(CourseToolkit.isEven(-8));
        assertFalse(CourseToolkit.isEven(-7));
        assertTrue(CourseToolkit.isEven(0));
    }

    @Test
    void identifiesPrimeNumbers() {
        assertTrue(CourseToolkit.isPrime(2));
        assertTrue(CourseToolkit.isPrime(3));
        assertTrue(CourseToolkit.isPrime(47));
        assertTrue(CourseToolkit.isPrime(Integer.MAX_VALUE));
    }

    @Test
    void rejectsNumbersLessThanTwo() {
        assertFalse(CourseToolkit.isPrime(-5));
        assertFalse(CourseToolkit.isPrime(0));
        assertFalse(CourseToolkit.isPrime(1));
    }

    @Test
    void rejectsCompositeNumbersAndPrimeSquares() {
        assertFalse(CourseToolkit.isPrime(4));
        assertFalse(CourseToolkit.isPrime(9));
        assertFalse(CourseToolkit.isPrime(49));
        assertFalse(CourseToolkit.isPrime(100));
    }

    @Test
    void recognizesPalindromes() {
        assertTrue(CourseToolkit.isPalindrome("level"));
        assertTrue(CourseToolkit.isPalindrome("1221"));
        assertTrue(CourseToolkit.isPalindrome(""));
        assertTrue(CourseToolkit.isPalindrome("a"));
    }

    @Test
    void rejectsNonPalindromes() {
        assertFalse(CourseToolkit.isPalindrome("hello"));
    }

    @Test
    void palindromeComparisonIsCaseAndSpaceSensitive() {
        assertFalse(CourseToolkit.isPalindrome("Level"));
        assertFalse(CourseToolkit.isPalindrome("a b"));
    }

    @Test
    void rejectsNullPalindromeText() {
        assertThrows(IllegalArgumentException.class,
                () -> CourseToolkit.isPalindrome(null));
    }

    @Test
    void calculatesAverageWithFractionalResult() {
        assertEquals(2.0, CourseToolkit.average(new int[]{1, 2, 3}));
        assertEquals(2.5, CourseToolkit.average(new int[]{2, 3}));
    }

    @Test
    void calculatesAverageWithNegativeValues() {
        assertEquals(-2.0, CourseToolkit.average(new int[]{-1, -2, -3}));
        assertEquals(0.0, CourseToolkit.average(new int[]{-2, 2}));
    }

    @Test
    void averageDoesNotChangeInputArray() {
        int[] values = {1, 2, 3};
        int[] original = values.clone();

        CourseToolkit.average(values);

        assertArrayEquals(original, values);
    }

    @Test
    void rejectsNullOrEmptyArrayForAverage() {
        assertThrows(IllegalArgumentException.class,
                () -> CourseToolkit.average(null));
        assertThrows(IllegalArgumentException.class,
                () -> CourseToolkit.average(new int[]{}));
    }

    @Test
    void findsMinimumAndMaximum() {
        int[] values = {4, -2, 9, 1};

        assertEquals(-2, CourseToolkit.min(values));
        assertEquals(9, CourseToolkit.max(values));
    }

    @Test
    void findsMinimumAndMaximumInSingleElementArray() {
        int[] values = {5};

        assertEquals(5, CourseToolkit.min(values));
        assertEquals(5, CourseToolkit.max(values));
    }

    @Test
    void rejectsNullOrEmptyArrayForMinAndMax() {
        assertThrows(IllegalArgumentException.class, () -> CourseToolkit.min(null));
        assertThrows(IllegalArgumentException.class, () -> CourseToolkit.min(new int[]{}));
        assertThrows(IllegalArgumentException.class, () -> CourseToolkit.max(null));
        assertThrows(IllegalArgumentException.class, () -> CourseToolkit.max(new int[]{}));
    }
}
