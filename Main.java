package airline;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Initialize FileHandler
        FileHandler fileHandler = new FileHandler();

        // Load flights from flights.txt
        List<Flight> flights = fileHandler.readFlights("flights.txt");

        // Load bookings from bookings.txt
        List<Booking> bookings = fileHandler.readBookings("bookings.txt");

        // Initialize FlightManager and BookingManager
        FlightManager flightManager = new FlightManager(flights);
        BookingManager bookingManager = new BookingManager(bookings, fileHandler);

        // Launch the GUI
        GUIController guiController = new GUIController(flightManager, bookingManager);
        guiController.displaySearchFlights();
    }
}
