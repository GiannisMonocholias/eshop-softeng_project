package gr.softeng.team21.view.user.EditData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the {@link UserEditDataPresenter} class.
 * These tests verify the navigation logic when selecting different options from the edit menu.
 * @author PAVLOS GRATSANIS
 */
public class UserEditDataPresenterTest {
    private UserEditDataViewStub view;
    private UserEditDataPresenter presenter;

    /**
     * Sets up the test class before each test case.
     * Initializes the view stub and the presenter.
     */
    @Before
    public void setUp() throws Exception {
        view = new UserEditDataViewStub();
        presenter = new UserEditDataPresenter(view);
    }

    /**
     * Verifies that the correct navigation method is called on the view
     * corresponding to the selected index in the menu list.
     * 0 -> Username, 1 -> Password, 2 -> Address, 3 -> Email, 4 -> Phone.
     */
    @Test
    public void selection() {
        presenter.handleSelection(0);
        Assert.assertEquals(1, view.getUsernameCount());

        presenter.handleSelection(1);
        Assert.assertEquals(1, view.getPasswordCount());

        presenter.handleSelection(2);
        Assert.assertEquals(1, view.getAddressCount());

        presenter.handleSelection(3);
        Assert.assertEquals(1, view.getEmailCount());

        presenter.handleSelection(4);
        Assert.assertEquals(1, view.getPhoneCount());
    }
}