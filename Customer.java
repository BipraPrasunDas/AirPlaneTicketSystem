package airline;

public class Customer {
    private String name;
    private String bookingPassword;

    public Customer(String name) {
        this.name = name;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBookingPassword() {
        return bookingPassword;
    }

    public void setBookingPassword(String bookingPassword) {
        this.bookingPassword = bookingPassword;
    }
}

