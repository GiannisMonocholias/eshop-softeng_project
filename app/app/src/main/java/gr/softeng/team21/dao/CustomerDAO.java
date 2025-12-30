package gr.softeng.team21.dao;

import java.util.HashMap;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.memorydao.CustomerDAOMemory;

public interface CustomerDAO {

     HashMap<String, Customer> getCustomers();

     Customer getCustomer(String id);

     void addCustomer(Customer customer);

     void removeCustomer(Customer customer);

}
