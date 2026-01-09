package gr.softeng.team21.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.contact.Address;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.contact.EmailMessage;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.EmailDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.util.Date;

/**
 * Unit tests for the abstract {@link User} class (tested via its subclasses
 * {@link Employee} and {@link Customer}).
 * This suite verifies core user functionalities such as profile data management,
 * the dynamic {@code editData} system, and the internal messaging/email system.
 * @author Γιάννης Μονοχολιάς
 */
public class UserTest {

    private Employee sender;
    private Employee recipient;
    private Customer customer;
    private EmailDAOMemory senderProvider;
    private EmailDAOMemory recipientProvider;
    private EmailDAOMemory customerProvider;

    /**
     * Initializes the testing environment before each test.
     * Sets up sample Employees and a Customer, while instantiating memory-based
     * email providers to simulate communication.
     */
    @Before
    public void setUp() {
        EmailAddress senderAddress = new EmailAddress("sender@example.com");
        EmailAddress recipientAddress = new EmailAddress("recipient@example.com");
        EmailAddress customerEmailAddress = new EmailAddress("giannis@mail.com");

        sender = new Employee("GP","Giorgos","abcd123","Papadopoulos","3029761482",new EmailAddress("GP@gmail.com"),"E_1",100,1000,8,EmployeeState.ACTIVE, new Date(3,5,2025));
        recipient = new Employee("GPap","Giannis","abcd132","Papadopoulos","3029761543",new EmailAddress("GPap@gmail.com"),"E_2",150,1200,8,EmployeeState.ACTIVE, new Date(12,3,2022));
        customer = new Customer(
                "giannispap", "Giannis", "pass1234", "Papadopoulos",
                "697123456", customerEmailAddress, "CUST-001", new Date());


        senderProvider = new EmailDAOMemory();
        recipientProvider = new EmailDAOMemory();
        customerProvider = new EmailDAOMemory();

        sender.setEmailProvider(senderProvider);
        recipient.setEmailProvider(recipientProvider);
    }

    /** Tests the username property accessors. */
    @Test
    public void testUsernameGetterSetter() {
        customer.setUsername("newUser");
        assertEquals("newUser", customer.getUsername());
    }

    /** Tests the password property accessors. */
    @Test
    public void testPasswordGetterSetter() {
        customer.setPassword("newPass");
        assertEquals("newPass", customer.getPassword());
    }

    /** Tests the first name property accessors. */
    @Test
    public void testFirstnameGetterSetter() {
        customer.setFirstname("NewFirst");
        assertEquals("NewFirst", customer.getFirstname());
    }

    /** Tests the last name property accessors. */
    @Test
    public void testLastnameGetterSetter() {
        customer.setLastname("NewLast");
        assertEquals("NewLast", customer.getLastname());
    }

    /** Tests the phone number property accessors. */
    @Test
    public void testPhoneNumberGetterSetter() {
        customer.setPhonenumber("99999");
        assertEquals("99999", customer.getPhonenumber());
    }

    /** Tests the email address property accessors. */
    @Test
    public void testEmailAddressGetterSetter() {
        EmailAddress newAddress = new EmailAddress("new@example.com");
        customer.setEmailaddress(newAddress);
        assertEquals(newAddress, customer.getEmailAddress());
    }

    /** Tests the email provider property accessors. */
    @Test
    public void testEmailProviderStubGetterSetter() {
        customer.setEmailProvider(customerProvider);
        assertEquals(customerProvider, customer.getEmailProvider());
    }

    /** Tests the address property accessors. */
    @Test
    public void testAddressGetterSetter() {
        EmailAddress newEmailAddress = new EmailAddress("GiannisP@gmail.com");
        customer.setEmailaddress(newEmailAddress);
        assertEquals(newEmailAddress, customer.getEmailAddress());
    }

    /** Verifies that editData correctly updates the username (Choice "1"). */
    @Test
    public void editData_ChangeUsername() {
        customer.editData("1", "giannis15", null, null);
        assertEquals("giannis15", customer.getUsername());
    }

    /** Verifies that editData correctly updates the password (Choice "2"). */
    @Test
    public void editData_ChangePassword() {
        customer.editData("2", "gianis123!", null, null);
        assertEquals("gianis123!", customer.getPassword());
    }

    /** Verifies that editData correctly updates the phone number (Choice "5"). */
    @Test
    public void editData_ChangePhoneNumber() {
        customer.editData("5", "6987654321", null, null);
        assertEquals("6987654321", customer.getPhonenumber());
    }

    /** Verifies that editData correctly updates the physical address (Choice "3"). */
    @Test
    public void editData_ChangeAddress() {
        Address newaddr = new Address("Solonos", "25", "Athens", "Greece", "10672");
        customer.editData("3", null, newaddr, null);
        assertEquals(newaddr, customer.getAddress());
    }

    /** Verifies that editData correctly updates the email address (Choice "4"). */
    @Test
    public void editData_ChangeEmail() {
        EmailAddress newEmail = new EmailAddress("giannis15@mail.com");
        customer.editData("4", null, null, newEmail);
        assertEquals(newEmail, customer.getEmailAddress());
    }

    /** Verifies that editData does not modify data when an unhandled choice is provided. */
    @Test
    public void editData_InvalidChoice() {
        String oldusername = customer.getUsername();
        customer.editData("6", "kostas", null, null);
        assertEquals(oldusername, customer.getUsername());
    }

    /** Verifies that an invalid choice outside specified bounds results in an exception. */
    @Test(expected = IllegalArgumentException.class)
    public void editData_ErrorChoiceThrowsException() {
        customer.editData("10", "giannis7", null, null);
    }

    /** Verifies that a null choice results in an exception. */
    @Test(expected = IllegalArgumentException.class)
    public void editData_NullChoiceThrowsException() {
        customer.editData(null, "giannis77", null, null);
    }

    /** * Verifies that editData correctly processes specific choices even when
     * multiple non-null parameters are provided for other choices.
     */
    @Test
    public void editData_withMultipleEdits() {
        EmailAddress newEmail = new EmailAddress("giannis15@mail.com");
        customer.editData("1", "giannis15", null, newEmail);
        assertEquals("giannis15", customer.getUsername());
        assertEquals(new EmailAddress("giannis@mail.com"), customer.getEmailAddress());
    }

    /** Tests the full reply-to-email workflow between two users. */
    @Test
    public void replyToEmail() {
        EmailMessage original = new EmailMessage(sender.getEmailAddress(), recipient.getEmailAddress(), "Original", "Original body", new Date());
        recipient.getEmailProvider().saveInboxEmails(original);

        sender.replyToEmail(sender, recipient, original, "Reply", "Reply body", new Date());

        assertTrue(original.isReplied());
        assertEquals(2, recipient.getEmailProvider().getInboxEmails().size());
        EmailMessage replyMsg = recipient.getEmailProvider().getInboxEmails().get(1);
        assertEquals("Reply", replyMsg.getSubject());
        assertEquals("Reply body", replyMsg.getBody());
        assertTrue(replyMsg.isReplyMessage());
        assertEquals(1, sender.getEmailProvider().getSentEmails().size());
    }

    /** Tests the basic send-email functionality between two users. */
    @Test
    public void sendEmail() {
        sender.sendEmail(sender, recipient, "Hello", "Body text", new Date());

        assertEquals(1, recipient.getEmailProvider().getInboxEmails().size());
        EmailMessage inboxMsg = recipient.getEmailProvider().getInboxEmails().get(0);
        assertEquals("Hello", inboxMsg.getSubject());
        assertEquals("Body text", inboxMsg.getBody());
        assertFalse(inboxMsg.isReplyMessage());

        assertEquals(1, sender.getEmailProvider().getSentEmails().size());
    }

    /** Tests the delivery-style email reply workflow. */
    @Test
    public void deliverEmail() {
        EmailMessage original = new EmailMessage(sender.getEmailAddress(), recipient.getEmailAddress(),
                "Original", "Original body", new Date());

        sender.deliverEmail(sender, recipient, original, "Delivered", "Delivered body", true, new Date());

        assertTrue(original.isReplied());
        assertEquals(1, recipient.getEmailProvider().getInboxEmails().size());
        EmailMessage deliveredMsg = recipient.getEmailProvider().getInboxEmails().get(0);
        assertEquals("Delivered", deliveredMsg.getSubject());
        assertEquals("Delivered body", deliveredMsg.getBody());
        assertTrue(deliveredMsg.isReplyMessage());
        assertEquals(1, sender.getEmailProvider().getSentEmails().size());
    }

    /** Verifies that the setEmailRead method correctly updates a message's read status. */
    @Test
    public void readEmail() {
        EmailMessage msg = new EmailMessage(sender.getEmailAddress(), recipient.getEmailAddress(),
                "Subject", "Body", new Date());
        assertFalse(msg.isRead());

        sender.setEmailRead(msg);
        assertTrue(msg.isRead());
    }

    /** Cleans up repositories after each test to ensure state isolation. */
    @After
    public void tearDownTest(){
        EmployeeDAOMemory.getInstance().clear();
        CustomerDAOMemory.getInstance().getCustomers().clear();
    }
}