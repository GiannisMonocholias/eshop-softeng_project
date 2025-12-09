package gr.softeng.team21.domain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;

import static org.junit.Assert.*;

public class CustomerRepositoryTest {

    private CustomerRepository customerRepository;
    private Customer customer;
    private EmailAddress email;

    @Before
    public void setUp ( ) throws Exception {
        customerRepository = CustomerRepository.getInstance ( );
        customerRepository.getCustomers ( ).clear ( );
        email = TestHelper.getEmail ( );
        customer = new Customer (
                "user1", "John", "pass1", "Doe",
                "1234567890", email, "CUST-001", new Date ( ) );
    }

    @After
    public void tearDown ( ) throws Exception {
        customerRepository.getCustomers ( ).clear ( );
    }

    @Test
    public void getCustomers ( ) {
        HashMap<String, Customer> customers = customerRepository.getCustomers ( );
        assertEquals ( 1, customers.size ( ) );
        assertTrue ( customers.containsKey ( "CUST-001" ) );
    }

    @Test
    public void addCustomer ( ) {
        EmailAddress email2 = new EmailAddress ( "test2@mail.com" );
        Customer customer2 = new Customer (
                "user2", "Jane", "pass2", "Doe",
                "0987654321", email2, "CUST-002", new Date ( ) );

        assertEquals ( 2, customerRepository.getCustomers ( ).size ( ) );

    }

    @Test(expected = IllegalArgumentException.class)
    public void addCustomerNull ( ) {
        customerRepository.addCustomer ( null );
    }

    @Test(expected = IllegalArgumentException.class)
    public void addCustomerDuplicateId ( ) {
        customerRepository.addCustomer ( customer );
    }

    @Test
    public void removeCustomer ( ) {
        customerRepository.removeCustomer ( customer );
        assertEquals ( 0, customerRepository.getCustomers ( ).size ( ) );

    }

    @Test(expected = IllegalArgumentException.class)
    public void removeCustomerNull ( ) {
        customerRepository.removeCustomer ( null );
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeCustomerNotExists ( ) {
        customerRepository.getCustomers ( ).clear ( );
        customerRepository.removeCustomer ( customer );
    }
    @AfterClass
    public static void tearDownAfterClass () {
        CustomerRepository.getInstance ( ).getCustomers ( ).clear ( );
    }
}

