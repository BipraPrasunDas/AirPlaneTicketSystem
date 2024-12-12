package airline;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


public class DiscountCalculator {
    public double calculateEarlyBirdDiscount(LocalDate bookingDate, LocalDate departureDate) {
        long daysBetween = ChronoUnit.DAYS.between(bookingDate, departureDate);
        if (daysBetween > 30) return 0.1; // 10% discount
        return 0.0;
    }


    public double calculateLateSurcharge(LocalDate bookingDate, LocalDate departureDate) {
        long daysBetween = ChronoUnit.DAYS.between(bookingDate, departureDate);
        if (daysBetween <= 7) return 0.2; // 20% surcharge
        return 0.0;
    }


    public double calculateBulkPurchaseDiscount(int seatCount) {
        if (seatCount >= 5) return 0.15; // 15% discount
        return 0.0;
    }


    public double applyDiscounts(double basePrice, LocalDate bookingDate, LocalDate departureDate, int seatCount) {
        double discount = calculateEarlyBirdDiscount(bookingDate, departureDate);
        double surcharge = calculateLateSurcharge(bookingDate, departureDate);
        double bulkDiscount = calculateBulkPurchaseDiscount(seatCount);
        return basePrice * (1 - discount + surcharge - bulkDiscount);
    }
}
