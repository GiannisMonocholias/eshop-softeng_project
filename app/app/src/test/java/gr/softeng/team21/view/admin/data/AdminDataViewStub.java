package gr.softeng.team21.view.admin.data;

/**
 * A stub implementation of the {@link AdminDataView} interface for unit testing.
 * Captures user input simulations and ensures correct dialog triggers.
 * @author Αλέξανδρος Δρακάκης
 */
public class AdminDataViewStub implements AdminDataView {

    private String username = "";
    private String password = "";
    private String email = "";
    private String firstName = "";
    private String lastName = "";
    private String phone = "";
    private String street = "";
    private String streetNo = "";
    private String city = "";
    private String zip = "";

    private String errorMessage = "";
    private String successMessage = "";
    private boolean unsavedDialogShown = false;
    private boolean finished = false;

    // --- Setters for user input simulation (χρησιμοποιούνται από το Test) ---
    public void setUsername(String v) { this.username = v; }
    public void setPassword(String v) { this.password = v; }
    public void setEmail(String v) { this.email = v; }
    public void setFirstName(String v) { this.firstName = v; }
    public void setLastName(String v) { this.lastName = v; }
    public void setPhone(String v) { this.phone = v; }
    public void setStreet(String v) { this.street = v; }
    public void setStreetNo(String v) { this.streetNo = v; }
    public void setCity(String v) { this.city = v; }
    public void setZip(String v) { this.zip = v; }

    // --- Override μεθόδων του View (λαμβάνουν τα simulated inputs) ---
    @Override public String getUsername() { return username; }
    @Override public String getPassword() { return password; }
    @Override public String getEmail() { return email; }
    @Override public String getFirstName() { return firstName; }
    @Override public String getLastName() { return lastName; }
    @Override public String getPhone() { return phone; }
    @Override public String getStreet() { return street; }
    @Override public String getStreetNo() { return streetNo; }
    @Override public String getCity() { return city; }
    @Override public String getZip() { return zip; }

    /**
     * Αποθηκεύει τοπικά τα δεδομένα που φορτώνει ο Presenter στην αρχή,
     * ώστε να ελεγχθούν αργότερα στα Assertions του Test.
     */
    @Override
    public void setAdminData(String username, String password, String email, String firstName, String lastName, String phone,
                             String street, String streetNo, String city, String zip) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.street = street;
        this.streetNo = streetNo;
        this.city = city;
        this.zip = zip;
    }

    @Override
    public void showError(String message) {
        this.errorMessage = message;
    }

    @Override
    public void showSuccessMessage(String message) {
        this.successMessage = message;
    }

    @Override
    public void showUnsavedChangesDialog() {
        this.unsavedDialogShown = true;
    }

    @Override
    public void finishActivity() {
        this.finished = true;
    }

    // --- Getters for verification (χρησιμοποιούνται από τα Assertions του Test) ---

    public String getErrorMessage() { return errorMessage; }
    public String getSuccessMessage() { return successMessage; }
    public boolean isUnsavedDialogShown() { return unsavedDialogShown; }
    public boolean isFinished() { return finished; }
}