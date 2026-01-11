package gr.softeng.team21.view.admin.deleteEmp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.view.admin.createEmp.CreateEmployeePresenter;
import gr.softeng.team21.view.admin.createEmp.CreateEmployeeViewStub;

public class DeleteEmployeePresenterTest {

    DeleteEmployeePresenter presenter;
    DeleteEmployeeViewStub view;
    EmployeeDAOMemory employeeDAOMemory;

    @Before
    public void setUp() throws Exception {

        MemoryInitializer.prepareData();

        employeeDAOMemory = EmployeeDAOMemory.getInstance();
        view = new DeleteEmployeeViewStub();
        presenter = new DeleteEmployeePresenter();
        presenter.setView(view);
    }

    @Test
    public void searchEmp() {

        Employee emp = presenter.searchEmp("k_papadakis" , "PREP-203");

        assertEquals("PREP-203"  , emp.getEmployeeId());
    }
}