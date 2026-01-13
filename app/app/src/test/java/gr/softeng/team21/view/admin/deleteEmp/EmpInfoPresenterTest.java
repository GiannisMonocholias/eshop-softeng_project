package gr.softeng.team21.view.admin.deleteEmp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.view.admin.createEmp.CreateEmployeeViewStub;

/**
 * In this test we check that the employee the admin is going
 * to delete is correctly removed from the list.
 */

public class EmpInfoPresenterTest {

    EmpInfoPresenter presenter;
    EmpInfoViewStub view;
    EmployeeDAOMemory employeeDAOMemory;

    /**
     * Prepares data for the test and initializes dao, view and presenter objects.
     */

    @Before
    public void setUp() {

        MemoryInitializer.prepareData();

        employeeDAOMemory = EmployeeDAOMemory.getInstance();
        view = new EmpInfoViewStub();
        presenter = new EmpInfoPresenter();

    }

    /**
     * Checks that each time we delete an employee the size of list
     * is reduced by one.
     */

    @Test
    public void deleteEmp() {

        int before = employeeDAOMemory.getEmployees().size();

        presenter.deleteEmp("Δήμητρα" , "Γεωργίου" , "6941112223");

        int after = employeeDAOMemory.getEmployees().size();

        assertEquals(before - 1 , after);
    }
}