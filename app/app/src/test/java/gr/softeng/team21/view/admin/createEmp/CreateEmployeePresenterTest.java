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

public class CreateEmployeePresenterTest {

    CreateEmployeePresenter presenter;
    CreateEmployeeViewStub view;
    EmployeeDAOMemory employeeDAOMemory;

    @Before
    public void setUp() throws Exception {

        MemoryInitializer.prepareData();

        employeeDAOMemory = EmployeeDAOMemory.getInstance();
        view = new CreateEmployeeViewStub();
        presenter = new CreateEmployeePresenter();
        presenter.setView(view);

    }

    @Test
    public void saveData() {

        int before = employeeDAOMemory.getEmployees().size();

        presenter.saveData("alexdrak" , new EmailAddress("alexdrak@team21.gr") , "Alex" , "Drakakis" , "6900000000" ,  "Agia Galini", "123456" , "Emp12" , 1000 );

        int after = employeeDAOMemory.getEmployees().size();

        assertEquals(after , before + 1);

    }
}