package airline;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FlightManager {
    private List<Flight> flights;

    public FlightManager(List<Flight> flights) {
        this.flights = flights;
    }

    public List<Flight> searchFlights(String origin, String destination, LocalDate departureDate) {
        List<Flight> results = new ArrayList<>();
        for (Flight flight : flights) {
            if (flight.getOrigin().equalsIgnoreCase(origin) &&
                    flight.getDestination().equalsIgnoreCase(destination) &&
                    flight.getDepartureDate().equals(departureDate)) {
                results.add(flight);
            }
        }
        return results;
    }

    public Flight getFlightByID(String flightID) {
        for (Flight flight : flights) {
            if (flight.getFlightID().equals(flightID)) {
                return flight;
            }
        }
        return null;
    }
}

