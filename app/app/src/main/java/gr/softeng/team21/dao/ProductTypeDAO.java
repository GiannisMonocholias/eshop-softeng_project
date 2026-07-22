package gr.softeng.team21.dao;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.domain.ProductType;

/**
 * Interface for the Product Type Data Access Object.
 * Defines asynchronous operations for managing product definitions within the catalogue.
 * @author PAVLOS GRATSANIS
 */
public interface ProductTypeDAO {

     /**
      * Retrieves a specific product type asynchronously based on its unique product code.
      * @param productCode the unique code of the product type to retrieve.
      * @return A CompletableFuture containing the ProductType object, or null if no such product code is registered.
      * Completes exceptionally with an IllegalArgumentException if the productCode is null.
      */
     CompletableFuture<ProductType> getProduct(String productCode);

     /**
      * Registers a new product type in the repository asynchronously and initializes it in the warehouse.
      * @param product the ProductType object to add.
      * @return A CompletableFuture representing the completion of the insertion.
      * Completes exceptionally with an IllegalArgumentException if the product is null or already exists.
      */
     CompletableFuture<Void> addProductType(ProductType product);

     /**
      * Removes a product type from the repository asynchronously and also deletes it from the warehouse.
      * @param product the ProductType object to remove.
      * @return A CompletableFuture representing the completion of the deletion.
      * Completes exceptionally with an IllegalArgumentException if the product is null or not found.
      */
     CompletableFuture<Void> deleteProductType(ProductType product);

     /**
      * Updates the details of an existing product type asynchronously (name, price, description).
      * @param updatedProduct the ProductType object containing the updated information.
      * @return A CompletableFuture representing the completion of the update.
      * Completes exceptionally with an IllegalArgumentException if updatedProduct is null,
      * or an IllegalStateException if the product code is not found.
      */
     CompletableFuture<Void> processProduct(ProductType updatedProduct);

     /**
      * Retrieves all registered product types asynchronously.
      * @return A CompletableFuture containing a map of all registered product types.
      */
     CompletableFuture<HashMap<String, ProductType>> getProducts();

     /**
      * Clears all registered product types from the repository asynchronously.
      * @return A CompletableFuture representing the completion of the clearing operation.
      */
     CompletableFuture<Void> clear();
}