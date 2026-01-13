package gr.softeng.team21.view.admin.data;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.domain.Admin;

/**
 * Test for correct save of admin data.
 */

public class AdminDataPresenterTest {

    private AdminDataPresenter presenter;
    private Admin admin;

    /**
     * Initializes presenter and gets the current instance of admin
     * in order to have access to his latest info.
     */
    @Before
    public void setUp() {

        presenter = new AdminDataPresenter();
        admin = Admin.getInstance();

    }

    /**
     * Ensures that the new admin's info are correct saved.
     */
    @Test
    public void saveData() {

        String username = "admin21";
        String email = "admin@test.com";
        String firstName = "John";
        String lastName = "Doe";
        String phone = "6900000000";
        String address = "Athens";

        EmailAddress emailAddress = new EmailAddress(email);

        presenter.saveData(username, email, firstName, lastName, phone, address);

        assertEquals(username, admin.getUsername());
        assertEquals(emailAddress , admin.getEmailAddress());
        assertEquals(firstName, admin.getFirstname());
        assertEquals(lastName, admin.getLastname());
        assertEquals(phone, admin.getPhonenumber());
        assertNotNull(admin.getAddress());
    }
}