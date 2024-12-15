package airline;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class GUIController {
    private FlightManager flightManager;
    private BookingManager bookingManager;

    public GUIController(FlightManager flightManager, BookingManager bookingManager) {
        this.flightManager = flightManager;
        this.bookingManager = bookingManager;
    }

    public void displayMainMenu() {
        JFrame mainMenu = this.createFrame("Airline Booking System", 300, 200);
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBackground(Color.DARK_GRAY);
        JButton searchFlightsButton = this.createButton("Search Flights");
        JButton loginButton = this.createButton("Login to View Booking");
        JButton exitButton = this.createButton("Exit");
        searchFlightsButton.addActionListener((e) -> {
            this.displaySearchFlights();
        });
        loginButton.addActionListener((e) -> {
            this.displayLoginWindow();
        });
        exitButton.addActionListener((e) -> {
            System.exit(0);
        });
        panel.add(searchFlightsButton);
        panel.add(loginButton);
        panel.add(exitButton);
        mainMenu.add(panel);
        mainMenu.setVisible(true);
    }

    void displaySearchFlights() {
        JFrame searchFrame = this.createFrame("Search Flights", 800, 600);
        this.addMenuBar(searchFrame);
        JPanel panel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBackground(Color.BLACK);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel originLabel = this.createLabel("Origin:");
        JTextField originField = new JTextField();
        JLabel destinationLabel = this.createLabel("Destination:");
        JTextField destinationField = new JTextField();
        JLabel dateLabel = this.createLabel("Departure Date (YYYY-MM-DD):");
        JTextField dateField = new JTextField();
        JButton searchButton = this.createButton("Search");
        inputPanel.add(originLabel);
        inputPanel.add(originField);
        inputPanel.add(destinationLabel);
        inputPanel.add(destinationField);
        inputPanel.add(dateLabel);
        inputPanel.add(dateField);
        inputPanel.add(searchButton);
        JTable flightsTable = new JTable(new DefaultTableModel(new String[]{"Flight ID", "Origin", "Destination", "Date", "Time", "Seats", "Base Price"}, 0));
        JScrollPane tableScrollPane = new JScrollPane(flightsTable);
        panel.add(inputPanel, "North");
        panel.add(tableScrollPane, "Center");
        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(Color.BLACK);
        JButton bookButton = this.createButton("Book Selected Flight");
        actionPanel.add(bookButton);
        panel.add(actionPanel, "South");
        searchButton.addActionListener((e) -> {
            try {
                String origin = originField.getText().trim();
                String destination = destinationField.getText().trim();
                LocalDate date = LocalDate.parse(dateField.getText().trim());
                List<Flight> matchedFlights = this.flightManager.searchFlights(origin, destination, date);
                DefaultTableModel model = (DefaultTableModel)flightsTable.getModel();
                model.setRowCount(0);
                Iterator var12 = matchedFlights.iterator();

                while(var12.hasNext()) {
                    Flight flight = (Flight)var12.next();
                    model.addRow(new Object[]{flight.getFlightID(), flight.getOrigin(), flight.getDestination(), flight.getDepartureDate(), flight.getDepartureTime(), flight.getTotalSeats(), flight.getBasePrice()});
                }
            } catch (Exception var14) {
                Exception exx = var14;
                JOptionPane.showMessageDialog(searchFrame, "Error: " + exx.getMessage());
            }

        });
        bookButton.addActionListener((e) -> {
            int selectedRow = flightsTable.getSelectedRow();
            if (selectedRow >= 0) {
                String flightID = (String)flightsTable.getValueAt(selectedRow, 0);
                Flight selectedFlight = this.flightManager.getFlightByID(flightID);
                if (selectedFlight != null) {
                    this.showBookingForm(selectedFlight);
                }
            } else {
                JOptionPane.showMessageDialog(searchFrame, "Please select a flight to book.");
            }

        });
        searchFrame.add(panel);
        searchFrame.setVisible(true);
    }

    private void showBookingForm(Flight flight) {
        JFrame bookingFrame = this.createFrame("Book Flight", 500, 400);
        this.addMenuBar(bookingFrame);
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel nameLabel = this.createLabel("Customer Name:");
        JTextField nameField = new JTextField();
        JLabel seatsLabel = this.createLabel("Select Seats:");
        DefaultListModel<String> seatListModel = new DefaultListModel();
        Iterator var8 = flight.getAvailableSeats().iterator();

        while(var8.hasNext()) {
            Integer seat = (Integer)var8.next();
            seatListModel.addElement("" + seat + " (" + flight.getSeatClass(seat) + ")");
        }

        JList<String> seatsList = new JList(seatListModel);
        seatsList.setSelectionMode(2);
        JScrollPane seatScrollPane = new JScrollPane(seatsList);
        JLabel priceLabel = this.createLabel("Final Price:");
        JLabel priceValue = new JLabel("Calculating...");
        priceValue.setForeground(Color.WHITE);
        JButton calculateButton = this.createButton("Calculate Price");
        JButton bookButton = this.createButton("Confirm Booking");
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(seatsLabel);
        panel.add(seatScrollPane);
        panel.add(priceLabel);
        panel.add(priceValue);
        panel.add(calculateButton);
        panel.add(bookButton);
        calculateButton.addActionListener((e) -> {
            try {
                List<Integer> selectedSeats = new ArrayList();
                Iterator var6 = seatsList.getSelectedValuesList().iterator();

                while(var6.hasNext()) {
                    String seatEntry = (String)var6.next();
                    int seatNumber = Integer.parseInt(seatEntry.split(" ")[0]);
                    selectedSeats.add(seatNumber);
                }

                double finalPrice = flight.calculatePrice(LocalDate.now(), selectedSeats);
                priceValue.setText(String.format("%.2f", finalPrice));
            } catch (Exception var9) {
                Exception exx = var9;
                JOptionPane.showMessageDialog(bookingFrame, "Invalid input: " + exx.getMessage());
            }

        });
        bookButton.addActionListener((e) -> {
            try {
                String customerName = nameField.getText().trim();
                List<Integer> selectedSeats = new ArrayList();
                Iterator var8 = seatsList.getSelectedValuesList().iterator();

                while(var8.hasNext()) {
                    String seatEntry = (String)var8.next();
                    int seatNumber = Integer.parseInt(seatEntry.split(" ")[0]);
                    selectedSeats.add(seatNumber);
                }

                if (selectedSeats.isEmpty()) {
                    JOptionPane.showMessageDialog(bookingFrame, "Please select at least one seat.");
                    return;
                }

                List<Booking> bookings = this.bookingManager.createBooking(flight, customerName, selectedSeats);
                StringBuilder successMessage = new StringBuilder("Booking successful!");
                Iterator var13 = bookings.iterator();

                while(var13.hasNext()) {
                    Booking booking = (Booking)var13.next();
                    successMessage.append("\nBooking ID: ").append(booking.getBookingID()).append("\nPassword: ").append(booking.getPassword());
                }

                JOptionPane.showMessageDialog(bookingFrame, successMessage.toString());
                bookingFrame.dispose();
            } catch (Exception var12) {
                Exception exx = var12;
                JOptionPane.showMessageDialog(bookingFrame, "Error: " + exx.getMessage());
            }

        });
        bookingFrame.add(panel);
        bookingFrame.setVisible(true);
    }

    private void displayLoginWindow() {
        JFrame loginFrame = this.createFrame("Login to View Booking", 400, 200);
        this.addMenuBar(loginFrame);
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel bookingIDLabel = this.createLabel("Booking ID:");
        JTextField bookingIDField = new JTextField();
        JLabel passwordLabel = this.createLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = this.createButton("Login");
        panel.add(bookingIDLabel);
        panel.add(bookingIDField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel());
        panel.add(loginButton);
        loginButton.addActionListener((e) -> {
            String bookingID = bookingIDField.getText().trim();
            String password = (new String(passwordField.getPassword())).trim();
            String details = this.bookingManager.getBookingDetails(bookingID, password);
            JOptionPane.showMessageDialog(loginFrame, details);
        });
        loginFrame.add(panel);
        loginFrame.setVisible(true);
    }

    private JFrame createFrame(String title, int width, int height) {
        JFrame frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(2);
        frame.getContentPane().setBackground(Color.BLACK);
        return frame;
    }

    private void addMenuBar(JFrame frame) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.BLACK);
        menuBar.setForeground(Color.WHITE);
        JMenu menu = new JMenu("Menu");
        menu.setForeground(Color.WHITE);
        JMenuItem searchFlightsItem = new JMenuItem("Search Flights");
        JMenuItem loginItem = new JMenuItem("Login to View Booking");
        searchFlightsItem.addActionListener((e) -> {
            this.displaySearchFlights();
        });
        loginItem.addActionListener((e) -> {
            this.displayLoginWindow();
        });
        menu.add(searchFlightsItem);
        menu.add(loginItem);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(60, 60, 60));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", 1, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(12));
        button.setOpaque(true);
        return button;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", 0, 14));
        return label;
    }
}
