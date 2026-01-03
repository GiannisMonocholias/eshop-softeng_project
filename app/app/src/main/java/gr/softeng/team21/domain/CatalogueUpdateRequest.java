package gr.softeng.team21.domain;

public class CatalogueUpdateRequest{
    private  int requestId;
    private final Date submissionDate;
    private Date dateModified;
    private  AllowedRequest type;
    private  ProductType product;
    private String updateDescription;
    private boolean executed=false;

    public CatalogueUpdateRequest(Date submissionDate, String updateDescription, ProductType product, AllowedRequest type, int requestId) {
        this.submissionDate = this.dateModified = submissionDate;
        this.updateDescription = updateDescription;
        this.product = product;
        this.type = type;
        this.requestId = requestId;
    }


    public  int getId(){return requestId;}

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date newDateModified){this.dateModified = newDateModified;}

    public String getUpdateDescription() {
        return updateDescription;
    }

    public void setUpdateDescription(String newDescription){this.updateDescription = newDescription;}

    public ProductType getProduct() {
        return product;
    }

    public void setProduct(ProductType type){this.product = type;}

    public AllowedRequest getType() {
        return type;
    }

    public void setType(AllowedRequest requestType){this.type = requestType;}

    public Boolean getExecuted() {
        return executed;
    }

    public void setExecuted(Boolean executed) {
        this.executed = executed;
    }
    public Date getSubmissionDate() {return submissionDate;}

}