package gr.softeng.team21.view.admin.adminMenu;

/**
 * View contract for the Admin Menu screen.
 * Defines the navigation methods required to access the various administrative
 * functions of the system.
 * @author Αλέξανρδος Δρακάκης
 */
public interface AdminMenuView {

    /**
     * Navigates to the screen where the admin can edit their personal data.
     */
    void navigateToEditData();

    /**
     * Navigates to the screen for managing catalogue update requests.
     */
    void navigateToRequests();

    /**
     * Navigates to the screen for creating a new employee profile.
     */
    void navigateToCreateEmployee();

    /**
     * Navigates to the screen for deleting an existing employee profile.
     */
    void navigateToDeleteEmployee();

    /**
     * Navigates to the screen for adjusting the quantities of products in the warehouse.
     */
    void navigateToChangeQuantities();
}