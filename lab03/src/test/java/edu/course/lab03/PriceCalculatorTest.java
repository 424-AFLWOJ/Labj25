package edu.course.lab03;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PriceCalculatorTest {

    @Test
    void noDiscountReturnsOriginalPrice() {
        PriceCalculator calculator = new PriceCalculator(new NoDiscount());
        assertEquals(100.0, calculator.calculate(100.0), 0.0001);
    }

    @Test
    void noDiscountAcceptsZeroPrice() {
        PriceCalculator calculator = new PriceCalculator(new NoDiscount());
        assertEquals(0.0, calculator.calculate(0.0), 0.0001);
    }

    @Test
    void percentDiscountReducesPriceByTwentyPercent() {
        PriceCalculator calculator = new PriceCalculator(new PercentDiscount(20));
        assertEquals(80.0, calculator.calculate(100.0), 0.0001);
    }

    @Test
    void oneHundredPercentDiscountReturnsZero() {
        PriceCalculator calculator = new PriceCalculator(new PercentDiscount(100));
        assertEquals(0.0, calculator.calculate(75.0), 0.0001);
    }

    @Test
    void discountPolicyCanBeReplaced() {
        PriceCalculator withoutDiscount = new PriceCalculator(new NoDiscount());
        PriceCalculator withDiscount = new PriceCalculator(new PercentDiscount(20));

        assertEquals(100.0, withoutDiscount.calculate(100.0), 0.0001);
        assertEquals(80.0, withDiscount.calculate(100.0), 0.0001);
    }

    @Test
    void invalidPricesPercentagesAndDependenciesAreRejected() {
        PriceCalculator calculator = new PriceCalculator(new NoDiscount());
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(-1));
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> new PercentDiscount(-1));
        assertThrows(IllegalArgumentException.class, () -> new PercentDiscount(101));
        assertThrows(IllegalArgumentException.class, () -> new PercentDiscount(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> new PriceCalculator(null));
    }
}
