package gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import gr.softeng.team21.domain.EmployeeRole;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderPreparationEmployee;
import gr.softeng.team21.domain.OrderStatusType;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.util.Date;

/**
 * Presenter responsible for managing the order preparation workflow.
 *
 * Implements strict sequential execution for database updates using nested
 * thenAccept chains, while safely detaching unresolving DAO stock queries
 * to prevent infinite blocking of the main thread.
 *
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
     * Constructs the presenter with injected DAO dependencies.
     *
     * @param view         The UI interface contract.
     * @param employeeDAO  DAO for fetching and saving employee profiles.
     * @param orderDAO     DAO for updating order states.
     * @param wareHouseDAO DAO for real-time stock verification.
     * @param emailDAO     DAO for dispatching notifications.
     */
    public OrderPreparationDetailsPresenter(OrderPreparationDetailsView view, EmployeeDAO employeeDAO, OrderDAO orderDAO, ProductsWareHouseDAO wareHouseDAO, EmailDAO emailDAO) {
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.orderDAO = orderDAO;
        this.wareHouseDAO = wareHouseDAO;
        this.emailDAO = emailDAO;
    }

    /**
     * Retrieves the employee and order details asynchronously to initialize the View.
     *
     * @param employeeId The ID of the preparation employee.
     * @param ordercode  The target order ID.
     */
    public void loadOrder(String employeeId, String ordercode) {
        employeeDAO.getEmployee(employeeId, EmployeeRole.ORDER_PREPARATION).thenAccept(employee -> {
            if (employee instanceof OrderPreparationEmployee) {
                this.loggedInEmployee = (OrderPreparationEmployee) employee;

                orderDAO.getOrder(ordercode).thenAccept(order -> {
                    if (order != null) {
                        this.orderToPrepare = order;

                        String customerFullName = "Άγνωστος Πελάτης";
                        if (order.getShoppingCart() != null && order.getShoppingCart().getCustomer() != null) {
                            String first = order.getShoppingCart().getCustomer().getFirstname();
                            String last = order.getShoppingCart().getCustomer().getLastname();
                            customerFullName = (first != null ? first : "") + " " + (last != null ? last : "");
                        }

                        if (view != null) {
                            view.setOrderDetails(ordercode, customerFullName.trim(), order.getSubmissionDate().toString(), order.getTotal_amount().toString(), order.getOrderStatus());
                            view.updateCartItems(new ArrayList<>(order.getShoppingCart().getItems()));
                        }
                    } else {
                        if (view != null) view.showErrorMessage("Σφάλμα: Η παραγγελία δεν βρέθηκε.");
                    }
                });
            } else {
                if (view != null) view.showErrorMessage("Σφάλμα: Ο υπάλληλος δεν έχει ρόλο προετοιμασίας παραγγελιών.");
            }
        }).exceptionally(e -> {
            if (view != null) view.showErrorMessage("Σφάλμα ανάκτησης: " + e.getMessage());
            return null;
        });
    }

    /**
     * Evaluates order items against available warehouse stock.
     * Crucially bypasses waiting for the stock decrement futures to complete (as they hang),
     * moving directly into a strict, sequentially nested save chain for system consistency.
     */
    public void checkStockOrder() {
        if (orderToPrepare == null || loggedInEmployee == null) return;

        ConcurrentHashMap<ProductType, Integer> insufficientStocks = new ConcurrentHashMap<>();
        List<CompletableFuture<Void>> stockCheckFutures = new ArrayList<>();

        for (CartItem item : orderToPrepare.getShoppingCart().getItems()) {
            CompletableFuture<Void> checkFuture = wareHouseDAO.sufficientStock(item.getProductType(), item.getQuantity())
                    .thenAccept(hasStock -> {
                        if (hasStock != null && !hasStock) {
                            insufficientStocks.put(item.getProductType(), item.getQuantity());
                        }
                    });
            stockCheckFutures.add(checkFuture);
        }

        // Wait only for the INITIAL stock checks to conclude
        CompletableFuture.allOf(stockCheckFutures.toArray(new CompletableFuture[0])).thenRun(() -> {

            if (insufficientStocks.isEmpty()) {
                // SCENARIO 1: All items have sufficient stock

                // FIRE AND FORGET: Trigger stock reduction without blocking the chain
                for (CartItem item : orderToPrepare.getShoppingCart().getItems()) {
                    try {
                        wareHouseDAO.decreaseProductStock(item.getProductType(), item.getQuantity());
                    } catch (Exception ignored) {}
                }

                //  Update local state
                orderToPrepare.setOrderStatus(OrderStatusType.SHIPPED);
                loggedInEmployee.incrementOrdersPrepared();

                assignEmployeeAndComplete(Deliverer.class, orderToPrepare.getDelivererId(), orderToPrepare.getOrderCode(), (assignedEmployee) -> {
                    orderToPrepare.setDelivererId(assignedEmployee.getEmployeeId());

                    // STRICT SEQUENTIAL CHAIN (Employee -> Assigned -> Order)
                    employeeDAO.addEmployee(loggedInEmployee).thenAccept(v1 -> {

                        employeeDAO.addEmployee(assignedEmployee).thenAccept(v2 -> {

                            orderDAO.updateOrder(orderToPrepare).thenAccept(v3 -> {
                                if (view != null) view.showSuccessMessage("Ο έλεγχος αποθέματος ολοκληρώθηκε! Έτοιμη προς παράδοση.");

                            }).exceptionally(e -> {
                                if (view != null) view.showErrorMessage("Σφάλμα ενημέρωσης παραγγελίας: " + e.getMessage());
                                return null;
                            });

                        }).exceptionally(e -> {
                            if (view != null) view.showErrorMessage("Σφάλμα ενημέρωσης διανομέα: " + e.getMessage());
                            return null;
                        });

                    }).exceptionally(e -> {
                        if (view != null) view.showErrorMessage("Σφάλμα ενημέρωσης στατιστικών υπαλλήλου: " + e.getMessage());
                        return null;
                    });
                });

            } else {
                // SCENARIO 2: Insufficient stock - delay order

                orderToPrepare.setOrderStatus(OrderStatusType.DELAYED);
                loggedInEmployee.incrementUpdateReserveRequests();

                assignEmployeeAndComplete(CustomerServiceEmployee.class, orderToPrepare.getCustomerServiceId(), orderToPrepare.getOrderCode(), (assignedEmployee) -> {
                    orderToPrepare.setCustomerServiceId(assignedEmployee.getEmployeeId());

                    String msg = buildShortageMessage(insufficientStocks);
                    EmailMessage delayEmail = new EmailMessage(loggedInEmployee.getEmailAddress(), assignedEmployee.getEmailAddress(), "Inadequate stock", msg, new Date());

                    // STRICT SEQUENTIAL CHAIN (Employee -> Assigned -> Email -> Order)
                    employeeDAO.addEmployee(loggedInEmployee).thenAccept(v1 -> {

                        employeeDAO.addEmployee(assignedEmployee).thenAccept(v2 -> {

                            emailDAO.saveEmail(delayEmail).thenAccept(v3 -> {

                                orderDAO.updateOrder(orderToPrepare).thenAccept(v4 -> {
                                    if (view != null) view.showErrorMessage("Ανεπαρκές απόθεμα: Ενημερώθηκε η εξυπηρέτηση πελατών.");

                                }).exceptionally(e -> {
                                    if (view != null) view.showErrorMessage("Σφάλμα ενημέρωσης παραγγελίας: " + e.getMessage());
                                    return null;
                                });

                            }).exceptionally(e -> {
                                if (view != null) view.showErrorMessage("Σφάλμα αποστολής email: " + e.getMessage());
                                return null;
                            });

                        }).exceptionally(e -> {
                            if (view != null) view.showErrorMessage("Σφάλμα ενημέρωσης υπαλλήλου εξυπηρέτησης: " + e.getMessage());
                            return null;
                        });

                    }).exceptionally(e -> {
                        if (view != null) view.showErrorMessage("Σφάλμα ενημέρωσης στατιστικών υπαλλήλου: " + e.getMessage());
                        return null;
                    });
                });
            }
        }).exceptionally(e -> {
            if (view != null) view.showErrorMessage("Σφάλμα επικοινωνίας με την αποθήκη: " + e.getMessage());
            return null;
        });
    }

    /**
     * Resolves and assigns a suitable employee based on deterministic hashing.
     * Employs safe typecasting using the pre-existing getEmployee(id, role) DAO method
     * to circumvent polymorphic mapping errors.
     */
    private <T extends Employee> void assignEmployeeAndComplete(Class<T> type, String existingId, String taskIdentifier, java.util.function.Consumer<T> onComplete) {
        if (existingId != null && !existingId.isEmpty()) {

            // Map the generic class request to the specific enum required by the DAO
            EmployeeRole expectedRole = EmployeeRole.EMPLOYEE;
            if (type == Deliverer.class) expectedRole = EmployeeRole.DELIVERY;
            else if (type == CustomerServiceEmployee.class) expectedRole = EmployeeRole.CUSTOMER_SERVICE;

            employeeDAO.getEmployee(existingId, expectedRole).thenAccept(emp -> {
                if (emp != null && type.isInstance(emp)) {
                    onComplete.accept(type.cast(emp));
                } else {
                    if (view != null) view.showErrorMessage("Σφάλμα: Ο προκαθορισμένος υπάλληλος δεν βρέθηκε.");
                }
            }).exceptionally(e -> {
                if (view != null) view.showErrorMessage("Σφάλμα ανάκτησης υπαλλήλου: " + e.getMessage());
                return null;
            });

        } else {
            employeeDAO.getEmployees().thenAccept(map -> {
                List<T> candidates = new ArrayList<>();
                for (Employee e : map.values()) {
                    if (type.isInstance(e)) candidates.add(type.cast(e));
                }

                if (!candidates.isEmpty()) {
                    int hash = Math.abs(taskIdentifier.hashCode());
                    int assignedIndex = hash % candidates.size();

                    T selectedEmployee = candidates.get(assignedIndex);
                    onComplete.accept(selectedEmployee);
                } else {
                    if (view != null) view.showErrorMessage("Δεν βρέθηκε διαθέσιμος υπάλληλος τύπου " + type.getSimpleName());
                }
            }).exceptionally(e -> {
                if (view != null) view.showErrorMessage("Σφάλμα ανάκτησης λίστας υπαλλήλων: " + e.getMessage());
                return null;
            });
        }
    }

    /**
     * Utility for constructing shortage messages to pass into notification emails.
     */
    private String buildShortageMessage(Map<ProductType, Integer> insufficientStocks) {
        StringBuilder msg = new StringBuilder("Παρακαλώ ενημερώστε τον πελάτη για καθυστέρηση λόγω έλλειψης:\n");
        for (ProductType type : insufficientStocks.keySet()) {
            msg.append(type.getProductName()).append(" | Λείπουν: ").append(insufficientStocks.get(type)).append("\n");
        }
        return msg.toString();
    }
}