package gr.softeng.team21.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.util.Date;

public class EmployeeTest {
    private Employee employee;
    private Date hireDate;

    @Before 
    public void SetUp(){
        EmployeeDAOMemory.getInstance().clear(); // Καθαρισμός για απομόνωση

        hireDate = new Date(3,5,2025);
        employee =new Employee("GP","Giorgos","abcd123","Papadopoulos","3029761482",
                new EmailAddress("GP@gmail.com"),"E_1",100,1000,8,
                EmployeeState.ACTIVE, hireDate);
    }

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

    @Test
    public void testEmployeeIdGetterSetter() {
        employee.setEmployeeId("E_2");
        assertEquals("E_2", employee.getEmployeeId());
    }

    @Test
    public void testBonusGetterSetter() {
        employee.setBonus(200);
        assertEquals(200, employee.getBonus());
    }

    @Test
    public void testSalaryGetterSetter() {
        employee.setSalary(1500);
        assertEquals(1500, employee.getSalary());
    }

    @Test
    public void testEmployeeStateGetterSetter() {
        employee.setEmployeeState(EmployeeState.INACTIVE);
        assertEquals(EmployeeState.INACTIVE, employee.getEmployeeState());
    }

    @Test
    public void testWorkingHoursGetterSetter() {
        employee.setWorkingHours(8);
        assertEquals(8, employee.getWorkingHours());
    }


    @Test
    public void testSetHireDate_WhenAlreadySet() {
        Date newDate = new Date(5, 1, 2026);

        // employee already has hire date, so setHireDate() will not have any effect
        employee.setHireDate(newDate);
        assertEquals(hireDate, employee.getHireDate());
    }

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

    @After
    public void tearDownTest(){
        EmployeeDAOMemory.getInstance().clear();
    }
}