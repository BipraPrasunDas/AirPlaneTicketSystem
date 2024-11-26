package airline;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class GUIController {
    private JFrame mainFrame;
    private FlightManager flightManager;
    private BookingManager bookingManager;

    public GUIController(FlightManager flightManager, BookingManager bookingManager) {
        this.flightManager = flightManager;
        this.bookingManager = bookingManager;
    }

    // Display the search flights interface
    public void displaySearchFlights() {
        // Frame to hold the search interface
        JFrame searchFrame = new JFrame("Search Flights");
        searchFrame.setSize(800, 600);

        // Panel to hold all components
        JPanel panel = new JPanel(new BorderLayout());

        // Panel for input fields (origin, destination, date)
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel originLabel = new JLabel("Origin:");
        JTextField originField = new JTextField();

        JLabel destinationLabel = new JLabel("Destination:");
        JTextField destinationField = new JTextField();

        JLabel dateLabel = new JLabel("Departure Date (YYYY-MM-DD):");
        JTextField dateField = new JTextField();

        JButton searchButton = new JButton("Search");

        // Add the components to input panel
        inputPanel.add(originLabel);
        inputPanel.add(originField);
        inputPanel.add(destinationLabel);
        inputPanel.add(destinationField);
        inputPanel.add(dateLabel);
        inputPanel.add(dateField);
        inputPanel.add(searchButton);

        // Table to display search results (flights)
        JTable flightsTable = new JTable(new DefaultTableModel(new String[]{"Flight ID", "Origin", "Destination", "Date", "Time", "Seats", "Price"}, 0));
        JScrollPane tableScrollPane = new JScrollPane(flightsTable);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        // Panel for action buttons
        JPanel actionPanel = new JPanel();
        JButton bookButton = new JButton("Book Selected Flight");
        actionPanel.add(bookButton);
        panel.add(actionPanel, BorderLayout.SOUTH);

        // Action for search button
        searchButton.addActionListener(e -> {
            String origin = originField.getText().trim();
            String destination = destinationField.getText().trim();
            LocalDate date = LocalDate.parse(dateField.getText().trim());

            // Search for flights using FlightManager
            List<Flight> matchedFlights = flightManager.searchFlights(origin, destination, date);

            // Update table with search results
            DefaultTableModel model = (DefaultTableModel) flightsTable.getModel();
            model.setRowCount(0); // Clear previous results
            for (Flight flight : matchedFlights) {
                model.addRow(new Object[]{flight.getFlightID(), flight.getOrigin(), flight.getDestination(),
                        flight.getDepartureDate(), flight.getDepartureTime(), flight.getTotalSeats(), flight.getBasePrice()});
            }
        });

        // Action for book button
        bookButton.addActionListener(e -> {
            int selectedRow = flightsTable.getSelectedRow();
            if (selectedRow >= 0) {
                String flightID = (String) flightsTable.getValueAt(selectedRow, 0);
                Flight selectedFlight = flightManager.getFlightByID(flightID);
                if (selectedFlight != null) {
                    showBookingForm(selectedFlight);
                }
            } else {
                JOptionPane.showMessageDialog(searchFrame, "Please select a flight to book.");
            }
        });

        // Display the search frame
        searchFrame.add(panel);
        searchFrame.setVisible(true);
    }

    // Display booking form when a flight is selected
    private void showBookingForm(Flight flight) {
        JFrame bookingFrame = new JFrame("Book Flight");
        bookingFrame.setSize(400, 300);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel("Customer Name:");
        JTextField nameField = new JTextField();

        JLabel seatLabel = new JLabel("Seat Number:");
        JTextField seatField = new JTextField();

        JButton bookButton = new JButton("Confirm Booking");

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(seatLabel);
        panel.add(seatField);
        panel.add(new JLabel());
        panel.add(bookButton);

        // Handle booking action
        bookButton.addActionListener(e -> {
            String customerName = nameField.getText().trim();
            int seatNumber = Integer.parseInt(seatField.getText().trim());

            try {
                Booking booking = bookingManager.createBooking(flight, customerName, seatNumber);
                JOptionPane.showMessageDialog(bookingFrame, "Booking Confirmed!\nBooking ID: " + booking.getBookingID() +
                        "\nPassword: " + booking.getPassword());
                bookingFrame.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(bookingFrame, "Error: " + ex.getMessage());
            }
        });

        bookingFrame.add(panel);
        bookingFrame.setVisible(true);
    }

    public static void main(String[] args) {
        // Initialize FileHandler to read flight and booking data
        FileHandler fileHandler = new FileHandler();

        // Load flights from file
        List<Flight> flights = fileHandler.readFlights("flights.txt");

        // Create FlightManager with loaded flights
        FlightManager flightManager = new FlightManager(flights);

        // Initialize BookingManager
        BookingManager bookingManager = new BookingManager(fileHandler.readBookings("bookings.txt"), fileHandler);

        // Create and show the GUI
        GUIController guiController = new GUIController(flightManager, bookingManager);
        guiController.displaySearchFlights();
    }
}

      

     
