package gr.softeng.team21.domain;

import gr.softeng.team21.util.Date;

/**
 * Represents a request to update the product catalogue.
 * This class tracks the lifecycle of an update, including its status,
 * the type of product affected, and whether the update has been executed.
 * @author Γιάννης Μονοχολιάς
 */
public class CatalogueUpdateRequest {
    private int requestId;
    private final Date submissionDate;
    private Date dateModified;
    private AllowedRequest type;
    private ProductType product;
    private String updateDescription;
    private boolean executed = false;
    private RequestStatusType status = RequestStatusType.NEW;

    /**
     * Constructs a new CatalogueUpdateRequest with the initial submission details.
     * @param submissionDate The date the request was submitted.
     * @param updateDescription A detailed description of the requested changes.
     * @param product The category or type of product to be updated.
     * @param type The type of request (e.g., INSERT_PRODUCT, DELETE_PRODUCT, PROCESS_PRODUCT).
     * @param requestId The unique identifier for this request.
     */
    public CatalogueUpdateRequest(Date submissionDate, String updateDescription, ProductType product, AllowedRequest type, int requestId) {
        this.submissionDate = this.dateModified = submissionDate;
        this.updateDescription = updateDescription;
        this.product = product;
        this.type = type;
        this.requestId = requestId;
    }

    /**
     * @return the unique identifier in the system of the request.
     */
    public int getId() { return requestId; }

    /**
     * @return the current status of the request (e.g., NEW, ASSIGNED, SERVED).
     */
    public RequestStatusType getStatus() {
        return status;
    }

    /**
     * Updates the status of the request.
     * @param status the new status to set.
     */
    public void setStatus(RequestStatusType status) {
        this.status = status;
    }

    /**
     * @return the date the request was last modified.
     */
    public Date getDateModified() {
        return dateModified;
    }

    /**
     * @param newDateModified the new modification date to set.
     */
    public void setDateModified(Date newDateModified) { this.dateModified = newDateModified; }

    /**
     * @return the description of the update.
     */
    public String getUpdateDescription() {
        return updateDescription;
    }

    /**
     * @param newDescription the new description for the update.
     */
    public void setUpdateDescription(String newDescription) { this.updateDescription = newDescription; }

    /**
     * @return the product type associated with this request.
     */
    public ProductType getProduct() {
        return product;
    }

    /**
     * @param type the product type to set.
     */
    public void setProduct(ProductType type) { this.product = type; }

    /**
     * @return the type of the allowed request.
     */
    public AllowedRequest getType() {
        return type;
    }

    /**
     * @param requestType the specific type of request to set.
     */
    public void setType(AllowedRequest requestType) { this.type = requestType; }

    /**
     * Checks if the request has been finalized and executed.
     * @return true if executed, false otherwise.
     */
    public Boolean getExecuted() {
        return executed;
    }

    /**
     * Marks the request as executed or pending.
     * @param executed the execution status.
     */
    public void setExecuted(Boolean executed) {
        this.executed = executed;
    }

    /**
     * @return the immutable date of initial submission.
     */
    public Date getSubmissionDate() { return submissionDate; }
}