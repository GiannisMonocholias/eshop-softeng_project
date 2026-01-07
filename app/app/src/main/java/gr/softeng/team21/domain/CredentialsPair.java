package gr.softeng.team21.domain;

/**
 * A value object representing a pair of user credentials (username and password).
 * This class is immutable, once created, its state cannot be modified.
 * @author Γιάννης Μονοχολιάς
 */
public class CredentialsPair {
    private final String username;
    private final String password;

    /**
     * Constructs a new CredentialsPair with the specified username and password.
     * @param username the user's identification name
     * @param password the user's security password
     * @throws IllegalArgumentException if either username or password is null
     */
    public CredentialsPair(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username and password cannot be null");
        }
        this.username = username;
        this.password = password;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Compares this credentials pair to the specified object.
     * The result is true if and only if the argument is not null and is a
     * CredentialsPair object that represents the same username and password.
     * @param other the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof CredentialsPair)) return false;

        CredentialsPair that = (CredentialsPair) other;
        return username.equals(that.username) && password.equals(that.password);
    }
}