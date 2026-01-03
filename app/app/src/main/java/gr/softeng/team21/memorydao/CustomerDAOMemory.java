package gr.softeng.team21.memorydao;


import java.util.HashMap;

import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.Employee;

public class CustomerDAOMemory implements CustomerDAO {

    private static HashMap<String, Customer> customers;
    private static CustomerDAOMemory instance;

    private CustomerDAOMemory() {
        customers = new HashMap<>();
    }

    public static CustomerDAOMemory getInstance() {
        if (instance == null) {
            instance = new CustomerDAOMemory();
        }
        return instance;
    }

    public HashMap<String, Customer> getCustomers() {
        return customers;
    }

    public Customer getCustomerByEmail(String emailAddress){
        for(String id: customers.keySet()){
            if(customers.get(id).getEmailAddress().toString().equals(emailAddress)){
                return customers.get(id);
            }
        }
        return null;
    }

    public Customer getCustomer(String id){
        return customers.get(id);
    }

    public void addCustomer(Customer customer) {
        if (customer != null) {
            String id = customer.getCustomer_id();
            if (!customers.containsKey(id)) {
                customers.put(id, customer);
            } else {
                throw new IllegalArgumentException("Customer with  this id already exists.");
            }
        } else {
            throw new IllegalArgumentException("Cannot add null customer.");
        }
    }

    public void removeCustomer(Customer customer) {
        if (customer != null) {
            String id = customer.getCustomer_id();
            if (customers.containsKey(id)) {
                customers.remove(id);
            } else {
                throw new IllegalArgumentException("Customer with this id does not exist.");
            }
        } else {
            throw new IllegalArgumentException("Cannot remove null customer.");
        }
    }


    public void clear(){
        customers.clear();;
    }
}
