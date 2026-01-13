package gr.softeng.team21.view.admin.deleteEmp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.view.admin.createEmp.CreateEmployeePresenter;
import gr.softeng.team21.view.admin.createEmp.CreateEmployeeViewStub;

/**
 * Here we check that each employee that admin wants to delete
 * is correctly deleted and removed out of the list.
 */

public class DeleteEmployeePresenterTest {

    DeleteEmployeePresenter presenter;
    DeleteEmployeeViewStub view;
    EmployeeDAOMemory employeeDAOMemory;

    /**
     * In setUp method the memory is prepared with the initialized data
     * and the presenter,view and dao objects are initialized.
     *
     */

    @Before
    public void setUp()  {

        MemoryInitializer.prepareData();

        employeeDAOMemory = EmployeeDAOMemory.getInstance();
        view = new DeleteEmployeeViewStub();
        presenter = new DeleteEmployeePresenter();
        presenter.setView(view);
    }

    /**
     * In this method we check if the search of an employee
     * given his username and the id returns the correct employee object.
     */

    @Test
    public void searchEmp() {

        Employee emp = presenter.searchEmp("k_papadakis" , "PREP-203");

        assertEquals("PREP-203"  , emp.getEmployeeId());

        Employee emp2 = presenter.searchEmp("l_kostop" , "PPER-390");
        assertNull(emp2);
    }
}