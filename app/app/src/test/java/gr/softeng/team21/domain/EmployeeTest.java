package gr.softeng.team21.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.util.Date;

/**
 * Unit tests for the {@link Employee} base class.
 * This suite verifies the core functionality shared by all employee types,
 * including identity management, compensation details, operational state,
 * and the specific business logic regarding the hire date.
 * @author Γιάννης Μονοχολιάς
 */
public class EmployeeTest {
    private Employee employee;
    private Date hireDate;

    /**
     * Initializes the testing environment before each test.
     * Clears the employee repository to ensure isolation and creates a
     * generic Employee instance with sample data.
     */
    @Before
    public void SetUp(){
        EmployeeDAOMemory.getInstance().clear(); // Καθαρισμός για απομόνωση

        hireDate = new Date(3,5,2025);
        employee =new Employee("GP","Giorgos","abcd123","Papadopoulos","3029761482",
                new EmailAddress("GP@gmail.com"),"E_1",100,1000,8,
                EmployeeState.ACTIVE, hireDate);
    }

    /**
     * Verifies that the constructor correctly populates all inherited
     * and class-specific fields upon instantiation.
     */
    @Test
    public void testConstructorInitializesFields() {
        assertEquals("GP", employee.getUsername());
        assertEquals("Giorgos", employee.getFirstname());
        assertEquals("abcd123", employee.getPassword());
        assertEquals("Papadopoulos", employee.getLastname());
        assertEquals("3029761482", employee.getPhonenumber());
        assertEquals("GP@gmail.com", employee.getEmailAddress().getAddress());
        assertEquals("E_1", employee.getEmployeeId());
        assertEquals(100, employee.getBonus());
        assertEquals(1000, employee.getSalary());
        assertEquals(8, employee.getWorkingHours());
        assertEquals(EmployeeState.ACTIVE, employee.getEmployeeState());
        assertEquals(hireDate, employee.getHireDate());
    }

    /**
     * Tests the employee ID property accessors.
     */
    @Test
    public void testEmployeeIdGetterSetter() {
        employee.setEmployeeId("E_2");
        assertEquals("E_2", employee.getEmployeeId());
    }

    /**
     * Tests the bonus amount property accessors.
     */
    @Test
    public void testBonusGetterSetter() {
        employee.setBonus(200);
        assertEquals(200, employee.getBonus());
    }

    /**
     * Tests the salary property accessors.
     */
    @Test
    public void testSalaryGetterSetter() {
        employee.setSalary(1500);
        assertEquals(1500, employee.getSalary());
    }

    /**
     * Tests the operational state (ACTIVE/INACTIVE) property accessors.
     */
    @Test
    public void testEmployeeStateGetterSetter() {
        employee.setEmployeeState(EmployeeState.INACTIVE);
        assertEquals(EmployeeState.INACTIVE, employee.getEmployeeState());
    }

    /**
     * Tests the working hours property accessors.
     */
    @Test
    public void testWorkingHoursGetterSetter() {
        employee.setWorkingHours(8);
        assertEquals(8, employee.getWorkingHours());
    }

    /**
     * Verifies the business rule that a hire date cannot be modified
     * once it has been established.
     */
    @Test
    public void testSetHireDate_WhenAlreadySet() {
        Date newDate = new Date(5, 1, 2026);

        // employee already has hire date, so setHireDate() will not have any effect
        employee.setHireDate(newDate);
        assertEquals(hireDate, employee.getHireDate());
    }

    /**
     * Verifies that the hire date can be set if it was initially null,
     * satisfying the initialization logic for partially formed objects.
     */
    @Test
    public void testSetHireDate_WhenInitiallyNull() {
        Date newDate = new Date(5, 1, 2026);

        Employee employee2 = new Employee("GP","Giorgos","abcd123","Papadopoulos","3029761482",
                new EmailAddress("GP@gmail.com"),"E_2",100,1000,8,
                EmployeeState.ACTIVE, null);

        assertNull(employee2.getHireDate());

        // employee does not have hire date, so setHireDate() will succeed
        employee2.setHireDate(newDate);
        assertEquals(newDate, employee2.getHireDate());
    }

    /**
     * Cleans up the employee repository after each test to maintain a clean state.
     */
    @After
    public void tearDownTest(){
        EmployeeDAOMemory.getInstance().clear();
    }
}