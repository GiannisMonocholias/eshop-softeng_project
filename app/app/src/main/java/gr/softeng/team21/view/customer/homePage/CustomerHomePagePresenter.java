package gr.softeng.team21.view.customer.homePage;

import gr.softeng.team21.domain.Customer;

public class CustomerHomePagePresenter {
private CustomerHomePageView view;
private Customer customer;
    public void setView(CustomerHomePageView view) {
        this.view = view;
    }

    public CustomerHomePagePresenter(CustomerHomePageView view, Customer customer) {
        this.view = view;
        this.customer = customer;
    }

    public void EditDataClicked() {
        view.goToEditData();
    }

    public void LogoutClicked() {
        view.goToLogin();

    }

    public void FindProductClicked() {
        view.goToFindProduct();
    }

    public void DeleteClicked() {
        view.showDeleteConfirmation();
    }

    public void DeleteConfirm() {
        if(customer!=null){
            customer.remove();
            view.showMessage("Ο λογαριασμός σας διαγράφηκε.");
            view.goToLogin();
        }
    }

    public void InboxClicked() {
        view.goToInbox();
    }
}
