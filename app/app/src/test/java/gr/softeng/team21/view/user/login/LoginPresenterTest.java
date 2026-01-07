package gr.softeng.team21.view.user.login;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.view.util.UserType;

public class LoginPresenterTest {

    private LoginPresenter presenter;
    private LoginViewStub viewStub;

    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        viewStub = new LoginViewStub();
        presenter = new LoginPresenter(viewStub);
    }


    @Test
    public void onLoginEmptyCredentialsShowsError() {
        viewStub.setUsername("");
        viewStub.setPassword("");
        presenter.onLogin();
        Assert.assertEquals("Παρακαλώ συμπληρώστε όλα τα πεδία.", viewStub.getErrorMessage());
    }

    @Test
    public void onLoginWrongCredentialsShowsError() {
        viewStub.setUsername("wrong");
        viewStub.setPassword("wrong");
        presenter.onLogin();
        Assert.assertEquals("Λάθος όνομα χρήστη ή κωδικός.", viewStub.getErrorMessage());
    }

    @Test
    public void onLoginSuccessCustomer() {
        viewStub.setUsername("nickgeorg");
        viewStub.setPassword("pass1234");
        presenter.onLogin();
        Assert.assertEquals(UserType.CUSTOMER, viewStub.getNavigatedUserType());
        Assert.assertEquals("Επιτυχής σύνδεση!", viewStub.getSuccessMessage());
    }

    @Test
    public void onLoginSuccessDeliverer() {
        viewStub.setUsername("n_stamos");
        viewStub.setPassword("pass1246");
        presenter.onLogin();
        Assert.assertEquals(UserType.DELIVERER, viewStub.getNavigatedUserType());
    }

    @Test
    public void onRegisterNavigates() {
        presenter.onRegister();
        Assert.assertTrue(viewStub.isRegisterCalled());
    }

    @Test
    public void loginResetClearsFields() {
        viewStub.setUsername("old_text");
        viewStub.setPassword("old_text");

        presenter.loginReset();

        Assert.assertTrue(viewStub.isFieldsReset());
        Assert.assertEquals("", viewStub.getUsername());
        Assert.assertEquals("", viewStub.getPassword());
    }

    @Test
    public void onLoginOrderPreparationSuccess() {
        viewStub.setUsername("g_nikolaou");
        viewStub.setPassword("pass1240");

        presenter.onLogin();

        Assert.assertEquals(UserType.ORDER_PREPARATION_EMPLOYEE, viewStub.getNavigatedUserType());
        Assert.assertEquals("Επιτυχής σύνδεση!", viewStub.getSuccessMessage());
    }

    @Test
    public void onLoginUpdateCatalogueSuccess() {
        viewStub.setUsername("d_georgiou");
        viewStub.setPassword("pass1243");

        presenter.onLogin();

        Assert.assertEquals(UserType.UPDATE_CATALOGUE_EMPLOYEE, viewStub.getNavigatedUserType());
        Assert.assertEquals("Επιτυχής σύνδεση!", viewStub.getSuccessMessage());
    }


}