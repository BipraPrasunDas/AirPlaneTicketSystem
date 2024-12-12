package airline;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
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

    public Flight(String flightID, String origin, String destination, LocalDate departureDate, LocalTime departureTime, int totalSeats, double basePrice) {
        this.flightID = flightID;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.totalSeats = totalSeats;
        this.basePrice = basePrice;
        this.availableSeats = new ArrayList();

        for(int i = 1; i <= totalSeats; ++i) {
            this.availableSeats.add(i);
        }

    }

    public String getFlightID() {
        return this.flightID;
    }

    public String getOrigin() {
        return this.origin;
    }

    public String getDestination() {
        return this.destination;
    }

    public LocalDate getDepartureDate() {
        return this.departureDate;
    }

    public LocalTime getDepartureTime() {
        return this.departureTime;
    }

    public double getBasePrice() {
        return this.basePrice;
    }

    public List<Integer> getAvailableSeats() {
        return this.availableSeats;
    }

    public int getTotalSeats() {
        return this.totalSeats;
    }

    public double calculatePrice(LocalDate bookingDate, List<Integer> seatNumbers) {
        double totalPrice = 0.0;

        double seatPrice;
        for(Iterator var5 = seatNumbers.iterator(); var5.hasNext(); totalPrice += seatPrice) {
            int seat = (Integer)var5.next();
            seatPrice = this.basePrice;
            if (seat >= 1 && seat <= 12) {
                seatPrice *= 1.2;
            }
        }

        DiscountCalculator discountCalculator = new DiscountCalculator();
        return discountCalculator.applyDiscounts(totalPrice, bookingDate, this.departureDate, seatNumbers.size());
    }

    public void bookSeat(int seatNumber) {
        if (this.availableSeats.contains(seatNumber)) {
            this.availableSeats.remove(seatNumber);
        } else {
            throw new IllegalArgumentException("Seat is already booked or invalid.");
        }
    }

    public boolean isSeatAvailable(int seatNumber) {
        return this.availableSeats.contains(seatNumber);
    }

    public String getSeatClass(int seatNumber) {
        return seatNumber >= 1 && seatNumber <= 12 ? "First Class" : "Economy Class";
    }

    public String toString() {
        return String.format("FlightID: %s | From: %s | To: %s | Date: %s | Time: %s | Price: %.2f", this.flightID, this.origin, this.destination, this.departureDate, this.departureTime, this.basePrice);
    }
}

