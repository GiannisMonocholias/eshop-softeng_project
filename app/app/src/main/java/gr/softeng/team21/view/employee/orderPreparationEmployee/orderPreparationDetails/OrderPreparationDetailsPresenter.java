package gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import gr.softeng.team21.contact.EmailMessage;
import gr.softeng.team21.dao.EmailDAO;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.dao.ProductsWareHouseDAO;
import gr.softeng.team21.domain.CartItem;
import gr.softeng.team21.domain.CustomerServiceEmployee;
import gr.softeng.team21.domain.Deliverer;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderPreparationEmployee;
import gr.softeng.team21.domain.OrderStatusType;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.util.Date;

/**
 * Presenter for managing order preparation.
 * Handles fully asynchronous stock verification for multiple items, dynamic employee
 * assignment, order status updates, and dispatching notification emails via DAOs.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderPreparationDetailsPresenter {
    private final OrderPreparationDetailsView view;
    private final EmployeeDAO employeeDAO;
    private final OrderDAO orderDAO;
    private final ProductsWareHouseDAO wareHouseDAO;
    private final EmailDAO emailDAO;

    private OrderPreparationEmployee loggedInEmployee;
    private Order orderToPrepare;

    /**
     * Initializes the presenter with required DAOs.
     */
    public OrderPreparationDetailsPresenter(OrderPreparationDetailsView view, EmployeeDAO employeeDAO, OrderDAO orderDAO, ProductsWareHouseDAO wareHouseDAO, EmailDAO emailDAO) {
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.orderDAO = orderDAO;
        this.wareHouseDAO = wareHouseDAO;
        this.emailDAO = emailDAO;
    }

    /**
     * Asynchronously loads the employee and order data, preparing the view for display.
     */
    public void loadOrder(String employeeId, String ordercode) {
        employeeDAO.getEmployee(employeeId).thenAccept(employee -> {
            if (employee instanceof OrderPreparationEmployee) {
                this.loggedInEmployee = (OrderPreparationEmployee) employee;
                orderDAO.getOrder(ordercode).thenAccept(order -> {
                    if (order != null) {
                        this.orderToPrepare = order;
                        String customerFullName = order.getShoppingCart().getCustomer().getFirstname() + " " + order.getShoppingCart().getCustomer().getLastname();
                        if (view != null) {
                            view.setOrderDetails(ordercode, customerFullName, order.getSubmissiondate().toString(), order.getTotal_amount().toString(), order.getOrderstatus());
                            view.updateCartItems(new ArrayList<>(order.getShoppingCart().getItems()));
                        }
                    } else {
                        if (view != null) view.showErrorMessage("Σφάλμα: Η παραγγελία δεν βρέθηκε.");
                    }
                });
            } else {
                if (view != null) view.showErrorMessage("Σφάλμα: Ο υπάλληλος δεν βρέθηκε.");
            }
        }).exceptionally(e -> {
            if (view != null) view.showErrorMessage("Σφάλμα ανάκτησης: " + e.getMessage());
            return null;
        });
    }

    /**
     * Orchestrates the stock check asynchronously for all items.
     * Decreases stock and assigns a deliverer if sufficient, or triggers delay emails
     * and customer service assignment if insufficient.
     */
    public void checkStockOrder() {
        if (orderToPrepare == null || loggedInEmployee == null) return;

        // Thread-safe map to aggregate results from multiple concurrent async calls
        ConcurrentHashMap<ProductType, Integer> insufficientStocks = new ConcurrentHashMap<>();
        List<CompletableFuture<Void>> stockCheckFutures = new ArrayList<>();

        // Create an async task for each item in the cart
        for (CartItem item : orderToPrepare.getShoppingCart().getItems()) {
            CompletableFuture<Void> checkFuture = wareHouseDAO.sufficientStock(item.getProductType(), item.getQuantity())
                    .thenAccept(hasStock -> {
                        if (hasStock != null && !hasStock) {
                            insufficientStocks.put(item.getProductType(), item.getQuantity());
                        }
                    });
            stockCheckFutures.add(checkFuture);
        }

        // Wait for ALL stock checks to finish before deciding the next step
        CompletableFuture.allOf(stockCheckFutures.toArray(new CompletableFuture[0])).thenRun(() -> {

            if (insufficientStocks.isEmpty()) {
                // STOCK OK -> Decrease stock asynchronously for all items
                List<CompletableFuture<Boolean>> decreaseFutures = new ArrayList<>();
                for (CartItem item : orderToPrepare.getShoppingCart().getItems()) {
                    decreaseFutures.add(wareHouseDAO.decreaseProductStock(item.getProductType(), item.getQuantity()));
                }

                // When stock is decreased, assign Deliverer and complete
                CompletableFuture.allOf(decreaseFutures.toArray(new CompletableFuture[0])).thenRun(() -> {
                    orderToPrepare.setOrderstatus(OrderStatusType.SHIPPED);
                    loggedInEmployee.incrementOrdersPrepared();

                    assignEmployeeAndComplete(Deliverer.class, orderToPrepare.getDelivererId(), (assignedEmployee) -> {
                        orderToPrepare.setDelivererId(assignedEmployee.getEmployeeId());
                        saveOrderAndNotifyView("Ο έλεγχος αποθέματος ολοκληρώθηκε! Έτοιμη προς παράδοση.");
                    });
                }).exceptionally(e -> {
                    if (view != null) view.showErrorMessage("Σφάλμα κατά τη μείωση αποθέματος: " + e.getMessage());
                    return null;
                });

            } else {
                // STOCK MISSING -> Assign Customer Service & Delay
                orderToPrepare.setOrderstatus(OrderStatusType.DELAYED);
                loggedInEmployee.incrementUpdateReserveRequests();

                assignEmployeeAndComplete(CustomerServiceEmployee.class, orderToPrepare.getCustomerServiceId(), (assignedEmployee) -> {
                    orderToPrepare.setCustomerServiceId(assignedEmployee.getEmployeeId());

                    String msg = buildShortageMessage(insufficientStocks);
                    EmailMessage delayEmail = new EmailMessage(loggedInEmployee.getEmailAddress(), assignedEmployee.getEmailAddress(), "Inadequate stock", msg, new Date());

                    CompletableFuture.allOf(emailDAO.saveSentEmails(delayEmail), emailDAO.saveInboxEmails(delayEmail))
                            .thenRun(() -> saveOrderAndNotifyView("Ανεπαρκές απόθεμα: Ενημερώθηκε η εξυπηρέτηση πελατών."))
                            .exceptionally(e -> {
                                if (view != null) view.showErrorMessage("Σφάλμα αποστολής email: " + e.getMessage());
                                return null;
                            });
                });
            }
        }).exceptionally(e -> {
            if (view != null) view.showErrorMessage("Σφάλμα ελέγχου αποθέματος: " + e.getMessage());
            return null;
        });
    }

    /**
     * Resolves the assigned employee. If an ID exists, it fetches them via getEmployee(id).
     * If null, it fetches all employees, filters by class, and picks a random one.
     */
    private <T extends Employee> void assignEmployeeAndComplete(Class<T> type, String existingId, java.util.function.Consumer<T> onComplete) {
        if (existingId != null && !existingId.isEmpty()) {
            employeeDAO.getEmployee(existingId).thenAccept(emp -> onComplete.accept(type.cast(emp)));
        } else {
            employeeDAO.getEmployees().thenAccept(map -> {
                List<T> candidates = new ArrayList<>();
                for (Employee e : map.values()) {
                    if (type.isInstance(e)) candidates.add(type.cast(e));
                }
                if (!candidates.isEmpty()) {
                    T randomEmployee = candidates.get(new Random().nextInt(candidates.size()));
                    onComplete.accept(randomEmployee);
                } else {
                    if (view != null) view.showErrorMessage("Δεν βρέθηκε διαθέσιμος υπάλληλος τύπου " + type.getSimpleName());
                }
            });
        }
    }

    private void saveOrderAndNotifyView(String message) {
        orderDAO.updateOrder(orderToPrepare).thenAccept(v -> {
            if (view != null) {
                if (orderToPrepare.getOrderstatus() == OrderStatusType.DELAYED) view.showErrorMessage(message);
                else view.showSuccessMessage(message);
            }
        }).exceptionally(e -> {
            if (view != null) view.showErrorMessage("Σφάλμα αποθήκευσης παραγγελίας: " + e.getMessage());
            return null;
        });
    }

    private String buildShortageMessage(Map<ProductType, Integer> insufficientStocks) {
        StringBuilder msg = new StringBuilder("Παρακαλώ ενημερώστε τον πελάτη για καθυστέρηση λόγω έλλειψης:\n");
        for (ProductType type : insufficientStocks.keySet()) {
            msg.append(type.getProductname()).append(" | Λείπουν: ").append(insufficientStocks.get(type)).append("\n");
        }
        return msg.toString();
    }
}