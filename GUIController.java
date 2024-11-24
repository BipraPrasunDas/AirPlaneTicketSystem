package airline;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class GUIController {
    private FlightManager flightManager;
    private BookingManager bookingManager;

    public GUIController(FlightManager flightManager, BookingManager bookingManager) {
        this.flightManager = flightManager;
        this.bookingManager = bookingManager;
    }

    public void displaySearchFlights() {
        JFrame frame = new JFrame("Search Flights");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 2));

        JTextField originField = new JTextField();
        JTextField destinationField = new JTextField();
        JTextField dateField = new JTextField("yyyy-mm-dd");

        JButton searchButton = new JButton("Search");
        JTextArea resultArea = new JTextArea();

        searchButton.addActionListener(e -> {
            String origin = originField.getText();
            String destination = destinationField.getText();
            LocalDate date = LocalDate.parse(dateField.getText());

            List<Flight> flights = flightManager.searchFlights(origin, destination, date);
            resultArea.setText("");
            for (Flight flight : flights) {
                resultArea.append(flight.toString() + "\n");
            }
        });

        panel.add(new JLabel("Origin:"));
        panel.add(originField);
        panel.add(new JLabel("Destination:"));
        panel.add(destinationField);
        panel.add(new JLabel("Date:"));
        panel.add(dateField);
        panel.add(searchButton);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        frame.setVisible(true);
    }
}

