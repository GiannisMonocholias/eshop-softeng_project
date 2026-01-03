package gr.softeng.team21.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.EmailDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;

public class UserTest {

    private Employee sender;
    private Employee recipient;
    private Customer customer;
    private EmailDAOMemory senderProvider;
    private EmailDAOMemory recipientProvider;
    private EmailDAOMemory customerProvider;

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

        // Ο User customer1 δεν χρησιμοποιείται, τον αφαιρούμε.
        // User customer1 = new Customer("giannispap", "Giannis", "pass1234", "Papadopoulos", "697123456", new EmailAddress("giannis@mail.com"), "Customer1", new Date());
    }


    @Test
    public void testUsernameGetterSetter() {
        customer.setUsername("newUser");
        assertEquals("newUser", customer.getUsername());
    }

    @Test
    public void testPasswordGetterSetter() {
        customer.setPassword("newPass");
        assertEquals("newPass", customer.getPassword());
    }

    @Test
    public void testFirstnameGetterSetter() {
        customer.setFirstname("NewFirst");
        assertEquals("NewFirst", customer.getFirstname());
    }

    @Test
    public void testLastnameGetterSetter() {
        customer.setLastname("NewLast");
        assertEquals("NewLast", customer.getLastname());
    }

    @Test
    public void testPhoneNumberGetterSetter() {
        customer.setPhonenumber("99999");
        assertEquals("99999", customer.getPhonenumber());
    }

    @Test
    public void testEmailAddressGetterSetter() {
        EmailAddress newAddress = new EmailAddress("new@example.com");
        customer.setEmailaddress(newAddress);
        assertEquals(newAddress, customer.getEmailAddress());
    }

    @Test
    public void testEmailProviderStubGetterSetter() {
        customer.setEmailProvider(customerProvider);
        assertEquals(customerProvider, customer.getEmailProvider());
    }

    @Test
    public void testAddressGetterSetter() {
        EmailAddress newEmailAddress = new EmailAddress("GiannisP@gmail.com");
        customer.setEmailaddress(newEmailAddress);
        assertEquals(newEmailAddress, customer.getEmailAddress());
    }


    @Test
    public void editData_ChangeUsername() {
        customer.editData("1", "giannis15", null, null);
        assertEquals("giannis15", customer.getUsername());
    }

    @Test
    public void editData_ChangePassword() {
        customer.editData("2", "gianis123!", null, null);
        assertEquals("gianis123!", customer.getPassword());
    }

    @Test
    public void editData_ChangePhoneNumber() {
        customer.editData("5", "6987654321", null, null);
        assertEquals("6987654321", customer.getPhonenumber());
    }

    @Test
    public void editData_ChangeAddress() {
        Address newaddr = new Address("Solonos", "25", "Athens", "Greece", "10672");
        customer.editData("3", null, newaddr, null);
        assertEquals(newaddr, customer.getAddress());
    }

    @Test
    public void editData_ChangeEmail() {
        EmailAddress newEmail = new EmailAddress("giannis15@mail.com");
        customer.editData("4", null, null, newEmail);
        assertEquals(newEmail, customer.getEmailAddress());
    }

    @Test
    public void editData_InvalidChoice() {
        String oldusername = customer.getUsername();
        customer.editData("6", "kostas", null, null);
        assertEquals(oldusername, customer.getUsername());
    }


    @Test(expected = IllegalArgumentException.class)
    public void editData_ErrorChoiceThrowsException() {
        // Error choice "10"
        customer.editData("10", "giannis7", null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void editData_NullChoiceThrowsException() {
        // Null choice
        customer.editData(null, "giannis77", null, null);
    }


    @Test
    public void editData_withMultipleEdits() {
        EmailAddress newEmail = new EmailAddress("giannis15@mail.com");


        customer.editData("1", "giannis15", null, newEmail);

        assertEquals("giannis15", customer.getUsername());

        assertEquals(new EmailAddress("giannis@mail.com"), customer.getEmailAddress());
    }

    @Test
    public void replyToEmail() {
        EmailMessage original = new EmailMessage(sender.getEmailAddress(), recipient.getEmailAddress(), "Original", "Original body");
        recipient.getEmailProvider().saveInboxEmails(original);

        sender.replyToEmail(sender, recipient, original, "Reply", "Reply body");

        assertTrue(original.isReplied());
        assertEquals(2, recipient.getEmailProvider().getInboxEmails().size());
        EmailMessage replyMsg = recipient.getEmailProvider().getInboxEmails().get(1);
        assertEquals("Reply", replyMsg.getSubject());
        assertEquals("Reply body", replyMsg.getBody());
        assertTrue(replyMsg.isReplyMessage());
        assertEquals(1, sender.getEmailProvider().getSentEmails().size());
    }

    @Test
    public void sendEmail() {
        sender.sendEmail(sender, recipient, "Hello", "Body text");

        assertEquals(1, recipient.getEmailProvider().getInboxEmails().size());
        EmailMessage inboxMsg = recipient.getEmailProvider().getInboxEmails().get(0);
        assertEquals("Hello", inboxMsg.getSubject());
        assertEquals("Body text", inboxMsg.getBody());
        assertFalse(inboxMsg.isReplyMessage());

        assertEquals(1, sender.getEmailProvider().getSentEmails().size());
    }

    @Test
    public void deliverEmail() {
        EmailMessage original = new EmailMessage(sender.getEmailAddress(), recipient.getEmailAddress(),
                "Original", "Original body");

        sender.deliverEmail(sender, recipient, original, "Delivered", "Delivered body", true);

        assertTrue(original.isReplied());
        assertEquals(1, recipient.getEmailProvider().getInboxEmails().size());
        EmailMessage deliveredMsg = recipient.getEmailProvider().getInboxEmails().get(0);
        assertEquals("Delivered", deliveredMsg.getSubject());
        assertEquals("Delivered body", deliveredMsg.getBody());
        assertTrue(deliveredMsg.isReplyMessage());
        assertEquals(1, sender.getEmailProvider().getSentEmails().size());
    }

    @Test
    public void readEmail() {
        EmailMessage msg = new EmailMessage(sender.getEmailAddress(), recipient.getEmailAddress(),
                "Subject", "Body");
        assertFalse(msg.isRead());

        sender.setEmailRead(msg);
        assertTrue(msg.isRead());
    }

    @After
    public void tearDownTest(){
        EmployeeDAOMemory.getInstance().clear();
        CustomerDAOMemory.getInstance().getCustomers().clear();
    }
}