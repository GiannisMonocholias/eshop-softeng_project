package gr.softeng.team21.memorydao;

import java.math.BigDecimal;
import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.dao.ProductTypeDAO;
import gr.softeng.team21.dao.ProductsWareHouseDAO;
import gr.softeng.team21.dao.UpdateRequestDAO;
import gr.softeng.team21.dao.UserCredentialsDAO;
import gr.softeng.team21.domain.Admin;
import gr.softeng.team21.util.Money;
import gr.softeng.team21.util.Date;
import gr.softeng.team21.contact.Address;
import gr.softeng.team21.domain.AllowedRequest;
import gr.softeng.team21.domain.AuthenticationSystem;
import gr.softeng.team21.domain.CartItem;
import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.CustomerServiceEmployee;
import gr.softeng.team21.domain.Deliverer;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.domain.EmployeeState;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderPreparationEmployee;
import gr.softeng.team21.domain.PaymentType;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.domain.ShoppingCart;
import gr.softeng.team21.domain.OrderStatusType;
import gr.softeng.team21.domain.UpdateCatalogueEmployee;

public class MemoryInitializer {
    public static void eraseData() {
        try {
            getProductsWareHouseDAO().clear();
            if (!getProductsWareHouseDAO().getProductStocks().isEmpty()) {
                throw new IllegalStateException("Products warehouse was not cleared");
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        // CustomerDAO erase data
        try {
            getCustomerDAO().clear();
            if (!getCustomerDAO().getCustomers().isEmpty()) {
                throw new IllegalStateException("Customers repository was not cleared");
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        // EmployeeDAO erase data
        try {
            getEmployeeDAO().clear();
            if (!getEmployeeDAO().getEmployees().isEmpty()) {
                throw new IllegalStateException("Employee repository was not cleared");
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        // ProductTypeDAO erase data
        try {
            getProductTypeDAO().clear();
            if (!getProductTypeDAO().getProducts().isEmpty()) {
                throw new IllegalStateException("Product types repository was not cleared");
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        // OrderDAO erase data
        try {
            getOrderDAO().clear();
            if (!getOrderDAO().getOrders().isEmpty()) {
                throw new IllegalStateException("Orders repository was not cleared");
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        // UpdateRequestDAO erase data
        try {
            getUpdateRequestDAO().clear();
            if (!getUpdateRequestDAO().getUpdateRequests().isEmpty()) {
                throw new IllegalStateException("Update requests repository was not cleared");
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        // UserCredentialsDAO erase data
        try {
            getUserCredentialsDAO().clear();
            if (!getUserCredentialsDAO().getUsersCredentials().isEmpty()) {
                throw new IllegalStateException("Users credentials repository was not cleared");
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public static void prepareData() {

        eraseData();
        //=====================================================
        //START: INITIALIZE CUSTOMERS
        //=====================================================
        CustomerDAO customers = CustomerDAOMemory.getInstance();

        Address addr1 = new Address("Ερμού", "15", "Αθήνα", "Ελλάδα", "10563");
        Address addr2 = new Address("Τσιμισκή", "42", "Θεσσαλονίκη", "Ελλάδα", "54623");
        Address addr3 = new Address("Μαιζώνος", "120", "Πάτρα", "Ελλάδα", "26221");
        Address addr4 = new Address("Κύπρου", "8", "Λάρισα", "Ελλάδα", "41221");


        Customer cust1 = new Customer("nickgeorg", "Νίκος", "pass1234", "Γεωργίου",
                "6987654321", new EmailAddress("nickgeorg@team21.gr"), "CUST-500", new Date(10, 5, 2022));
        cust1.setAddress(addr1);
        customers.addCustomer(cust1);
        getUserCredentialsDAO().addUser(cust1);

        Customer cust2 = new Customer("georgepap", "Γιώργος", "pass1235", "Παπαδόπουλος",
                "6987659483", new EmailAddress("georgepap@team21.gr"), "CUST-501", new Date(19, 3, 2021));
        cust2.setAddress(addr2);
        customers.addCustomer(cust2);
        getUserCredentialsDAO().addUser(cust2);

        Customer cust3 = new Customer("giannismonoh", "Γιάννης", "pass1236", "Μονοχολιάς",
                "6987651456", new EmailAddress("giannismonoh@team21.gr"), "CUST-502", new Date(22, 6, 2021));
        cust3.setAddress(addr3);
        customers.addCustomer(cust3);
        getUserCredentialsDAO().addUser(cust3);

        Customer cust4 = new Customer(
                "giannis_oik", "Γιάννης", "pass3210", "Οικονόμου", "6944556677",
                new EmailAddress("giannis.oik@team21.gr"), "CUST-503",new Date(22, 3, 2024)
        );
        cust4.setAddress(addr4);
        customers.addCustomer(cust4);
        getUserCredentialsDAO().addUser(cust4);
        //=====================================================
        //END: INITIALIZE CUSTOMERS
        //=====================================================


        //=====================================================
        //START: INITIALIZE EMPLOYEES
        //=====================================================
        EmployeeDAO employees = (EmployeeDAO) EmployeeDAOMemory.getInstance();

        //=====================================================
        //CUSTOMER SERVICE EMPLOYEES INITIALIZATION
        //=====================================================
        EmailAddress emailCs1 = new EmailAddress("m.alexandrou@team21.gr");
        CustomerServiceEmployee csr1 = new CustomerServiceEmployee(
                "m_alexandrou", "Μαρία", "pass1237", "Αλεξάνδρου",
                "6971234567", emailCs1, "CSR-101", 200, 1100, 40, EmployeeState.ACTIVE, new Date(19, 1, 2018)
        );
        employees.addEmployee(csr1);
        getUserCredentialsDAO().addUser(csr1);

        EmailAddress emailCs2 = new EmailAddress("p.dimitriou@team21.gr");
        CustomerServiceEmployee csr2 = new CustomerServiceEmployee(
                "p_dimitriou", "Πέτρος", "pass1238", "Δημητρίου",
                "6972345678", emailCs2, "CSR-102", 180, 1050, 40, EmployeeState.ACTIVE, new Date(15, 2, 2024)
        );
        employees.addEmployee(csr2);
        getUserCredentialsDAO().addUser(csr2);


        EmailAddress emailCs3 = new EmailAddress("s.konst@team21.gr");
        CustomerServiceEmployee csr3 = new CustomerServiceEmployee(
                "s_konstantinou", "Σοφία", "pass1239", "Κωνσταντίνου",
                "6973456789", emailCs3, "CSR-103", 220, 1150, 40, EmployeeState.ACTIVE, new Date(13, 9, 2019)
        );
        employees.addEmployee(csr3);
        getUserCredentialsDAO().addUser(csr3);


        //=====================================================
        //ORDER PREPARATION EMPLOYEES INITIALIZATION
        //=====================================================
        EmailAddress emailPrep1 = new EmailAddress("g.nikolaou@team21.gr");
        OrderPreparationEmployee prep1 = new OrderPreparationEmployee(
                "g_nikolaou", "Γιώργος", "pass1240", "Νικολάου",
                "6981122334", emailPrep1, "PREP-201", 100, 1200, 40, EmployeeState.ACTIVE, new Date(25, 7, 2023)
        );
        employees.addEmployee(prep1);
        getUserCredentialsDAO().addUser(prep1);


        EmailAddress emailPrep2 = new EmailAddress("a.vasiliou@team21.gr");
        OrderPreparationEmployee prep2 = new OrderPreparationEmployee(
                "a_vasiliou", "Άννα", "pass1241", "Βασιλείου",
                "6982233445", emailPrep2, "PREP-202", 120, 1250, 40, EmployeeState.ACTIVE, new Date(22, 1, 2020)
        );
        employees.addEmployee(prep2);
        getUserCredentialsDAO().addUser(prep2);


        EmailAddress emailPrep3 = new EmailAddress("k.papadakis@team21.gr");
        OrderPreparationEmployee prep3 = new OrderPreparationEmployee(
                "k_papadakis", "Κώστας", "pass1242", "Παπαδάκης",
                "6983344556", emailPrep3, "PREP-203", 100, 1180, 40, EmployeeState.ACTIVE, new Date(27, 8, 2021)
        );
        employees.addEmployee(prep3);
        getUserCredentialsDAO().addUser(prep3);


        //=====================================================
        //CATALOGUE UPDATE EMPLOYEES INITIALIZATION
        //=====================================================
        EmailAddress emailCat1 = new EmailAddress("d.georgiou@team21.gr");
        UpdateCatalogueEmployee cat1 = new UpdateCatalogueEmployee(
                "d_georgiou", "Δήμητρα", "pass1243", "Γεωργίου",
                "6941112223", emailCat1, "CAT-301", 150, 1300, 40, EmployeeState.ACTIVE, new Date(18, 9, 2022)
        );
        employees.addEmployee(cat1);
        getUserCredentialsDAO().addUser(cat1);


        EmailAddress emailCat2 = new EmailAddress("th.ioannou@team21.gr");
        UpdateCatalogueEmployee cat2 = new UpdateCatalogueEmployee(
                "th_ioannou", "Θάνος", "pass1244", "Ιωάννου",
                "6942223334", emailCat2, "CAT-302", 150, 1300, 40, EmployeeState.ACTIVE, new Date(15, 3, 2018)
        );
        employees.addEmployee(cat2);
        getUserCredentialsDAO().addUser(cat2);


        EmailAddress emailCat3 = new EmailAddress("e.rizos@team21.gr");
        UpdateCatalogueEmployee cat3 = new UpdateCatalogueEmployee(
                "e_rizos", "Ελένη", "pass1245", "Ρίζου",
                "6943334445", emailCat3, "CAT-303", 160, 1350, 40, EmployeeState.ACTIVE, new Date(25, 10, 2022)
        );
        employees.addEmployee(cat3);
        getUserCredentialsDAO().addUser(cat3);


        //=====================================================
        //DELIVERERS INITIALIZATION
        //=====================================================
        EmailAddress emailDel1 = new EmailAddress("n.stamos@team21.gr");
        Deliverer del1 = new Deliverer(
                "n_stamos", "Νίκος", "pass1246", "Στάμος",
                "6955556661", emailDel1, "DEL-401", 300, 900, 40, EmployeeState.ACTIVE, new Date(12, 11, 2024),
                15, true
        );
        getEmployeeDAO().addEmployee(del1);
        getUserCredentialsDAO().addUser(del1);


        EmailAddress emailDel2 = new EmailAddress("x.panou@team21.gr");
        Deliverer del2 = new Deliverer(
                "x_panou", "Χρήστος", "pass1247", "Πάνου",
                "6955556662", emailDel2, "DEL-402", 280, 900, 40, EmployeeState.ACTIVE, new Date(7, 10, 2023),
                12, true
        );
        getEmployeeDAO().addEmployee(del2);
        getUserCredentialsDAO().addUser(del2);


        EmailAddress emailDel3 = new EmailAddress("m.lazarou@team21.gr");
        Deliverer del3 = new Deliverer(
                "m_lazarou", "Μιχάλης", "pass1248", "Λαζάρου",
                "6955556663", emailDel3, "DEL-403", 320, 950, 40, EmployeeState.ACTIVE, new Date(10, 9, 2021),
                20, false
        );
        getEmployeeDAO().addEmployee(del3);
        getUserCredentialsDAO().addUser(del3);
        //=====================================================
        //END: INITIALIZE EMPLOYEES
        //=====================================================


        //=====================================================
        //START: INITIALIZE ADMIN
        //=====================================================

        EmailAddress email_Admin = new EmailAddress("geopap@team21.gr");
        Admin ad = Admin.getInstance("g_papadakis" , "Γεώργιος" , "ppd246" , "Παπαδάκης" , "6908381070" , email_Admin , 2000);
        getUserCredentialsDAO().addUser(ad);

        //=====================================================
        //END: INITIALIZE ADMIN
        //=====================================================


        //=====================================================
        //START: INITIALIZE PRODUCT TYPES
        //=====================================================

        ProductTypeDAO products = ProductTypeDAOMemory.getInstance();

        // --- 1. Laptops ---
        ProductType laptop1 = new ProductType("Dell XPS 15", "Φορητός υπολογιστής υψηλών επιδόσεων με οθόνη αφής 4K.", new Money(BigDecimal.valueOf(1850.00), "€"), "TECH-001");
        products.addProductType(laptop1);

        ProductType laptop2 = new ProductType("MacBook Air M2", "Ελαφρύ και κομψό laptop της Apple με κορυφαία αυτονομία.", new Money(BigDecimal.valueOf(1299.90), "€"), "TECH-002");
        products.addProductType(laptop2);

        // --- 2. Mice ---
        ProductType mouse1 = new ProductType("Logitech MX Master 3S", "Ασύρματο ποντίκι εργονομικής σχεδίασης με αθόρυβα κλικ.", new Money(BigDecimal.valueOf(99.90), "€"), "TECH-003");
        products.addProductType(mouse1);

        ProductType mouse2 = new ProductType("Razer DeathAdder V3", "Ενσύρματο ποντίκι gaming με εξαιρετικά ελαφρύ σχεδιασμό.", new Money(BigDecimal.valueOf(79.90), "€"), "TECH-004");
        products.addProductType(mouse2);

        // --- 3. Keyboards ---
        ProductType keyboard1 = new ProductType("Corsair K70 RGB", "Μηχανικό πληκτρολόγιο gaming με διακόπτες Cherry MX.", new Money(BigDecimal.valueOf(169.90), "€"), "TECH-005");
        products.addProductType(keyboard1);

        ProductType keyboard2 = new ProductType("Logitech MX Keys", "Ασύρματο πληκτρολόγιο χαμηλού προφίλ με έξυπνο φωτισμό.", new Money(BigDecimal.valueOf(119.00), "€"), "TECH-006");
        products.addProductType(keyboard2);

        // --- 4. Monitors ---
        ProductType monitor1 = new ProductType("LG UltraGear 27\"", "Gaming οθόνη 27 ιντσών με ρυθμό ανανέωσης 144Hz.", new Money(BigDecimal.valueOf(349.00), "€"), "TECH-007");
        products.addProductType(monitor1);

        ProductType monitor2 = new ProductType("Dell UltraSharp 32\"", "Επαγγελματική οθόνη 4K με εξαιρετική πιστότητα χρωμάτων.", new Money(BigDecimal.valueOf(750.00), "€"), "TECH-008");
        products.addProductType(monitor2);

        // --- 5. CPUs ---
        ProductType cpu1 = new ProductType("Intel Core i9-14900K", "Κορυφαίος επεξεργαστής desktop με 24 πυρήνες.", new Money(BigDecimal.valueOf(680.00), "€"), "TECH-009");
        products.addProductType(cpu1);

        ProductType cpu2 = new ProductType("AMD Ryzen 7 7800X3D", "Ο καλύτερος επεξεργαστής για gaming με τεχνολογία 3D V-Cache.", new Money(BigDecimal.valueOf(420.00), "€"), "TECH-010");
        products.addProductType(cpu2);

        // --- 6. RAM ---
        ProductType ram1 = new ProductType("Corsair Vengeance 32GB", "Σετ μνήμης RAM DDR5 υψηλής ταχύτητας με RGB.", new Money(BigDecimal.valueOf(145.00), "€"), "TECH-011");
        products.addProductType(ram1);

        ProductType ram2 = new ProductType("G.Skill Trident Z5", "Μνήμη RAM εξαιρετικά χαμηλής καθυστέρησης για overclocking.", new Money(BigDecimal.valueOf(180.00), "€"), "TECH-012");
        products.addProductType(ram2);

        // --- 7. GPUs ---
        ProductType gpu1 = new ProductType("Nvidia RTX 4070", "Κάρτα γραφικών νέας γενιάς με υποστήριξη DLSS 3.", new Money(BigDecimal.valueOf(650.00), "€"), "TECH-013");
        products.addProductType(gpu1);

        ProductType gpu2 = new ProductType("AMD Radeon RX 7800 XT", "Ισχυρή κάρτα γραφικών με 16GB μνήμης VRAM.", new Money(BigDecimal.valueOf(540.00), "€"), "TECH-014");
        products.addProductType(gpu2);

        // --- 8. Storage ---
        ProductType storage1 = new ProductType("Samsung 990 Pro 1TB", "Δίσκος SSD NVMe M.2 με απίστευτες ταχύτητες.", new Money(BigDecimal.valueOf(109.90), "€"), "TECH-015");
        products.addProductType(storage1);

        ProductType storage2 = new ProductType("WD Blue 4TB HDD", "Κλασικός σκληρός δίσκος μεγάλης χωρητικότητας.", new Money(BigDecimal.valueOf(85.00), "€"), "TECH-016");
        products.addProductType(storage2);

        // --- 9. Accessories ---
        ProductType accessory1 = new ProductType("Sony WH-1000XM5", "Ασύρματα ακουστικά με κορυφαία ακύρωση θορύβου.", new Money(BigDecimal.valueOf(329.00), "€"), "TECH-017");
        products.addProductType(accessory1);

        ProductType accessory2 = new ProductType("Razer Kraken", "Ενσύρματα gaming ακουστικά με ήχο 7.1 Surround, οδηγούς 50mm και μαξιλαράκια Cooling Gel για μέγιστη άνεση.", new Money(BigDecimal.valueOf(79.90), "€"), "TECH-018");
        products.addProductType(accessory2);

        ProductType accessory3 = new ProductType("Logitech C920 HD Pro", "Web κάμερα υψηλής ευκρίνειας 1080p.", new Money(BigDecimal.valueOf(65.50), "€"), "TECH-019");
        products.addProductType(accessory3);

        ProductType accessory4 = new ProductType("iPad Air 5th Gen", "Tablet με επεξεργαστή M1 και οθόνη Liquid Retina.", new Money(BigDecimal.valueOf(679.00), "€"), "TECH-020");
        products.addProductType(accessory4);

        ProductType applewatch = new ProductType("AppleWatch SE 44mm" , "Έξυπνο ρολοι με λειτουργικό iOS" , new Money(BigDecimal.valueOf(299.00) , "€") , "TECH-021");

        ProductType airTag = new ProductType("Apple AirTag" , "Tag για πανεύκολο εντοπισμό αντικειμένων" , new Money(BigDecimal.valueOf(29.00) , "€") , "TECH-022");


        //=====================================================
        //END: INITIALIZE PRODUCT TYPES
        //=====================================================

        //=====================================================
        //START: PRODUCTS WARAHOUSE FILLING
        //=====================================================
        ProductsWareHouseDAO warehouse = getProductsWareHouseDAO();

        try {
            if (warehouse.getProductStocks().size() != products.getProducts().size()) {
                throw new IllegalStateException("Products warehouse does not have all productTypes");
            }
            // --- Laptops (Ακριβά είδη - Λίγο απόθεμα) ---
            warehouse.increaseProductStock(products.getProduct("TECH-001"), 10); // Dell XPS
            warehouse.increaseProductStock(products.getProduct("TECH-002"), 8);  // MacBook Air

            // --- Mice (Περιφερειακά - Αρκετό απόθεμα) ---
            warehouse.increaseProductStock(products.getProduct("TECH-003"), 45); // Logitech MX
            warehouse.increaseProductStock(products.getProduct("TECH-004"), 30); // Razer Mouse

            // --- Keyboards ---
            warehouse.increaseProductStock(products.getProduct("TECH-005"), 25); // Corsair
            warehouse.increaseProductStock(products.getProduct("TECH-006"), 35); // Logitech Keys

            // --- Monitors (Ογκώδη είδη) ---
            warehouse.increaseProductStock(products.getProduct("TECH-007"), 15); // LG Monitor
            warehouse.increaseProductStock(products.getProduct("TECH-008"), 10); // Dell Monitor

            // --- CPUs (Υψηλή ζήτηση) ---
            warehouse.increaseProductStock(products.getProduct("TECH-009"), 12); // Intel i9
            warehouse.increaseProductStock(products.getProduct("TECH-010"), 12); // Ryzen 7

            // --- RAM (Συχνή αγορά) ---
            warehouse.increaseProductStock(products.getProduct("TECH-011"), 50); // Corsair RAM
            warehouse.increaseProductStock(products.getProduct("TECH-012"), 40); // G.Skill RAM

            // --- GPUs (Πολύ ακριβά - Περιορισμένο απόθεμα) ---
            warehouse.increaseProductStock(products.getProduct("TECH-013"), 6);  // Nvidia 4070
            warehouse.increaseProductStock(products.getProduct("TECH-014"), 8);  // AMD 7800 XT

            // --- Storage ---
            warehouse.increaseProductStock(products.getProduct("TECH-015"), 60); // Samsung SSD
            warehouse.increaseProductStock(products.getProduct("TECH-016"), 40); // WD HDD

            // --- Accessories ---
            warehouse.increaseProductStock(products.getProduct("TECH-017"), 20); // Sony Headphones
            warehouse.increaseProductStock(products.getProduct("TECH-018"), 25); // Razer Headset
            warehouse.increaseProductStock(products.getProduct("TECH-019"), 30); // Webcam
            warehouse.increaseProductStock(products.getProduct("TECH-020"), 15); // iPad Air
        } catch (Exception e) {
            e.printStackTrace();
        }

        //=====================================================
        //END: PRODUCTS WARAHOUSE FILLING
        //=====================================================


        //=====================================================
        //START: INITIALIZE ORDERS
        //=====================================================
        OrderDAO orders = (OrderDAO) OrderDAOMemory.getInstance();


        //ORDER 1
        ShoppingCart cart1 = new ShoppingCart(cust1);
        cart1.addItem(new CartItem(products.getProduct("TECH-001"), 1));
        cart1.addItem(new CartItem(products.getProduct("TECH-003"), 1));

        Order order1 = new Order("ORD-2023-001", new Date(10, 11, 2023), OrderStatusType.SHIPPED,
                true, PaymentType.CARD, new Date(14, 11, 2023), cart1
        );

        order1.setTotal_amount(cart1.getTotalCost());
        cust1.Confirm("CONFIRM",order1);
        orders.addOrder(order1); // <--- ΑΠΟΘΗΚΕΥΣΗ


        //ORDER 2
        ShoppingCart cart2 = new ShoppingCart(cust2);
        // CPU (1), GPU (1), RAM (2)
        cart2.addItem(new CartItem(products.getProduct("TECH-009"), 1));
        cart2.addItem(new CartItem(products.getProduct("TECH-013"), 1));
        cart2.addItem(new CartItem(products.getProduct("TECH-011"), 2));

        Order order2 = new Order("ORD-2024-002", new Date(5, 1, 2024), OrderStatusType.NEW,
                false, PaymentType.CASH, null, cart2
        );
        order2.setTotal_amount(cart2.getTotalCost());
        cust2.Confirm("CONFIRM",order2);
        orders.addOrder(order2); // <--- ΑΠΟΘΗΚΕΥΣΗ


        //ORDER 3
        ShoppingCart cart3 = new ShoppingCart(cust3);
        // Headphones (1) + Webcam (1)
        cart3.addItem(new CartItem(products.getProduct("TECH-017"), 1));
        cart3.addItem(new CartItem(products.getProduct("TECH-019"), 1));

        Order order3 = new Order("ORD-2024-003", new Date(12, 1, 2024), OrderStatusType.SHIPPED,
                false, PaymentType.CASH, null, cart3
        );
        order3.setTotal_amount(cart3.getTotalCost());
        cust3.Confirm("CONFIRM",order3);
        orders.addOrder(order3); // <--- ΑΠΟΘΗΚΕΥΣΗ


        //ORDER 4
        ShoppingCart cart4 = new ShoppingCart(cust1);
        cart4.addItem(new CartItem(products.getProduct("TECH-007"), 2));

        Order order4 = new Order("ORD-2024-004", new Date(15, 1, 2024), OrderStatusType.DELAYED,
                false, PaymentType.CASH, null, cart4
        );

        order4.setTotal_amount(cart4.getTotalCost());
        cust1.Confirm("CONFIRM",order4);
        orders.addOrder(order4); // <--- ΑΠΟΘΗΚΕΥΣΗ


        //ORDER 5
        ShoppingCart cart5 = new ShoppingCart(cust2);
        cart5.addItem(new CartItem(products.getProduct("TECH-020"), 1));

        Order order5 = new Order("ORD-2024-005", new Date(20, 1, 2024), OrderStatusType.SHIPPED,
                true, PaymentType.CARD, null, cart5
        );

        order5.setTotal_amount(cart5.getTotalCost());
        cust2.Confirm("CONFIRM",order5);
        orders.addOrder(order5); // <--- ΑΠΟΘΗΚΕΥΣΗ

        //=====================================================
        //END: INITIALIZE ORDERS
        //=====================================================


        //=====================================================
        //START: INITIALIZE CATALOGUE UPDATE REQUESTS
        //=====================================================

        UpdateRequestDAO requests = getUpdateRequestDAO();


        // Request 1
        CatalogueUpdateRequest req1 = new CatalogueUpdateRequest(new Date(15, 1, 2024), "Αύξηση τιμής κατά 50€ λόγω νέας παρτίδας.",
                products.getProduct("TECH-001"), AllowedRequest.PROCESS_PRODUCT, 1
        );
        requests.addUpdateRequest(req1); // Ή requests.save(req1)


        // Request 2
        CatalogueUpdateRequest req2 = new CatalogueUpdateRequest(new Date(16, 1, 2024), "Διόρθωση τυπογραφικού λάθους στα DPI του αισθητήρα.",
                products.getProduct("TECH-004"), AllowedRequest.PROCESS_PRODUCT, 2
        );
        requests.addUpdateRequest(req2);


        // Request 3
        CatalogueUpdateRequest req3 = new CatalogueUpdateRequest(new Date(18, 1, 2024), "Εφαρμογή έκπτωσης 10% για προωθητική ενέργεια.",
                products.getProduct("TECH-017"), AllowedRequest.PROCESS_PRODUCT, 3
        );
        requests.addUpdateRequest(req3);


        // Request 4
        CatalogueUpdateRequest req4 = new CatalogueUpdateRequest(new Date(20, 1, 2024), "Το προϊόν καταργήθηκε από τον προμηθευτή, παρακαλώ να αφαιρεθεί.",
                products.getProduct("TECH-015"), AllowedRequest.DELETE_PRODUCT, 4
        );
        requests.addUpdateRequest(req4);


        // Request 5
        ProductType newPrinter = new ProductType("Canon Pixma TS3450", "Πολυμηχάνημα Inkjet έγχρωμο με WiFi.",
                new Money(BigDecimal.valueOf(55.90), "€"), "TECH-NEW-01"
        );

        CatalogueUpdateRequest req5 = new CatalogueUpdateRequest(new Date(22, 1, 2024), "Εισαγωγή νέου κωδικού εκτυπωτή στον κατάλογο.",
                newPrinter, AllowedRequest.INSERT_PRODUCT, 5
        );
        requests.addUpdateRequest(req5);

        //=====================================================
        //END: INITIALIZE CATALOGUE UPDATE REQUESTS
        //=====================================================

        //Initialize Authentication System
        AuthenticationSystem.getInstance();

    }

    public static CustomerDAO getCustomerDAO() {
        return CustomerDAOMemory.getInstance();
    }

    public static EmployeeDAO getEmployeeDAO() {
        return (EmployeeDAO) EmployeeDAOMemory.getInstance();
    }

    public static OrderDAO getOrderDAO() {
        return (OrderDAO) OrderDAOMemory.getInstance();
    }

    public static ProductsWareHouseDAO getProductsWareHouseDAO() {
        return (ProductsWareHouseDAO) ProductsWareHouseDAOMemory.getInstance();
    }

    public static ProductTypeDAO getProductTypeDAO() {
        return (ProductTypeDAO) ProductTypeDAOMemory.getInstance();
    }

    public static UpdateRequestDAO getUpdateRequestDAO() {
        return UpdateRequestDAOMemory.getInstance();
    }

    public static UserCredentialsDAO getUserCredentialsDAO() {
        return UserCredentialsDAOMemory.getInstance();
    }


}