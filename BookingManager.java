package airline;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class BookingManager {
    private List<Booking> bookings; // Stores all bookings
    private FileHandler fileHandler; // Handles file operations for bookings

    // Constructor
    public BookingManager(List<Booking> bookings, FileHandler fileHandler) {
        this.bookings = bookings;
        this.fileHandler = fileHandler;
    }

    /**
     * Create a new booking for a flight.
     */
    public Booking createBooking(Flight flight, String customerName, int seatNumber) {
        if (!flight.isSeatAvailable(seatNumber)) {
            throw new IllegalArgumentException("Seat is not available for booking.");
        }

        // Generate a unique booking ID
        String bookingID = UUID.randomUUID().toString();
        // Calculate the final price
        double finalPrice = flight.calculatePrice(LocalDate.now(), 1);

        // Create the booking object
        Booking booking = new Booking(bookingID, flight, customerName, LocalDate.now(), seatNumber, finalPrice);

        // Mark the seat as booked in the flight
        flight.bookSeat(seatNumber);

        // Add the booking to the list
        bookings.add(booking);

        // Update the bookings file
        updateBookingFile("bookings.txt");

        return booking;
    }

    /**
     * Retrieve a booking by its ID.
     */
    public Booking getBooking(String bookingID) {
        for (Booking booking : bookings) {
            if (booking.getBookingID().equals(bookingID)) {
                return booking;
            }
        }
        return null;
    }

    /**
     * Updates the bookings.txt file.
     */
    public void updateBookingFile(String filename) {
        fileHandler.writeBookings(bookings, filename);
    }

    /**
     * Retrieve booking details if the password matches.
     */
    public String getBookingDetails(String bookingID, String password) {
        Booking booking = getBooking(bookingID);
        if (booking != null && booking.getPassword().equals(password)) {
            return String.format("Booking ID: %s\nCustomer Name: %s\nFlight: %s\nSeat: %d\nPrice: %.2f",
                    booking.getBookingID(),
                    booking.getCustomerName(),
                    booking.getFlight().getFlightID(),
                    booking.getSeatNumber(),
                    booking.getFinalPrice());
        } else {
            return "Invalid booking ID or password.";
        }
    }

    /**
     * Check if a seat is available for a specific flight.
     */
    public boolean isSeatAvailable(Flight flight, int seatNumber) {
        return flight.isSeatAvailable(seatNumber);
    }

    /**
     * Reload bookings from file (for system reinitialization).
     */
    public void reloadBookings(String filename) {
        bookings = fileHandler.readBookings(filename);
    }

    /**
     * List all bookings (for administrative purposes).
     */
    public List<Booking> getAllBookings() {
        return bookings;
    }
}
