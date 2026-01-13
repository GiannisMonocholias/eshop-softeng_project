package gr.softeng.team21.view.admin.createEmp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.contact.Address;
import gr.softeng.team21.util.Date;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.domain.EmployeeState;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;

/**
 * In this test we check the methods of the presenter who is
 * responsible for the creation of an employee.
 */

public class CreateEmployeePresenterTest {

    CreateEmployeePresenter presenter;
    CreateEmployeeViewStub view;
    EmployeeDAOMemory employeeDAOMemory;

    /**
     *
     * Prepares data in memory and initializes presenter
     * view and memory objects.
     *
     */
    @Before
    public void setUp()  {

        MemoryInitializer.prepareData();

        employeeDAOMemory = EmployeeDAOMemory.getInstance();
        view = new CreateEmployeeViewStub();
        presenter = new CreateEmployeePresenter();
        presenter.setView(view);

    }

    /**
     * Tests if the new employee has succesfully been added in the list.
     */
    @Test
    public void saveData() {

        int before = employeeDAOMemory.getEmployees().size();

        presenter.saveData("alexdrak" , new EmailAddress("alexdrak@team21.gr") , "Alex" , "Drakakis" , "6900000000" ,  "Agia Galini", "123456" , "Emp12" , 1000 );

        int after = employeeDAOMemory.getEmployees().size();


        assertEquals(after , before + 1);

    }
}