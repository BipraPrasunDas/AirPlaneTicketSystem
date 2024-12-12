package airline;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingManager {
    private List<Booking> bookings;
    private FileHandler fileHandler;

    public BookingManager(List<Booking> bookings, FileHandler fileHandler) {
        this.bookings = bookings;
        this.fileHandler = fileHandler;
    }

    public List<Booking> createBooking(Flight flight, String customerName, List<Integer> seatNumbers) {
        List<Booking> newBookings = new ArrayList<>();

        // Check if all selected seats are available
        for (int seatNumber : seatNumbers) {
            if (!flight.isSeatAvailable(seatNumber)) {
                throw new IllegalArgumentException("Seat " + seatNumber + " is not available for booking.");
            }
        }

        // Create bookings for each seat
        for (int seatNumber : seatNumbers) {
            String bookingID = generateCompactBookingID(flight, customerName, seatNumber);

            // Use the full customer name without generating a short username
            String fullCustomerName = customerName;

            // Calculate the price for the selected seat (total price calculation adjusted)
            double finalPrice = flight.calculatePrice(LocalDate.now(), List.of(seatNumber));

            Booking booking = new Booking(bookingID, flight, fullCustomerName, LocalDate.now(), seatNumber, finalPrice);

            // Book the seat and add the booking to the list
            flight.bookSeat(seatNumber);
            bookings.add(booking);
            newBookings.add(booking);
        }

        // Update the booking file
        updateBookingFile("bookings.txt");
        return newBookings;
    }

    private String generateCompactBookingID(Flight flight, String customerName, int seatNumber) {
        String flightCode = flight.getFlightID().substring(0, 3).toUpperCase(); // First 3 characters of flight ID
        String customerNameInitials = customerName.split(" ")[0].toUpperCase(); // First name initial or full first name
        String dateCode = LocalDate.now().toString().replace("-", "").substring(2); // YYMMDD format
        return flightCode + customerNameInitials + dateCode + seatNumber;
    }

    public Booking getBooking(String bookingID) {
        for (Booking booking : bookings) {
            if (booking.getBookingID().equals(bookingID)) {
                return booking;
            }
        }
        return null;
    }

    public void updateBookingFile(String filename) {
        fileHandler.writeBookings(bookings, filename);
    }

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

    public void reloadBookings(String filename) {
        bookings = fileHandler.readBookings(filename);
    }

    public List<Booking> getAllBookings() {
        return bookings;
    }
}


    
