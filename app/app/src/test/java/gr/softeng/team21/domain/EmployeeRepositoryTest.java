package gr.softeng.team21.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;

public class EmployeeRepositoryTest {

    private EmployeeRepository repo;
    private Employee employee;

    @Before
    public void setUp() {
        repo = EmployeeRepository.getInstance();



        employee = new Employee("Α001", "Alex","123" ,"Drakakis", "69696969", new EmailAddress("alexd@gmail.com") , "3232" , 1000 , 1500 , 40 , EmployeeState.ACTIVE , new Date() );
    }

    @Test
    public void testGetInstanceReturnsSameObject() {
        EmployeeRepository inst1 = EmployeeRepository.getInstance();
        EmployeeRepository inst2 = EmployeeRepository.getInstance();

        assertSame(inst1, inst2);
    }

    @Test
    public void testEmployeeAddedOnConstructionSuccess() {
        HashMap<String, Employee> map = repo.getEmployees();

        assertTrue(map.containsKey(employee.getEmployeeId()));
        assertEquals(employee, map.get(employee.getEmployeeId()));
    }


    @Test(expected = IllegalArgumentException.class)
    public void testAddEmployeeNullThrowsException() {
        repo.addEmployee(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddExistingEmployeeThrowsException() {
        repo.addEmployee(employee);
    }

    @Test
    public void testRemoveEmployeeSuccess() {
        repo.removeEmployee(employee);

        assertFalse(repo.getEmployees().containsKey(employee.getEmployeeId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveEmployeeNullThrowsException() {
        repo.removeEmployee(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveEmployeeNotInRepositoryThrowsException() {
        repo.removeEmployee(employee);

        repo.removeEmployee(employee);
    }


    @After
    public void tearDown(){
        repo.clear();
    }
}