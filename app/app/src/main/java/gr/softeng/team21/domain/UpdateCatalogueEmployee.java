package gr.softeng.team21.domain;

import java.util.HashMap;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.ProductTypeDAOMemory;
import gr.softeng.team21.memorydao.UpdateRequestDAOMemory;
import gr.softeng.team21.util.Date;

/**
 * Represents an employee responsible for managing and updating the product catalogue.
 * This class handles the assignment and execution of catalogue update requests,
 * modifying the available product types in the system.
 * @author Γιάννης Μονοχολιάς
 */
public class UpdateCatalogueEmployee extends Employee {
    private int totalCatalogueUpdates;
    private HashMap<Integer, CatalogueUpdateRequest> assignedRequests;


    /**
     * Default constructor
     * */
    public UpdateCatalogueEmployee() {
    }

    /**
     * Constructs a new UpdateCatalogueEmployee with the specified details.
     * Initializes the catalogue update counter and the assigned requests map.
     * @param username      The unique account username.
     * @param firstname     The employee's first name.
     * @param password      The account password.
     * @param lastname      The employee's last name.
     * @param phoneNumber   The contact phone number.
     * @param emailaddress  The professional email address.
     * @param employeeId    The unique business identifier.
     * @param bonus         Performance-based bonus amount.
     * @param salary        Base salary.
     * @param workingHours  Contracted weekly working hours.
     * @param employeeState The current employment status.
     * @param hireDate      The official date of hire.
     */
    public UpdateCatalogueEmployee(String username, String firstname, String password, String lastname, String phoneNumber, EmailAddress emailaddress, String employeeId, int bonus, int salary, int workingHours, EmployeeState employeeState, Date hireDate) {
        super(username, firstname, password, lastname, phoneNumber, emailaddress, employeeId, bonus, salary, workingHours, employeeState, hireDate);
        this.totalCatalogueUpdates = 0;
        this.assignedRequests = new HashMap<>();
    }

    /**
     * @return the total number of catalogue updates executed by this employee.
     */
    public int getTotalCatalogueUpdates() {
        return totalCatalogueUpdates;
    }

    /**
     * @return a map of all catalogue update requests assigned to this employee.
     */
    public HashMap<Integer, CatalogueUpdateRequest> getAssignedRequests() {
        return assignedRequests;
    }

    /**
     * Assigns a specific update request to this employee from the global request pool.
     * @param requestId the unique identifier of the request to assign.
     * @return true if the request was successfully found and assigned,
     * false if the ID does not exist or is already assigned to this employee.
     */
    public boolean assignRequest(int requestId) {
        if (!UpdateRequestDAOMemory.getInstance().getUpdateRequests().containsKey(requestId))
            return false;

        CatalogueUpdateRequest selectedRequest = UpdateRequestDAOMemory.getInstance().getUpdateRequest(requestId);
        if (!assignedRequests.containsKey(requestId))
            assignedRequests.put(requestId, selectedRequest);
        else
            return false;
        return true;
    }

    /**
     * Retrieves a specific request from the employee's assigned list.
     * @param requestId the unique identifier of the request.
     * @return the CatalogueUpdateRequest object, or null if not found in the assigned list.
     */
    public CatalogueUpdateRequest selectRequest(int requestId) {
        if (!assignedRequests.containsKey(requestId))
            return null;

        return assignedRequests.get(requestId);
    }

    /**
     * Executes the specified catalogue update request.
     * The operation performed (Insert, Delete, or Process) depends on the request type.
     * @param request the catalogue update request to be executed.
     * @throws IllegalArgumentException if the request argument is null.
     */
    public void executeUpdate(CatalogueUpdateRequest request) {
        if (request == null)
            throw new IllegalArgumentException("Request cannot be null");

        switch (request.getType()) {
            case INSERT_PRODUCT:
                ProductTypeDAOMemory.getInstance().addProductType(request.getProduct());
                break;
            case DELETE_PRODUCT:
                ProductTypeDAOMemory.getInstance().deleteProductType(request.getProduct());
                break;
            case PROCESS_PRODUCT:
                ProductTypeDAOMemory.getInstance().processProduct(request.getProduct());
                break;
        }
        totalCatalogueUpdates++;
    }
}