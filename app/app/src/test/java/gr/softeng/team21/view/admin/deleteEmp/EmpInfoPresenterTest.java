package gr.softeng.team21.view.admin.deleteEmp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.view.admin.createEmp.CreateEmployeeViewStub;

public class EmpInfoPresenterTest {

    EmpInfoPresenter presenter;
    EmpInfoViewStub view;
    EmployeeDAOMemory employeeDAOMemory;
    @Before
    public void setUp() throws Exception {

        MemoryInitializer.prepareData();

        employeeDAOMemory = EmployeeDAOMemory.getInstance();
        view = new EmpInfoViewStub();
        presenter = new EmpInfoPresenter();

    }

    @Test
    public void deleteEmp() {

        int before = employeeDAOMemory.getEmployees().size();

        presenter.deleteEmp("Δήμητρα" , "Γεωργίου" , "6941112223");

        int after = employeeDAOMemory.getEmployees().size();

        assertEquals(before - 1 , after);
    }
}