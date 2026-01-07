package gr.softeng.team21.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Random;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.OrderDAOMemory;
import gr.softeng.team21.memorydao.ProductsWareHouseDAOMemory;
import gr.softeng.team21.util.Date;

/**
 * Represents an employee responsible for preparing orders and managing warehouse stock levels.
 * This class handles order selection, stock validation, and the coordination between
 * deliverers and customer service in case of stock shortages.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderPreparationEmployee extends Employee {
    private int totalOrdersPreparations;
    private int totalUpdateReserveRequests;
    private ProductsWareHouseDAOMemory wareHouse;
    private ArrayList<Order> assignedOrders;

    /**
     * Constructs a new OrderPreparationEmployee with the specified details.
     * Initializes the warehouse reference and assigned orders list.
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
    public OrderPreparationEmployee(String username, String firstname, String password, String lastname, String phoneNumber, EmailAddress emailaddress, String employeeId, int bonus, int salary, int workingHours, EmployeeState employeeState, Date hireDate) {
        super(username, firstname, password, lastname, phoneNumber, emailaddress, employeeId, bonus, salary, workingHours, employeeState, hireDate);
        this.totalOrdersPreparations = 0;
        this.totalUpdateReserveRequests = 0;
        this.assignedOrders = new ArrayList<Order>();
        this.wareHouse = ProductsWareHouseDAOMemory.getInstance();
    }

    /**
     * Manually adds an order to the employee's assignment list.
     * @param order the order to assign.
     */
    public void addOrder(Order order) { assignedOrders.add(order); }

    /**
     * Removes an order from the employee's assignment list.
     * @param order the order to remove.
     */
    public void removeOrder(Order order) { assignedOrders.remove(order); }

    /**
     * @return the total number of orders fully prepared by this employee.
     */
    public int getTotalOrdersPreparations() {
        return totalOrdersPreparations;
    }

    /**
     * @return the total number of stock replenishment requests sent to administration.
     */
    public int getTotalUpdateReserveRequests() {
        return totalUpdateReserveRequests;
    }

    /**
     * @return the list of orders currently assigned to this employee for preparation.
     */
    public ArrayList<Order> getAssignedOrders() {
        return assignedOrders;
    }

    /**
     * @return the warehouse data access object associated with this employee.
     */
    public ProductsWareHouseDAOMemory getWareHouse() {
        return wareHouse;
    }

    /**
     * Selects a random employee of a specific type from the system.
     * @param employeeClass the class type of the employee to select (e.g., Deliverer.class).
     * @return a randomly selected employee instance of the specified type.
     * @throws IllegalStateException if no employees of the requested type are found.
     */
    public Employee selectRandomEmployee(Class<? extends Employee> employeeClass) {
        Random r = new Random();
        ArrayList<Employee> candidateEmployees = new ArrayList<>();
        HashMap<String, Employee> employees = EmployeeDAOMemory.getInstance().getEmployees();

        for (String id : employees.keySet()) {
            if (employeeClass.isInstance(employees.get(id)))
                candidateEmployees.add(employees.get(id));
        }

        if (candidateEmployees.isEmpty()) {
            throw new IllegalStateException("No employees of type " + employeeClass.getSimpleName() + " found");
        }

        int position = r.nextInt(candidateEmployees.size());
        return candidateEmployees.get(position);
    }

    /**
     * Picks an order from the global submitted orders and assigns it to this employee.
     * @param orderId the unique ID of the order to select.
     * @throws NoSuchElementException if the order ID does not exist in the system.
     */
    public void selectOrder(String orderId) {
        if (!OrderDAOMemory.getInstance().getOrders().containsKey(orderId))
            throw new NoSuchElementException("The given orderId does not correspond to any submitted order");

        Order selectedOrder = OrderDAOMemory.getInstance().getOrder(orderId);
        assignedOrders.add(selectedOrder);
    }

    /**
     * Processes the preparation of an order.
     * If stock is sufficient, it updates warehouse levels and assigns a deliverer.
     * If stock is insufficient, it triggers notifications to Admin and Customer Service.
     * @param order the order to be prepared.
     * @throws IllegalArgumentException if the order argument is null.
     * @throws NoSuchElementException if the order is not found in the employee's assigned list.
     */
    public void prepareOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order order argument in prepareOrder is null");
        }
        if (!assignedOrders.contains(order)) {
            throw new NoSuchElementException("The given order is not in assigned orders");
        }

        HashMap<ProductType, Integer> insufficientstocks = new HashMap<>();

        for (CartItem item : order.getShoppingCart().getItems()) {
            if (!wareHouse.sufficientStock(item.getProductType(), item.getQuantity())) {
                insufficientstocks.put(item.getProductType(), item.getQuantity());
            }
        }

        if (insufficientstocks.isEmpty()) {
            for (CartItem item : order.getShoppingCart().getItems()) {
                wareHouse.decreaseProductStock(item.getProductType(), item.getQuantity());
            }
            order.setOrderstatus(OrderStatusType.SHIPPED);

            Deliverer selectedDeliverer = (Deliverer) selectRandomEmployee(Deliverer.class);
            selectedDeliverer.addOrder(order);
            totalOrdersPreparations++;
        } else {
            String msg = "";
            for (ProductType type : insufficientstocks.keySet()) {
                msg += type + "\n" + "  Needed quantity: " + insufficientstocks.get(type) +
                        "\n  Available quantity: " + wareHouse.getProductStock(type) + "\n";
            }

            totalUpdateReserveRequests++;
            sendEmail(this, Admin.getInstance(), "Inadequate stock for products", msg, new Date());

            CustomerServiceEmployee selectedCustomerServiceEmployee = (CustomerServiceEmployee) selectRandomEmployee(CustomerServiceEmployee.class);
            msg = "Please, inform customer about the expected delay of his order with id " + order.getOrdercode() + ", due to products' stock shortage\n";
            sendEmail(this, selectedCustomerServiceEmployee, "Inadequate stock for products", msg, new Date());

            order.setOrderstatus(OrderStatusType.DELAYED);
            selectedCustomerServiceEmployee.addOrder(order);
        }
    }
}