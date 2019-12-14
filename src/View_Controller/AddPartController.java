package View_Controller;

import Model.Part;
import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddPartController implements Initializable {

    @FXML private TextField fieldId;
    @FXML private TextField fieldName;
    @FXML private TextField fieldStock;
    @FXML private TextField fieldPrice;
    @FXML private TextField fieldMin;
    @FXML private TextField fieldMax;
    @FXML private TextField fieldMachineId;
    @FXML private TextField fieldCompanyName;
    @FXML private RadioButton btnInHouse;
    @FXML private RadioButton btnOutsourced;
    @FXML private Button btnCancel;
    @FXML private Button btnSave;
    @FXML private Label lblMachineId;
    @FXML private Label lblCompanyName;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void btnInHouseAction(ActionEvent event) {
        lblMachineId.visibleProperty().set(true);
        fieldMachineId.visibleProperty().set(true);
        lblCompanyName.visibleProperty().set(false);
        fieldCompanyName.visibleProperty().set(false);
    }
    
    @FXML
    void btnOusourcedAction(ActionEvent event) {
        lblMachineId.visibleProperty().set(false);
        fieldMachineId.visibleProperty().set(false);
        lblCompanyName.visibleProperty().set(true);
        fieldCompanyName.visibleProperty().set(true);
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
        String txtMachineId = fieldMachineId.getText();
        String txtCompanyName = fieldCompanyName.getText();

        ObservableList<String> errors = FXCollections.observableArrayList();

        Part part;
        
        if (btnInHouse.selectedProperty().get()) {
            part = new InHouse();
        } else {
            part = new Outsourced();
        }
        
        part.setId(Inventory.autoIncrementedPartId+1);
        
        if (!txtName.trim().isEmpty()) {
            part.setName(txtName);
        } else {
            errors.add("'Name' is a required field.");
        }
                
        try {
            part.setStock(Integer.parseInt(txtStock));
        } catch (NumberFormatException e) {
            part.setStock(0);
        }
        
        try {
            part.setMin(Integer.parseInt(txtMin));
        } catch (NumberFormatException e) {
            errors.add("'Min' must be a valid integer.");
        }
        
        try {
            part.setMax(Integer.parseInt(txtMax));
        } catch (NumberFormatException e) {
            errors.add("'Max' must be a valid integer.");
        }
        
        try {
            part.setPrice(Double.parseDouble(txtPrice));
        } catch (NumberFormatException e) {
            errors.add("'Price' must be a valid double.");
        }

        if (InHouse.class.isAssignableFrom(part.getClass())) {
            InHouse inHousePart = (InHouse) part;
            try {         
                inHousePart.setMachineID(Integer.parseInt(txtMachineId));
            } catch (NumberFormatException e) {
                errors.add("'Machine Id' must be a valid integer.");
            }
        }
        
        if (Outsourced.class.isAssignableFrom(part.getClass())) {
            if (!txtCompanyName.trim().isEmpty()) {
                Outsourced outsourcedPart = (Outsourced) part;
                outsourcedPart.setCompanyName(txtCompanyName);
            } else {
                errors.add("'Company Name' is a required field.");
            }
        }
        
        if (!errors.isEmpty()) { errorDialog(errors); return; }
                
        if ((part.getMin() > part.getMax())) {
            errors.add("'Min' can not be greater than 'Max'.");
        }
        
        if ((part.getMax() < part.getMin())) {
            errors.add("'Max' can not be less than 'Min'.");
        }
        
        if ((part.getStock() < part.getMin()) || (part.getStock() > part.getMax())) {
            errors.add("'Stock' must be >= Min and <= Max.");
        }
        
        if (!errors.isEmpty()) { errorDialog(errors); return; }
        
        try {
            Inventory.addPart(part);
        } catch (Exception e) {
            errors.add("Error adding the part.");
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
        alert.setHeaderText("Part not valid");
        alert.setContentText(message.toString());
        alert.showAndWait();
    }
    
}