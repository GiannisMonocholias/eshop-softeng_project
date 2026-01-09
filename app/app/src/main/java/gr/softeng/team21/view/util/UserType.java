package gr.softeng.team21.view.util;

/**
 * Defines all possible user roles within the system.
 * This enumeration is used during the authentication process to route users
 * to their respective home dashboards and to handle role-specific data passing.
 * @author Γιάννης Μονοχολιάς
 */
public enum UserType {

    ADMIN,
    CUSTOMER,
    CUSTOMER_SERVICE_EMPLOYEE,
    DELIVERER,
    ORDER_PREPARATION_EMPLOYEE,
    UPDATE_CATALOGUE_EMPLOYEE
}