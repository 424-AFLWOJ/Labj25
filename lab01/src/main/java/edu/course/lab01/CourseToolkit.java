package edu.course.lab01;

/**
 * Набор небольших статических методов для лабораторной работы № 1.
 */
public final class CourseToolkit {

    private CourseToolkit() {
        // Утилитарный класс не должен иметь экземпляров.
    }

    /**
     * Проверяет, является ли число чётным.
     */
    public static boolean isEven(int number) {
        return number % 2 == 0;
    }

    /**
     * Проверяет, является ли число простым.
     *
     * @param number проверяемое число
     * @return true, если число простое
     */
    public static boolean isPrime(int number) {
        if (number < 2) {
            return false;
        }

        // Приведение к long защищает квадрат делителя от переполнения int.
        for (int divisor = 2; (long) divisor * divisor <= number; divisor++) {
            if (number % divisor == 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Проверяет, читается ли строка одинаково слева направо и справа налево.
     * Регистр и пробелы учитываются.
     *
     * @throws IllegalArgumentException если text равен null
     */
    public static boolean isPalindrome(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text must not be null");
        }

        int left = 0;
        int right = text.length() - 1;

        while (left < right) {
            if (text.charAt(left) != text.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }

        return true;
    }

    /**
     * Вычисляет среднее арифметическое элементов массива.
     * Исходный массив не изменяется.
     *
     * @throws IllegalArgumentException если массив равен null или пуст
     */
    public static double average(int[] values) {
        validateArray(values);

        long sum = 0;
        for (int value : values) {
            sum += value;
        }

        return (double) sum / values.length;
    }

    /**
     * Находит минимальный элемент массива (дополнительная часть).
     */
    public static int min(int[] values) {
        validateArray(values);

        int result = values[0];
        for (int value : values) {
            if (value < result) {
                result = value;
            }
        }
        return result;
    }

    /**
     * Находит максимальный элемент массива (дополнительная часть).
     */
    public static int max(int[] values) {
        validateArray(values);

        int result = values[0];
        for (int value : values) {
            if (value > result) {
                result = value;
            }
        }
        return result;
    }

    private static void validateArray(int[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("values must not be null or empty");
        }
    }
}
