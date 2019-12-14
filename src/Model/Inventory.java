package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {

    public static int autoIncrementedPartId = 0;
    public static int autoIncrementedProductId = 0;
    
    private final static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private final static ObservableList<Product> allProducts = FXCollections.observableArrayList();
  
    public static void addPart(Part part) {
        allParts.add(part);
        autoIncrementedPartId += 1;
    }
    
    public static void addProduct(Product product) {
        allProducts.add(product);
        autoIncrementedProductId += 1;
    }
    
    public static Part lookupPart(int id) {
        for (Part p: allParts) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
    
    public static Product lookupProduct(int id) {
        for (Product p: allProducts) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
    
    public static ObservableList<Part> lookupPart(String search) {
        ObservableList<Part> result = FXCollections.observableArrayList();
        allParts.stream().filter((p) -> (p.getName().toLowerCase().contains(search.toLowerCase()))).forEachOrdered((p) -> {
            result.add(p);
        });
        return result;
    }
    
    public static ObservableList<Product> lookupProduct(String search) {
        ObservableList<Product> result = FXCollections.observableArrayList();
        allProducts.stream().filter((p) -> (p.getName().toLowerCase().contains(search))).forEachOrdered((p) -> {
            result.add(p);
        });
        return result;
    }

    public static void updatePart(Part part) {
        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getId()== part.getId()) {
                allParts.set(i, part);
                break;
            }
        }
    }
    
    public static void updateProduct(Product product) {
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getId()== product.getId()) {
                allProducts.set(i, product);
                break;
            }
        }
    }
        
    public static boolean deletePart(Part part) {
        return allParts.removeIf(p -> p.getId() == part.getId());
    }
    
    public static boolean deleteProduct(Product product) {
        return allProducts.removeIf(p -> p.getId() == product.getId());
    }
        
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }
    
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

}
