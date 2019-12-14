package View_Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;
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

public class ModifyPartController implements Initializable {

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
    
    public void initData(Part part) {
        fieldId.setText(Integer.toString(part.getId()));
        fieldName.setText(part.getName());
        fieldPrice.setText(Double.toString(part.getPrice()));
        fieldStock.setText(Integer.toString(part.getStock()));
        fieldMin.setText(Integer.toString(part.getMin()));
        fieldMax.setText(Integer.toString(part.getMax()));
        
        if (InHouse.class.isAssignableFrom(part.getClass())) {
            InHouse ip = (InHouse) part;
            btnInHouse.setSelected(true);
            lblMachineId.visibleProperty().set(true);
            fieldMachineId.visibleProperty().set(true);
            lblCompanyName.visibleProperty().set(false);
            fieldCompanyName.visibleProperty().set(false);
            fieldMachineId.setText(Integer.toString(ip.getMachineId()));
        }
                
        if (Outsourced.class.isAssignableFrom(part.getClass())) {
            Outsourced op = (Outsourced) part;
            btnOutsourced.setSelected(true);
            lblMachineId.visibleProperty().set(false);
            fieldMachineId.visibleProperty().set(false);
            lblCompanyName.visibleProperty().set(true);
            fieldCompanyName.visibleProperty().set(true);
            fieldCompanyName.setText(op.getCompanyName());
        }
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
        Part part;
        String txtId = fieldId.getText();
        String txtName = fieldName.getText();
        String txtStock = fieldStock.getText();
        String txtPrice = fieldPrice.getText();
        String txtMin = fieldMin.getText();
        String txtMax = fieldMax.getText();
        String txtMachineId = fieldMachineId.getText();
        String txtCompanyName = fieldCompanyName.getText();
        
        ObservableList<String> errors = FXCollections.observableArrayList();

        if (btnInHouse.selectedProperty().get()) {
            part = new InHouse();
        } else {
            part = new Outsourced();
        }
                
        try {
            part.setId(Integer.parseInt(txtId));
        } catch (NumberFormatException e) {
            errors.add("'Id' must be a valid integer.");
        }
        
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
            InHouse ip = (InHouse) part;
            try {         
                ip.setMachineID(Integer.parseInt(txtMachineId));
            } catch (NumberFormatException e) {
                errors.add("'Machine Id' must be a valid integer.");
            }
        } else {
            Outsourced op = (Outsourced) part;
            if (!txtCompanyName.trim().isEmpty()) {
                op.setCompanyName(txtCompanyName);
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
            Inventory.updatePart(part);
        } catch (Exception e) {
            errors.add("Error updating the part.");
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