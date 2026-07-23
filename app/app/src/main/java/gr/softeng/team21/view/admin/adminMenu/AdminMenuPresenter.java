package gr.softeng.team21.view.admin.adminMenu;

/**
 * Presenter for the Admin Menu dashboard.
 * Coordinates user interactions with the menu buttons and triggers
 * the appropriate navigation routes in the View.
 * @author Αλέξανρδος Δρακάκης
 */
public class AdminMenuPresenter {
    private AdminMenuView view;

    /**
     * Initializes the presenter with the provided view interface.
     * @param view The view implementation (Activity or Stub).
     */
    public AdminMenuPresenter(AdminMenuView view) {
        this.view = view;
    }

    /**
     * Triggered when the user selects the option to edit their personal data.
     */
    public void onEditDataClicked() {
        view.navigateToEditData();
    }

    /**
     * Triggered when the user selects the option to manage update requests.
     */
    public void onRequestsClicked() {
        view.navigateToRequests();
    }

    /**
     * Triggered when the user selects the option to create a new employee.
     */
    public void onCreateEmployeeClicked() {
        view.navigateToCreateEmployee();
    }

    /**
     * Triggered when the user selects the option to delete an employee.
     */
    public void onDeleteEmployeeClicked() {
        view.navigateToDeleteEmployee();
    }

    /**
     * Triggered when the user selects the option to change product quantities.
     */
    public void onChangeQuantitiesClicked() {
        view.navigateToChangeQuantities();
    }
}