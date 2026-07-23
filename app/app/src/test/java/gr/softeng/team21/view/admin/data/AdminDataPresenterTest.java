package gr.softeng.team21.view.admin.data;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.domain.Admin;

/**
 * Unit tests for {@link AdminDataPresenter}.
 * Verifies data loading from the Singleton, saving logic, and the critical detection of
 * unsaved changes upon pressing the back button.
 * @author Γιάννης Μονοχολιάς
 */
public class AdminDataPresenterTest {

    private AdminDataPresenter presenter;
    private AdminDataViewStub viewStub;
    private Admin admin;

    @Before
    public void setUp() {
        viewStub = new AdminDataViewStub();
        presenter = new AdminDataPresenter(viewStub);

        // Αρχικοποίηση Singleton με default τιμές για το Test
        admin = Admin.getInstance();
        admin.setUsername("admin21");
        admin.setPassword("pass123");
        admin.setFirstname("John");
        admin.setLastname("Doe");
    }

    @Test
    public void loadAdminDataPopulatesView() {
        presenter.loadAdminData();

        Assert.assertEquals("admin21", viewStub.getUsername());
        Assert.assertEquals("pass123", viewStub.getPassword());
        Assert.assertEquals("John", viewStub.getFirstName());
        Assert.assertEquals("Doe", viewStub.getLastName());
    }

    @Test
    public void onSaveClickedUpdatesDataAndShowsSuccess() {
        presenter.loadAdminData();

        viewStub.setUsername("super_admin");
        viewStub.setFirstName("George");

        presenter.onSaveClicked();

        Assert.assertTrue(viewStub.getSuccessMessage().contains("επιτυχώς"));

        // Επαλήθευση απευθείας στο Domain Singleton
        Assert.assertEquals("super_admin", admin.getUsername());
        Assert.assertEquals("George", admin.getFirstname());
    }

    @Test
    public void onBackPressedWithNoChangesFinishesActivity() {
        presenter.loadAdminData();

        presenter.onBackPressed();

        Assert.assertFalse(viewStub.isUnsavedDialogShown());
        Assert.assertTrue(viewStub.isFinished());
    }

    @Test
    public void onBackPressedWithChangesShowsWarningDialog() {
        presenter.loadAdminData();

        viewStub.setFirstName("ChangedName");

        presenter.onBackPressed();

        Assert.assertTrue(viewStub.isUnsavedDialogShown());
        Assert.assertFalse(viewStub.isFinished());
    }

    @Test
    public void onBackPressedAfterSaveDoesNotShowWarning() {
        presenter.loadAdminData();

        viewStub.setFirstName("ChangedName");
        presenter.onSaveClicked();

        presenter.onBackPressed();

        Assert.assertFalse(viewStub.isUnsavedDialogShown());
        Assert.assertTrue(viewStub.isFinished());
    }

    @Test
    public void onDiscardChangesConfirmedFinishesActivity() {
        presenter.onDiscardChangesConfirmed();
        Assert.assertTrue(viewStub.isFinished());
    }
}