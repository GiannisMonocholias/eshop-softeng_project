package gr.softeng.team21.domain;

import java.util.HashMap;
public class ProductTypesRepository {
    private static ProductTypesRepository instance;
    private HashMap<String,ProductType> products;
    private ProductTypesRepository(){
        products = new HashMap<>();
    }


    public static ProductTypesRepository getInstance(){
        if (instance == null){
            instance = new ProductTypesRepository();
        }
        return instance;


    }
    public ProductType getProduct(String productCode){
       if(productCode == null)
           throw  new IllegalArgumentException("Product code cannot be null");

        if(products.containsKey(productCode))
            return products.get(productCode);
        else
            return null;
    }


    public void addProductType(ProductType product){
        if(product != null) {
            if(!products.containsKey(product.getProductCode())) {
                products.put(product.getProductCode(), product);
                ProductsWareHouse.getInstance().insertProduct(product);
            }
            else {
                throw new IllegalArgumentException("The given product type is already in the repository");
            }
        }
        else{
            throw new IllegalArgumentException("Product cannot be null");
        }
    }

    public void deleteProductType(ProductType product){
        if(product != null){
            if(products.containsKey(product.getProductCode())) {
                products.remove(product.getProductCode());
                ProductsWareHouse.getInstance().deleteProduct(product);
            }
            else {
                throw new IllegalArgumentException("The given product type is not registered in the repository");
            }
        }
        else{
            throw new IllegalArgumentException("Product cannot be null");
        }
    }


    public void processProduct(ProductType updatedProduct){
        if(updatedProduct == null){
            throw new IllegalArgumentException("Product cannot be null");
        }

        if(products.containsKey(updatedProduct.getProductCode())){
            ProductType existingProduct = products.get(updatedProduct.getProductCode());
            existingProduct.setProductname(updatedProduct.getProductname());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setDescription(updatedProduct.getDescription());
        }
        else{
            throw new IllegalStateException("Product type with id " + updatedProduct.getProductCode() + " is not a registerd product type.");
        }
    }

    public HashMap<String, ProductType> getProducts () {
        return products;
    }

    public void clear(){
        products.clear();
    }

}
