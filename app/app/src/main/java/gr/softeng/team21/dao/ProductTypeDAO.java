package gr.softeng.team21.dao;

import java.util.HashMap;

import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.memorydao.ProductsWareHouseDAOMemory;

public interface ProductTypeDAO {

     /**
      * Retrieves a specific product type based on its unique product code.
      * @param productCode the unique code of the product type to retrieve.
      * @return the ProductType object, or null if no such product code is registered.
      * @throws IllegalArgumentException if the productCode is null.
      */
     ProductType getProduct(String productCode);

     /**
      * Registers a new product type in the repository and initializes it in the warehouse.
      * @param product the ProductType object to add.
      * @throws IllegalArgumentException if the product is null or already exists in the repository.
      */
     void addProductType(ProductType product);

     /**
      * Removes a product type from the repository and also deletes it from the warehouse.
      * @param product the ProductType object to remove.
      * @throws IllegalArgumentException if the product is null or not found in the repository.
      */
     void deleteProductType(ProductType product);

     /**
      * Updates the details of an existing product type (name, price, description).
      * @param updatedProduct the ProductType object containing the updated information.
      * @throws IllegalArgumentException if the updatedProduct is null.
      * @throws IllegalStateException if the product code is not found in the repository.
      */
     void processProduct(ProductType updatedProduct);

     /**
      * @return a map containing all registered product types.
      */
     HashMap<String, ProductType> getProducts ();

     /**
      * Clears all registered product types from the repository.
      */
     void clear();
}
