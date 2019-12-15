package View_Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ModifyProductController implements Initializable {
    
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
    @FXML private TextField fieldId;
    @FXML private TextField fieldName;
    @FXML private TextField fieldStock;
    @FXML private TextField fieldPrice;
    @FXML private TextField fieldMin;
    @FXML private TextField fieldMax;
    @FXML private Button btnSearch;
    @FXML private TextField fieldSearchParts;
    @FXML private Button btnCancel;
    @FXML private Button btnSave;
    @FXML private Button btnDelete;
    @FXML private Button btnAdd;
    
    private Product product;
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
    
    public void initData(Product product) {
        this.product = product;
        fieldId.setText(Integer.toString(product.getId()));
        fieldName.setText(product.getName());
        fieldPrice.setText(Double.toString(product.getPrice()));
        fieldStock.setText(Integer.toString(product.getStock()));
        fieldMin.setText(Integer.toString(product.getMin()));
        fieldMax.setText(Integer.toString(product.getMax()));
        associatedParts = product.getAllAssociatedParts();
        tableViewAssociatedParts.setItems(associatedParts);
    }
        
    @FXML
    void btnSearchAction(ActionEvent event) {
        ObservableList<Part> result = FXCollections.observableArrayList();
        try {
            int id = Integer.parseInt(fieldSearchParts.getText());
            Part p = Inventory.lookupPart(id);
            if (id == p.getId()) { result.add(p);}
        } catch (NumberFormatException e) {
            result = Inventory.lookupPart(fieldSearchParts.getText());
        }
        tableViewParts.setItems(result);
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
            ObservableList<Part> newParts = FXCollections.observableArrayList();
            associatedParts.forEach((Part p) -> {
                newParts.add(p);
            });
            product.deleteAllAssociatedParts();
            
            newParts.forEach((Part p) -> {
                product.addAssociatedPart(p);
            });
            
            if (associatedParts.size()<1) {
                errors.add("Products must include at least 1 part.");
            }
        } catch (NumberFormatException e) {
            errors.add("Error updating associated parts.");
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
            Inventory.updateProduct(product);
        } catch (Exception e) {
            errors.add("Error updating the product.");
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