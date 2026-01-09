package gr.softeng.team21.view.user.login;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.view.util.UserType;

/**
 * Unit tests for {@link LoginPresenter}.
 * This suite verifies the authentication flow, ensuring that users are
 * correctly validated and navigated to their respective home pages based
 * on their roles (Customer, Deliverer, Order Preparation, etc.).
 * @author Γιάννης Μονοχολιάς
 */
public class LoginPresenterTest {

    private LoginPresenter presenter;
    private LoginViewStub viewStub;

    /**
     * Initializes the testing environment before each test case.
     * Prepares memory data and instantiates the presenter with its view stub.
     */
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();
        viewStub = new LoginViewStub();
        presenter = new LoginPresenter(viewStub);
    }

    /**
     * Verifies that an error message is shown when attempting to log in
     * with empty credentials.
     */
    @Test
    public void onLoginEmptyCredentialsShowsError() {
        viewStub.setUsername("");
        viewStub.setPassword("");
        presenter.onLogin();
        Assert.assertEquals("Παρακαλώ συμπληρώστε όλα τα πεδία.", viewStub.getErrorMessage());
    }

    /**
     * Verifies that an error message is shown when providing
     * incorrect username or password.
     */
    @Test
    public void onLoginWrongCredentialsShowsError() {
        viewStub.setUsername("wrong");
        viewStub.setPassword("wrong");
        presenter.onLogin();
        Assert.assertEquals("Λάθος όνομα χρήστη ή κωδικός.", viewStub.getErrorMessage());
    }

    /**
     * Verifies successful login and navigation for a Customer user.
     */
    @Test
    public void onLoginSuccessCustomer() {
        viewStub.setUsername("nickgeorg");
        viewStub.setPassword("pass1234");
        presenter.onLogin();
        Assert.assertEquals(UserType.CUSTOMER, viewStub.getNavigatedUserType());
        Assert.assertEquals("Επιτυχής σύνδεση!", viewStub.getSuccessMessage());
    }

    /**
     * Verifies successful login and navigation for a Deliverer user.
     */
    @Test
    public void onLoginSuccessDeliverer() {
        viewStub.setUsername("n_stamos");
        viewStub.setPassword("pass1246");
        presenter.onLogin();
        Assert.assertEquals(UserType.DELIVERER, viewStub.getNavigatedUserType());
    }

    /**
     * Verifies that the register navigation is correctly triggered.
     */
    @Test
    public void onRegisterNavigates() {
        presenter.onRegister();
        Assert.assertTrue(viewStub.isRegisterCalled());
    }

    /**
     * Verifies that the reset functionality correctly clears the UI fields.
     */
    @Test
    public void loginResetClearsFields() {
        viewStub.setUsername("old_text");
        viewStub.setPassword("old_text");

        presenter.loginReset();

        Assert.assertTrue(viewStub.isFieldsReset());
        Assert.assertEquals("", viewStub.getUsername());
        Assert.assertEquals("", viewStub.getPassword());
    }

    /**
     * Verifies successful login and navigation for an Order Preparation Employee.
     */
    @Test
    public void onLoginOrderPreparationSuccess() {
        viewStub.setUsername("g_nikolaou");
        viewStub.setPassword("pass1240");

        presenter.onLogin();

        Assert.assertEquals(UserType.ORDER_PREPARATION_EMPLOYEE, viewStub.getNavigatedUserType());
        Assert.assertEquals("Επιτυχής σύνδεση!", viewStub.getSuccessMessage());
    }

    /**
     * Verifies successful login and navigation for an Update Catalogue Employee.
     */
    @Test
    public void onLoginUpdateCatalogueSuccess() {
        viewStub.setUsername("d_georgiou");
        viewStub.setPassword("pass1243");

        presenter.onLogin();

        Assert.assertEquals(UserType.UPDATE_CATALOGUE_EMPLOYEE, viewStub.getNavigatedUserType());
        Assert.assertEquals("Επιτυχής σύνδεση!", viewStub.getSuccessMessage());
    }
}