package gr.softeng.team21.memorydao;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;

import static org.junit.Assert.*;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.Date;
import gr.softeng.team21.domain.EmailAddress;
import gr.softeng.team21.domain.TestHelper;

public class CustomerDAOMemoryTest {

    private CustomerDAOMemory customerDAOMemory;
    private Customer customer;
    private EmailAddress email;

    @Before
    public void setUp ( ) throws Exception {
        customerDAOMemory = CustomerDAOMemory.getInstance ( );
        customerDAOMemory.getCustomers ( ).clear ( );
        email = TestHelper.getEmail ( );
        customer = new Customer (
                "user1", "John", "pass1", "Doe",
                "1234567890", email, "CUST-001", new Date( ) );
        customerDAOMemory.addCustomer(customer);
    }

    @After
    public void tearDown ( ) throws Exception {
        customerDAOMemory.getCustomers ( ).clear ( );
    }

    @Test
    public void getCustomers ( ) {
        HashMap<String, Customer> customers = customerDAOMemory.getCustomers ( );
        assertEquals ( 1, customers.size ( ) );
        assertTrue ( customers.containsKey ( "CUST-001" ) );
    }

    @Test
    public void addCustomer ( ) {
        EmailAddress email2 = new EmailAddress ( "test2@mail.com" );
        Customer customer2 = new Customer (
                "user2", "Jane", "pass2", "Doe",
                "0987654321", email2, "CUST-002", new Date ( ) );
        customerDAOMemory.addCustomer(customer2);

        assertEquals ( 2, customerDAOMemory.getCustomers ( ).size ( ) );

    }

    @Test(expected = IllegalArgumentException.class)
    public void addCustomerNull ( ) {
        customerDAOMemory.addCustomer ( null );
    }

    @Test(expected = IllegalArgumentException.class)
    public void addCustomerDuplicateId ( ) {
        customerDAOMemory.addCustomer ( customer );
    }

    @Test
    public void removeCustomer ( ) {
        customerDAOMemory.removeCustomer ( customer );
        assertEquals ( 0, customerDAOMemory.getCustomers ( ).size ( ) );

    }

    @Test(expected = IllegalArgumentException.class)
    public void removeCustomerNull ( ) {
        customerDAOMemory.removeCustomer ( null );
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeCustomerNotExists ( ) {
        customerDAOMemory.getCustomers ( ).clear ( );
        customerDAOMemory.removeCustomer ( customer );
    }
    @AfterClass
    public static void tearDownAfterClass () {
        CustomerDAOMemory.getInstance ( ).getCustomers ( ).clear ( );
    }
}

