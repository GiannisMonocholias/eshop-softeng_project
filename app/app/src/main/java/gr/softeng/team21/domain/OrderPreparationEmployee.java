package gr.softeng.team21.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Random;

import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.OrderDAOMemory;
import gr.softeng.team21.memorydao.ProductsWareHouseDAOMemory;

public  class OrderPreparationEmployee extends Employee{
    private int totalOrdersPreparations;
    private int totalUpdateReserveRequests;
    private ProductsWareHouseDAOMemory wareHouse;
    private ArrayList<Order> assignedOrders;



    public OrderPreparationEmployee(String username, String firstname, String password, String lastname, String phoneNumber, EmailAddress emailaddress, String employeeId, int bonus, int salary, int workingHours, EmployeeState employeeState, Date hireDate) {
        super(username, firstname, password, lastname, phoneNumber, emailaddress, employeeId, bonus, salary, workingHours, employeeState, hireDate);
        this.totalOrdersPreparations = 0;
        this.totalUpdateReserveRequests = 0;
        this.assignedOrders = new ArrayList<Order>();
        this.wareHouse = ProductsWareHouseDAOMemory.getInstance();
    }

    public int getTotalOrdersPreparations() {
        return totalOrdersPreparations;
    }

    public int getTotalUpdateReserveRequests() {
        return totalUpdateReserveRequests;
    }


    public ArrayList<Order> getAssignedOrders() {
        return assignedOrders;
    }

    public ProductsWareHouseDAOMemory getWareHouse() {
        return wareHouse;
    }


    public Employee selectRandomEmployee(Class<? extends Employee> employeeClass){
        Random r = new Random();

        ArrayList<Employee> candidateEmployees = new ArrayList<>();

        HashMap<String,Employee> employees = EmployeeDAOMemory.getInstance().getEmployees();
        for(String id : employees.keySet()){
            if(employeeClass.isInstance(employees.get(id)))
                candidateEmployees.add(employees.get(id));
        }

        if (candidateEmployees.isEmpty()) {
            throw new IllegalStateException("No employees of type " + employeeClass.getSimpleName() + " found");
        }

        int position = r.nextInt(candidateEmployees.size());
        return candidateEmployees.get(position);

    }


    public void selectOrder(String orderId){
        if(!OrderDAOMemory.getInstance().getOrders().containsKey(orderId))
            throw new NoSuchElementException("The given orderId does not correspond to any submitted order");

        Order selectedOrder = OrderDAOMemory.getInstance().getOrder(orderId);
        assignedOrders.add(selectedOrder);
    }

    public void prepareOrder(Order order){
        if(order == null){
            throw new IllegalArgumentException("Order order argument in prepareOrder is null");
        }
        if(!assignedOrders.contains(order)){
            throw new NoSuchElementException("The given order is not in assigned orders");
        }


        HashMap<ProductType,Integer> insufficientstocks = new HashMap<>();

        for(CartItem item: order.getShoppingCart().getItems()){
            if(!wareHouse.sufficientStock(item.getProductType(),item.getQuantity())){
                insufficientstocks.put(item.getProductType(),item.getQuantity());
            }
        }

        if(insufficientstocks.isEmpty()){
            for(CartItem item: order.getShoppingCart().getItems()){
                wareHouse.decreaseProductStock(item.getProductType(), item.getQuantity());
            }
            order.setOrderstatus(StatusType.SHIPPED);


            Deliverer selectedDeliverer = (Deliverer) selectRandomEmployee(Deliverer.class);
            selectedDeliverer.addOrder(order);
            totalOrdersPreparations++;
        }

        else{

            String msg ="";
            for(ProductType type: insufficientstocks.keySet()){
                msg += type + "\n" + "  Needed quantity: " + insufficientstocks.get(type) +
                "\n  Available quantity: " + wareHouse.getProductStock(type);
            }

            totalUpdateReserveRequests++;


            sendEmail(this, Admin.getInstance(), "Inadequate stock for products", msg);

            CustomerServiceEmployee selectedCustomerServiceEmployee = (CustomerServiceEmployee)selectRandomEmployee(CustomerServiceEmployee.class);


            msg = "Please, inform customer about the expected delay of his order with id "+order.getOrdercode()+", due to products' stock shortage\n";
            sendEmail(this, selectedCustomerServiceEmployee, "Inadequate stock for products", msg);
            order.setOrderstatus(StatusType.DELAYED);
            selectedCustomerServiceEmployee.addOrder(order);
        }
    }
}