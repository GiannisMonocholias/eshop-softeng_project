package gr.softeng.team21.view.employee.updateCatalogueEmployee.availableRequestsToAssign;

import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.domain.Order;

public interface AvailableRequestsToAssignView {

    void showMessage(String message);

    void showError(String message);

    void onRequestAssignedSuccess(CatalogueUpdateRequest request);

    void updateList();

    void showConfirmationDialog(CatalogueUpdateRequest request, String confirmationMessage);
}
