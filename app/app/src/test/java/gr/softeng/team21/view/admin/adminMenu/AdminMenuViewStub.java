package gr.softeng.team21.view.admin.adminMenu;

/**
 * A stub implementation of the {@link AdminMenuView} interface for unit testing.
 * It simulates the routing actions of the Admin Menu, capturing which
 * navigation methods were invoked by the Presenter.
 * @author Αλέξανδρος Δρακάκης
 */
public class AdminMenuViewStub implements AdminMenuView {

    private boolean navigateToEditDataCalled = false;
    private boolean navigateToRequestsCalled = false;
    private boolean navigateToCreateEmployeeCalled = false;
    private boolean navigateToDeleteEmployeeCalled = false;
    private boolean navigateToChangeQuantitiesCalled = false;

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateToEditData() {
        navigateToEditDataCalled = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateToRequests() {
        navigateToRequestsCalled = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateToCreateEmployee() {
        navigateToCreateEmployeeCalled = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateToDeleteEmployee() {
        navigateToDeleteEmployeeCalled = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateToChangeQuantities() {
        navigateToChangeQuantitiesCalled = true;
    }

    // --- Accessor methods for verification during testing ---

    public boolean isNavigateToEditDataCalled() { return navigateToEditDataCalled; }
    public boolean isNavigateToRequestsCalled() { return navigateToRequestsCalled; }
    public boolean isNavigateToCreateEmployeeCalled() { return navigateToCreateEmployeeCalled; }
    public boolean isNavigateToDeleteEmployeeCalled() { return navigateToDeleteEmployeeCalled; }
    public boolean isNavigateToChangeQuantitiesCalled() { return navigateToChangeQuantitiesCalled; }
}