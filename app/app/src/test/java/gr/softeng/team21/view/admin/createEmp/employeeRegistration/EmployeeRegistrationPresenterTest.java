package gr.softeng.team21.view.admin.createEmp.employeeRegistration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.domain.CustomerServiceEmployee;
import gr.softeng.team21.domain.Deliverer;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;
import gr.softeng.team21.view.admin.createEmp.employeeRegistration.EmployeeRegistrationPresenter;

/**
 * Unit tests verifying the functionality of the {@link EmployeeRegistrationPresenter}.
 * It tests the dynamic UI modifications, the generation of the correct subclass instances,
 * validation errors, and asynchronous storage behaviors.
 * @author Γιάννης Μονοχολιάς
 */
public class EmployeeRegistrationPresenterTest {

    private EmployeeRegistrationPresenter presenter;
    private EmployeeRegistrationViewStub viewStub;

    /**
     * Initializes the memory databases and instantiates the presenter utilizing
     * the Stub view.
     */
    @Before
    public void setUp() {
        MemoryInitializer.prepareData();
        viewStub = new EmployeeRegistrationViewStub();
        presenter = new EmployeeRegistrationPresenter(viewStub, EmployeeDAOMemory.getInstance(), UserCredentialsDAOMemory.getInstance());
    }

    /**
     * Verifies that selecting the "Deliverer" type correctly adapts the title
     * and forces the extra 'max quantity' field to become visible.
     */
    @Test
    public void setupUIForDeliverer_ShowsSpecificFields() {
        presenter.setupUIForType("DELIVERER");
        Assert.assertEquals("Εγγραφή Διανομέα", viewStub.getHeaderTitle());
        Assert.assertTrue(viewStub.isDelivererFieldsVisible());
    }

    /**
     * Ensures that attempting to register an employee with incomplete basic information
     * aborts the process and displays an error message.
     */
    @Test
    public void onSubmitClicked_WithMissingFields_ShowsError() {
        presenter.setupUIForType("CUSTOMER_SERVICE");
        viewStub.setUsername(""); // Intentionally missing data

        presenter.onSubmitClicked();

        Assert.assertNotNull(viewStub.getErrorMessage());
        Assert.assertNull(viewStub.getConfirmedEmployee());
    }

    /**
     * Verifies that providing valid input generates the correct subclass
     * (CustomerServiceEmployee in this case) and passes it to the confirmation dialog.
     */
    @Test
    public void onSubmitClicked_CreatesCorrectEmployeeType_AndTriggersDialog() {
        presenter.setupUIForType("CUSTOMER_SERVICE");
        viewStub.setUsername("emp_new");
        viewStub.setPassword("pass123");
        viewStub.setFirstName("John");
        viewStub.setLastName("Doe");
        viewStub.setEmail("john@test.com");
        viewStub.setSalary("1200");
        viewStub.setWorkingHours("40");

        presenter.onSubmitClicked();

        Assert.assertNotNull(viewStub.getConfirmedEmployee());
        Assert.assertTrue(viewStub.getConfirmedEmployee() instanceof CustomerServiceEmployee);
    }

    /**
     * Specifically tests the Deliverer logic. Asserts that if a Deliverer is being created
     * but the unique 'maxQuantity' field is left blank, an error is generated.
     */
    @Test
    public void onSubmitClicked_ForDeliverer_WithoutQuantity_ShowsError() {
        presenter.setupUIForType("DELIVERER");
        viewStub.setUsername("deliv_new");
        viewStub.setPassword("pass123");
        viewStub.setFirstName("Mark");
        viewStub.setLastName("Smith");
        viewStub.setEmail("mark@test.com");
        viewStub.setSalary("1000");
        viewStub.setWorkingHours("40");
        viewStub.setMaxQuantity(""); // Intentionally missing Quantity!

        presenter.onSubmitClicked();

        Assert.assertNotNull(viewStub.getErrorMessage());
        Assert.assertNull(viewStub.getConfirmedEmployee());
    }

    /**
     * Tests the final asynchronous saving procedure to both Employee and Credentials DAOs.
     * Uses .join() to safely await CompletableFutures without try-catch blocks.
     */
    @Test
    public void onRegistrationConfirmed_SavesEmployeeAsync() {
        presenter.setupUIForType("DELIVERER");

        // Simulating the object created in onSubmitClicked
        Deliverer testDeliverer = new Deliverer("fast_d", "George", "pass1", "Jones", "690", null, "E100", 0, 1000, 40, null, null, 10, true);

        presenter.onRegistrationConfirmed(testDeliverer);

        Assert.assertTrue(viewStub.getSuccessMessage().contains("επιτυχώς"));
        Assert.assertTrue(viewStub.isFinished());

        // Verify in MemoryDAO asynchronously that the employee is indeed stored
        Assert.assertNotNull(EmployeeDAOMemory.getInstance().getEmployee("E100").join());
    }
}