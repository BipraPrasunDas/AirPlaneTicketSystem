package airline;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Flight {
    private String flightID;
    private String origin;
    private String destination;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private int totalSeats;
    private List<Integer> availableSeats;
    private double basePrice;

    // Constructor
    public Flight(String flightID, String origin, String destination, LocalDate departureDate,
                  LocalTime departureTime, int totalSeats, double basePrice) {
        this.flightID = flightID;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.totalSeats = totalSeats;
        this.basePrice = basePrice;
        this.availableSeats = new ArrayList<>();
        for (int i = 1; i <= totalSeats; i++) {
            availableSeats.add(i); // Initialize all seats as available
        }
    }

    // Getter and Setter Methods
    public String getFlightID() {
        return flightID;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public List<Integer> getAvailableSeats() {
        return availableSeats;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    /**
     * Calculate the final price based on the booking date and seat count.
     */
    public double calculatePrice(LocalDate bookingDate, int seatCount) {
        double price = basePrice * seatCount;
        DiscountCalculator discountCalculator = new DiscountCalculator();
        price = discountCalculator.applyDiscounts(price, bookingDate, departureDate, seatCount);
        return price;
    }

    /**
     * Book a specific seat by marking it as unavailable.
     */
    public void bookSeat(int seatNumber) {
        if (availableSeats.contains(seatNumber)) {
            availableSeats.remove(Integer.valueOf(seatNumber));
        } else {
            throw new IllegalArgumentException("Seat is already booked or invalid.");
        }
    }

    /**
     * Check if a specific seat is available.
     */
    public boolean isSeatAvailable(int seatNumber) {
        return availableSeats.contains(seatNumber);
    }

    @Override
    public String toString() {
        return String.format("FlightID: %s | From: %s | To: %s | Date: %s | Time: %s | Price: %.2f",
                flightID, origin, destination, departureDate, departureTime, basePrice);
    }
}
