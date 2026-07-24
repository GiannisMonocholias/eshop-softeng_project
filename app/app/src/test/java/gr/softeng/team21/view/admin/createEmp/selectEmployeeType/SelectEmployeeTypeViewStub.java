package gr.softeng.team21.view.admin.createEmp.selectEmployeeType;

import gr.softeng.team21.view.admin.createEmp.selectEmployeeType.SelectEmployeeTypeView;

/**
 * A stub implementation of the {@link SelectEmployeeTypeView} interface for unit testing.
 * Captures asynchronous count updates and navigation intents.
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public class SelectEmployeeTypeViewStub implements SelectEmployeeTypeView {

    private int csCount = 0;
    private int prepCount = 0;
    private int catCount = 0;
    private int delCount = 0;

    private String navigatedType = "";
    private String errorMessage = "";

    /** {@inheritDoc} */
    @Override
    public void showEmployeeCounts(int customerServiceCount, int orderPrepCount, int updateCatCount, int delivererCount) {
        this.csCount = customerServiceCount;
        this.prepCount = orderPrepCount;
        this.catCount = updateCatCount;
        this.delCount = delivererCount;
    }

    /** {@inheritDoc} */
    @Override
    public void showErrorMessage(String message) {
        this.errorMessage = message;
    }

    /** {@inheritDoc} */
    @Override
    public void navigateToRegistrationForm(String type) {
        this.navigatedType = type;
    }

    // --- Accessor methods for verification during testing ---

    public int getCsCount() { return csCount; }
    public int getPrepCount() { return prepCount; }
    public int getCatCount() { return catCount; }
    public int getDelCount() { return delCount; }

    public String getNavigatedType() { return navigatedType; }
    public String getErrorMessage() { return errorMessage; }
}