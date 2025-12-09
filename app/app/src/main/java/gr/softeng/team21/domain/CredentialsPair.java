package gr.softeng.team21.domain;


public class CredentialsPair {
    private final String username;
    private final String password;

    public CredentialsPair(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username and password cannot be null");
        }
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof CredentialsPair)) return false;

        CredentialsPair that = (CredentialsPair) other;
        return username.equals(that.username) && password.equals(that.password);
    }

}
