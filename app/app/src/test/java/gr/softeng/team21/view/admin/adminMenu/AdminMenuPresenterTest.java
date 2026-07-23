package gr.softeng.team21.view.admin.adminMenu;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link AdminMenuPresenter}.
 * This suite verifies the routing logic of the Admin Menu dashboard,
 * ensuring that user interactions accurately trigger the expected
 * navigation events within the View.
 * @author Αλέξανδρος Δρακάκης
 */
public class AdminMenuPresenterTest {

    private AdminMenuPresenter presenter;
    private AdminMenuViewStub viewStub;

    /**
     * Initializes the testing environment before each test case.
     * Instantiates the presenter with its view stub.
     */
    @Before
    public void setUp() {
        viewStub = new AdminMenuViewStub();
        presenter = new AdminMenuPresenter(viewStub);
    }

    /**
     * Verifies that clicking the edit data button triggers the correct navigation method.
     */
    @Test
    public void onEditDataClickedNavigatesToEditData() {
        presenter.onEditDataClicked();
        Assert.assertTrue("Should navigate to Admin Data", viewStub.isNavigateToEditDataCalled());
    }

    /**
     * Verifies that clicking the requests button triggers the correct navigation method.
     */
    @Test
    public void onRequestsClickedNavigatesToRequests() {
        presenter.onRequestsClicked();
        Assert.assertTrue("Should navigate to Requests", viewStub.isNavigateToRequestsCalled());
    }

    /**
     * Verifies that clicking the create employee button triggers the correct navigation method.
     */
    @Test
    public void onCreateEmployeeClickedNavigatesToCreateEmployee() {
        presenter.onCreateEmployeeClicked();
        Assert.assertTrue("Should navigate to Create Employee", viewStub.isNavigateToCreateEmployeeCalled());
    }

    /**
     * Verifies that clicking the delete employee button triggers the correct navigation method.
     */
    @Test
    public void onDeleteEmployeeClickedNavigatesToDeleteEmployee() {
        presenter.onDeleteEmployeeClicked();
        Assert.assertTrue("Should navigate to Delete Employee", viewStub.isNavigateToDeleteEmployeeCalled());
    }

    /**
     * Verifies that clicking the quantities button triggers the correct navigation method.
     */
    @Test
    public void onChangeQuantitiesClickedNavigatesToChangeQuantities() {
        presenter.onChangeQuantitiesClicked();
        Assert.assertTrue("Should navigate to Change Quantities", viewStub.isNavigateToChangeQuantitiesCalled());
    }
}