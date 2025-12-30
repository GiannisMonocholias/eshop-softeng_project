package gr.softeng.team21.dao;

import java.util.HashMap;

import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.memorydao.ProductsWareHouseDAOMemory;

public interface ProductTypeDAO {

     ProductType getProduct(String productCode);

     void addProductType(ProductType product);

     void deleteProductType(ProductType product);

     void processProduct(ProductType updatedProduct);

     HashMap<String, ProductType> getProducts ();

     void clear();
}
