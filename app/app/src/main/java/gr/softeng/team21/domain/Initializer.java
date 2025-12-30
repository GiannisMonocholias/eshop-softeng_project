package gr.softeng.team21.domain;

import java.math.BigDecimal;

public class Initializer {
    public static void InitializeProducts() {
        ProductTypesRepository products = ProductTypesRepository.getInstance();
        products.clear();
        if (!products.getProducts().isEmpty()) {
            return;
        }
        try {

            // --- 1. Laptops ---
            products.addProductType(new ProductType("Dell XPS 15", "Φορητός υπολογιστής υψηλών επιδόσεων με οθόνη αφής 4K.", new Money(BigDecimal.valueOf(1850.00), "€"), "TECH-001"));
            products.addProductType(new ProductType("MacBook Air M2", "Ελαφρύ και κομψό laptop της Apple με κορυφαία αυτονομία.", new Money(BigDecimal.valueOf(1299.90), "€"), "TECH-002"));

            // --- 2. Mice ---
            products.addProductType(new ProductType("Logitech MX Master 3S", "Ασύρματο ποντίκι εργονομικής σχεδίασης με αθόρυβα κλικ.", new Money(BigDecimal.valueOf(99.90), "€"), "TECH-003"));
            products.addProductType(new ProductType("Razer DeathAdder V3", "Ενσύρματο ποντίκι gaming με εξαιρετικά ελαφρύ σχεδιασμό.", new Money(BigDecimal.valueOf(79.90), "€"), "TECH-004"));

            // --- 3. Keyboards ---
            products.addProductType(new ProductType("Corsair K70 RGB", "Μηχανικό πληκτρολόγιο gaming με διακόπτες Cherry MX.", new Money(BigDecimal.valueOf(169.90), "€"), "TECH-005"));
            products.addProductType(new ProductType("Logitech MX Keys", "Ασύρματο πληκτρολόγιο χαμηλού προφίλ με έξυπνο φωτισμό.", new Money(BigDecimal.valueOf(119.00), "€"), "TECH-006"));

            // --- 4. Monitors ---
            products.addProductType(new ProductType("LG UltraGear 27\"", "Gaming οθόνη 27 ιντσών με ρυθμό ανανέωσης 144Hz.", new Money(BigDecimal.valueOf(349.00), "€"), "TECH-007"));
            products.addProductType(new ProductType("Dell UltraSharp 32\"", "Επαγγελματική οθόνη 4K με εξαιρετική πιστότητα χρωμάτων.", new Money(BigDecimal.valueOf(750.50), "€"), "TECH-008"));

            // --- 5. CPUs ---
            products.addProductType(new ProductType("Intel Core i9-14900K", "Κορυφαίος επεξεργαστής desktop με 24 πυρήνες.", new Money(BigDecimal.valueOf(680.00), "€"), "TECH-009"));
            products.addProductType(new ProductType("AMD Ryzen 7 7800X3D", "Ο καλύτερος επεξεργαστής για gaming με τεχνολογία 3D V-Cache.", new Money(BigDecimal.valueOf(420.00), "€"), "TECH-010"));

            // --- 6. RAM ---
            products.addProductType(new ProductType("Corsair Vengeance 32GB", "Σετ μνήμης RAM DDR5 υψηλής ταχύτητας με RGB.", new Money(BigDecimal.valueOf(145.00), "€"), "TECH-011"));
            products.addProductType(new ProductType("G.Skill Trident Z5", "Μνήμη RAM εξαιρετικά χαμηλής καθυστέρησης για overclocking.", new Money(BigDecimal.valueOf(180.00), "€"), "TECH-012"));

            // --- 7. GPUs ---
            products.addProductType(new ProductType("Nvidia RTX 4070", "Κάρτα γραφικών νέας γενιάς με υποστήριξη DLSS 3.", new Money(BigDecimal.valueOf(650.00), "€"), "TECH-013"));
            products.addProductType(new ProductType("AMD Radeon RX 7800 XT", "Ισχυρή κάρτα γραφικών με 16GB μνήμης VRAM.", new Money(BigDecimal.valueOf(540.00), "€"), "TECH-014"));

            // --- 8. Storage ---
            products.addProductType(new ProductType("Samsung 990 Pro 1TB", "Δίσκος SSD NVMe M.2 με απίστευτες ταχύτητες.", new Money(BigDecimal.valueOf(109.90), "€"), "TECH-015"));
            products.addProductType(new ProductType("WD Blue 4TB HDD", "Κλασικός σκληρός δίσκος μεγάλης χωρητικότητας.", new Money(BigDecimal.valueOf(85.00), "€"), "TECH-016"));

            // --- 9. Accessories ---
            products.addProductType(new ProductType("Sony WH-1000XM5", "Ασύρματα ακουστικά με κορυφαία ακύρωση θορύβου.", new Money(BigDecimal.valueOf(329.00), "€"), "TECH-017"));
            products.addProductType(new ProductType("Razer Kraken", "Ενσύρματα gaming ακουστικά με ήχο 7.1 Surround, οδηγούς 50mm και μαξιλαράκια Cooling Gel για μέγιστη άνεση.", new Money(BigDecimal.valueOf(79.90), "€"), "TECH-018"));
            products.addProductType(new ProductType("Logitech C920 HD Pro", "Web κάμερα υψηλής ευκρίνειας 1080p.", new Money(BigDecimal.valueOf(65.50), "€"), "TECH-019"));
            products.addProductType(new ProductType("iPad Air 5th Gen", "Tablet με επεξεργαστή M1 και οθόνη Liquid Retina.", new Money(BigDecimal.valueOf(679.00), "€"), "TECH-020"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

