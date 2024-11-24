package airline;

import java.time.LocalDate;

public class Booking {
    private String bookingID;
    private Flight flight;
    private String customerName;
    private LocalDate bookingDate;
    private int seatNumber;
    private double finalPrice;
    private String password;

    public Booking(String bookingID, Flight flight, String customerName, LocalDate bookingDate,
                   int seatNumber, double finalPrice) {
        this.bookingID = bookingID;
        this.flight = flight;
        this.customerName = customerName;
        this.bookingDate = bookingDate;
        this.seatNumber = seatNumber;
        this.finalPrice = finalPrice;
        this.password = generatePassword();
    }

    private String generatePassword() {
        int randomCode = (int) (Math.random() * 10000); // Random 4-digit code
        return String.format("%04d", randomCode);
    }

    // Getters and Setters
    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
