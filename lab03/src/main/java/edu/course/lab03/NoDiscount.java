package edu.course.lab03;

public final class NoDiscount implements DiscountPolicy {

    @Override
    public double apply(double price) {
        validatePrice(price);
        return price;
    }

    private static void validatePrice(double price) {
        if (!Double.isFinite(price) || price < 0) {
            throw new IllegalArgumentException("price must be finite and non-negative");
        }
    }
}
