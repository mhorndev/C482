package View_Controller;

import Model.Part;
import Model.Product;
import Model.Inventory;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

public class AddProductController implements Initializable {
    
    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    @FXML private TextField fieldName;
    @FXML private TextField fieldStock;
    @FXML private TextField fieldPrice;
    @FXML private TextField fieldMin;
    @FXML private TextField fieldMax;
    @FXML private TextField fieldSearchParts;
    @FXML private TableView<Part> tableViewParts;
    @FXML private TableColumn<Part, Integer> partIdColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> partStockColumn;
    @FXML private TableColumn<Part, Double> partPriceColumn;
    @FXML private TableView<Part> tableViewAssociatedParts;
    @FXML private TableColumn<Part, Integer> associatedPartIdColumn;
    @FXML private TableColumn<Part, String> associatedPartNameColumn;
    @FXML private TableColumn<Part, Integer> associatedPartStockColumn;
    @FXML private TableColumn<Part, Double> associatedPartPriceColumn;

    private ObservableList<Part> associatedParts;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        partIdColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partStockColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        
        associatedPartIdColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        associatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        associatedPartStockColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        associatedPartPriceColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        
        associatedParts = FXCollections.observableArrayList();
        
        tableViewParts.setItems(Inventory.getAllParts());
        tableViewAssociatedParts.setItems(associatedParts);
    }
    
    @FXML
    void btnSearchAction(ActionEvent event) {
        tableViewParts.setItems(Inventory.lookupPart(fieldSearchParts.getText()));
    }
    
    @FXML
    void btnAddAction(ActionEvent event) {
        if (tableViewParts.getSelectionModel().isEmpty()) return;
        Part p = tableViewParts.getSelectionModel().getSelectedItem();
        associatedParts.add(p);
    }
    
    @FXML
    void btnDeleteAction(ActionEvent event) {
        if (tableViewAssociatedParts.getSelectionModel().isEmpty()) return;
        Part p = tableViewAssociatedParts.getSelectionModel().getSelectedItem();
        associatedParts.remove(p);
    }
    
    @FXML
    void btnCancelAction(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    void btnSaveAction(ActionEvent event) {
        String txtName = fieldName.getText();
        String txtStock = fieldStock.getText();
        String txtPrice = fieldPrice.getText();
        String txtMin = fieldMin.getText();
        String txtMax = fieldMax.getText();
        
        ObservableList<String> errors = FXCollections.observableArrayList();
        
        Product product = new Product();
        product.setId(Inventory.autoIncrementedProductId+1);
        
        if (!txtName.trim().isEmpty()) {
            product.setName(txtName);
        } else {
            errors.add("'Name' is a required field.");
        }
                
        try {
            product.setStock(Integer.parseInt(txtStock));
        } catch (NumberFormatException e) {
            product.setStock(0);
        }
        
        try {
            product.setMin(Integer.parseInt(txtMin));
        } catch (NumberFormatException e) {
            errors.add("'Min' must be a valid integer.");
        }
        
        try {
            product.setMax(Integer.parseInt(txtMax));
        } catch (NumberFormatException e) {
            errors.add("'Max' must be a valid integer.");
        }
        
        try {
            product.setPrice(Double.parseDouble(txtPrice));
        } catch (NumberFormatException e) {
            errors.add("'Price' must be a valid double.");
        }
           
        try {
            associatedParts.forEach((p) -> { product.addAssociatedPart(p); });
        } catch (NumberFormatException e) {
            errors.add("Error adding the associated parts.");
        }
        
        if (associatedParts.size()<1) {
            errors.add("Products must include at least 1 part.");
        }
        
        if (!errors.isEmpty()) { errorDialog(errors); return; }
        
        if ((product.getMin() > product.getMax())) {
            errors.add("'Min' can not be greater than 'Max'.");
        }
        
        if ((product.getMax() < product.getMin())) {
            errors.add("'Max' can not be less than 'Min'.");
        }
        
        if ((product.getStock() < product.getMin()) || (product.getStock() > product.getMax())) {
            errors.add("'Stock' must be >= Min and <= Max.");
        }
        
        if (!errors.isEmpty()) { errorDialog(errors); return; }
        
        try {
            Inventory.addProduct(product);
        } catch (Exception e) {
            errors.add("Error adding the product.");
            return;
        }
                
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

    private void errorDialog(ObservableList<String> errors) {
        StringBuilder message = new StringBuilder();
        errors.forEach((error) -> {
            message.append(error).append("\n");
        });
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Validation Error");
        alert.setHeaderText("Product not valid");
        alert.setContentText(message.toString());
        alert.showAndWait();
    }
    
}