package gr.softeng.team21.view.customer.homePage;

import gr.softeng.team21.dao.UserCredentialsDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;

public class CustomerHomePagePresenter {
private CustomerHomePageView view;
private Customer customer;


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
        UserCredentialsDAOMemory userrepo = UserCredentialsDAOMemory.getInstance();
        if(customer!=null){
            userrepo.removeUser(customer.getUsername());
            customer.remove();
            view.showMessage("Ο λογαριασμός σας διαγράφηκε.");
            view.goToLogin();
        }
    }

    public void InboxClicked() {
        view.goToInbox();
    }
}
