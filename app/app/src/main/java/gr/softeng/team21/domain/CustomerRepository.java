package gr.softeng.team21.domain;


import java.util.HashMap;

public class CustomerRepository {

    private HashMap<String, Customer> customers;
    private static CustomerRepository instance;

    private CustomerRepository() {
        customers = new HashMap<>();
    }

    public static  CustomerRepository getInstance() {
        if (instance == null) {
            instance = new CustomerRepository();
        }
        return instance;
    }

    public HashMap<String, Customer> getCustomers() {
        return customers;
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

}
