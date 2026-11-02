package edu.course.lab03;

public final class PriceCalculator {
    private final DiscountPolicy discountPolicy;

    public PriceCalculator(DiscountPolicy discountPolicy) {
        if (discountPolicy == null) {
            throw new IllegalArgumentException("discountPolicy must not be null");
        }
        this.discountPolicy = discountPolicy;
    }

    public double calculate(double price) {
        if (!Double.isFinite(price) || price < 0) {
            throw new IllegalArgumentException("price must be finite and non-negative");
        }
        return discountPolicy.apply(price);
    }
}
