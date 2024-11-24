package airline;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    public List<Flight> readFlights(String filename) {
        List<Flight> flights = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String flightID = parts[0];
                String origin = parts[1];
                String destination = parts[2];
                LocalDate departureDate = LocalDate.parse(parts[3]);
                LocalTime departureTime = LocalTime.parse(parts[4]);
                int totalSeats = Integer.parseInt(parts[5]);
                double basePrice = Double.parseDouble(parts[6]);
                flights.add(new Flight(flightID, origin, destination, departureDate, departureTime, totalSeats, basePrice));
            }
        } catch (IOException e) {
            System.out.println("Error reading flights file: " + e.getMessage());
        }
        return flights;
    }

    public void writeFlights(List<Flight> flights, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Flight flight : flights) {
                bw.write(flight.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing flights file: " + e.getMessage());
        }
    }

    public List<Booking> readBookings(String filename) {
        List<Booking> bookings = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String bookingID = parts[0];
                String customerName = parts[1];
                String flightID = parts[2];
                String origin = parts[3];
                String destination = parts[4];
                LocalDate departureDate = LocalDate.parse(parts[5]);
                LocalTime departureTime = LocalTime.parse(parts[6]);
                int totalSeats = Integer.parseInt(parts[7]);
                double basePrice = Double.parseDouble(parts[8]);
                int seatNumber = Integer.parseInt(parts[9]);
                double finalPrice = Double.parseDouble(parts[10]);
                String password = parts[11];
                Flight flight = new Flight(flightID, origin, destination, departureDate, departureTime, totalSeats, basePrice);
                Booking booking = new Booking(bookingID, flight, customerName, departureDate, seatNumber, finalPrice);
                bookings.add(booking);
            }
        } catch (IOException e) {
            System.out.println("Error reading bookings file: " + e.getMessage());
        }
        return bookings;
    }

    public void writeBookings(List<Booking> bookings, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Booking booking : bookings) {
                bw.write(booking.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing bookings file: " + e.getMessage());
        }
    }
}

