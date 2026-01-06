package gr.softeng.team21.view.employee.orderPreparationEmployee.assignedOrdersToPrepare;

public class AssignedOrdersToPrepareViewStub implements AssignedOrdersToPrepareView {

    private String navigatedEmployeeId = "";
    private String navigatedOrderCode = "";
    private boolean navigationCalled = false;

    @Override
    public void navigateToOrderPreparationDetails(String employeeId, String ordercode) {
        this.navigationCalled = true;
        this.navigatedEmployeeId = employeeId;
        this.navigatedOrderCode = ordercode;
    }


    public String getNavigatedEmployeeId() {
        return navigatedEmployeeId;
    }

    public String getNavigatedOrderCode() {
        return navigatedOrderCode;
    }

    public boolean isNavigationCalled() {
        return navigationCalled;
    }
}