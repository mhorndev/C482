package Main;

import Model.Part;
import Model.Product;
import Model.InHouse;
import Model.Outsourced;
import Model.Inventory;

public class SampleData {

    public static void populate() {
        Part p1  = new InHouse(1, "Generic Soap", 0.99, 12, 1, 100, 428);
        Part p2  = new InHouse(2, "Generic Shampoo", 3.49, 6, 1, 100, 428);
        Part p3  = new InHouse(3, "Generic Deodarant", 3.99, 9, 1, 100, 428);
        Part p4  = new InHouse(4, "Generic Toothpaste", 2.89, 17, 1, 100, 428);
        Part p5  = new InHouse(5, "Toothbrush", 0.99, 42, 1, 100, 428);
        Part p6  = new InHouse(6, "Hairbrush", 1.99, 24, 1, 100, 428);
        Part p7  = new Outsourced(7, "Brand Soap", 1.99, 12, 10, 20, "Irish Spring");
        Part p8  = new Outsourced(8, "Brand Shampoo", 5.99, 6, 1, 20, "Dove"); 
        Part p9  = new Outsourced(9, "Brand Deodarant", 4.49, 12, 1, 10, "Old Spice");
        Part p10 = new Outsourced(10, "Brand Toothpaste", 4.99, 12, 1, 10, "Crest");
                
        Inventory.addPart(p1);
        Inventory.addPart(p2);
        Inventory.addPart(p3);
        Inventory.addPart(p4);
        Inventory.addPart(p5);
        Inventory.addPart(p6);
        Inventory.addPart(p7);
        Inventory.addPart(p8);
        Inventory.addPart(p9);
        Inventory.addPart(p10);
        
        Product One = new Product(1, "Generic Pack", 12.99, 12, 1, 10);
        One.addAssociatedPart(p1);
        One.addAssociatedPart(p2);
        One.addAssociatedPart(p3);
        One.addAssociatedPart(p4);
        One.addAssociatedPart(p5);
        One.addAssociatedPart(p6);
        Inventory.addProduct(One);
        
        Product Two = new Product(2, "Brand Name Pack", 19.99, 12, 1, 10);
        Two.addAssociatedPart(p5);
        Two.addAssociatedPart(p6);
        Two.addAssociatedPart(p7);
        Two.addAssociatedPart(p8);
        Two.addAssociatedPart(p9);
        Two.addAssociatedPart(p10);
        Inventory.addProduct(Two);

    }

}

