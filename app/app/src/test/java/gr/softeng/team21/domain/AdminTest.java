package gr.softeng.team21.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.contact.EmailAddress;

/**
 * Unit tests for the {@link Admin} domain entity.
 * Verifies the correctness of the Singleton implementation and the basic getter/setter methods.
 * @author Αλέξανρδος Δρακάκης
 */
public class AdminTest {

    private Admin admin;

    /**
     * Initializes the testing environment before each test execution.
     * Retrieves the singleton Admin instance and assigns a baseline testing salary.
     */
    @Before
    public void setup() {
        admin = Admin.getInstance();
        admin.setSalary(1000);
    }

    /**
     * Verifies that the {@link Admin} class correctly enforces the Singleton design pattern.
     * Ensures that subsequent calls to retrieval methods return the exact same memory reference.
     */
    @Test
    public void getInstanceReturnsTheSameReference(){
        Admin admin1 = Admin.getInstance("Alex" , "Drak" , "123" , "alexdr" , "6969696969", new EmailAddress("alexd@gmail.vom"), 5000);
        Admin admin2 = Admin.getInstance();

        assertSame(admin, admin1);
        assertSame(admin, admin2);
    }

    /**
     * Verifies that the salary property can be correctly updated and retrieved
     * via its respective setter and getter methods.
     */
    @Test
    public void testSetAndGetSalary() {
        int expectedSalary = 5000;
        admin.setSalary(expectedSalary);
        assertEquals(expectedSalary, admin.getSalary());
    }

    /**
     * Verifies that the initial salary of the Admin instance exactly matches
     * the value assigned during the setup phase.
     */
    @Test
    public void testInitialSalary() {
        // Based on the setup() initialization
        assertEquals(1000, admin.getSalary());
    }
}