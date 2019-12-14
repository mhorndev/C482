package View_Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import Main.SampleData;
import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import java.io.IOException;
import java.util.Optional;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainScreenController implements Initializable {

    @FXML private TableView<Part> tableViewParts;
    @FXML private TableColumn<Part, Integer> partIdColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> partStockColumn;
    @FXML private TableColumn<Part, Double> partPriceColumn;
    @FXML private TableView<Product> tableViewProducts;
    @FXML private TableColumn<Product, Integer> productIdColumn;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, Integer> productStockColumn;
    @FXML private TableColumn<Product, Double> productPriceColumn;
    @FXML private TextField fieldSearchParts;
    @FXML private TextField fieldSearchProducts;

    private Stage window;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        SampleData.populate();
        
        partIdColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partStockColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        
        productIdColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        productStockColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        
        tableViewParts.setItems(Inventory.getAllParts());
        tableViewProducts.setItems(Inventory.getAllProducts());
    }
    
    @FXML
    void btnAddPartAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(AddPartController.class.getResource("AddPart.fxml"));
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node)event.getSource()).getScene().getWindow() );
        stage.show();
    }
    
    @FXML
    void btnModifyPartAction(ActionEvent event) throws IOException {
        if (tableViewParts.getSelectionModel().isEmpty()) return;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyPart.fxml"));
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(new Scene((Pane) loader.load()));
        stage.initOwner(((Node)event.getSource()).getScene().getWindow());
        ModifyPartController controller = loader.<ModifyPartController>getController();
        Part p = tableViewParts.getSelectionModel().getSelectedItem();
        controller.initData(p);
        stage.show();
    }

    
    @FXML
    void btnAddProductAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(AddProductController.class.getResource("AddProduct.fxml"));
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node)event.getSource()).getScene().getWindow() );
        stage.show();
    }
        
    @FXML
    void btnModifyProductAction(ActionEvent event) throws IOException {
        if (tableViewProducts.getSelectionModel().isEmpty()) return;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyProduct.fxml"));
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(new Scene((Pane) loader.load()));
        stage.initOwner(((Node)event.getSource()).getScene().getWindow());
        ModifyProductController controller = loader.<ModifyProductController>getController();
        Product p = tableViewProducts.getSelectionModel().getSelectedItem();
        controller.initData(p);
        stage.show();
    }
    
    @FXML
    void btnDeletePartAction(ActionEvent event) {
        if (tableViewParts.getSelectionModel().isEmpty()) return;
        Part p = tableViewParts.getSelectionModel().getSelectedItem();
        Inventory.deletePart(p);
    }
        
    @FXML 
    void btnDeleteProductAction(ActionEvent event) {
        if (tableViewProducts.getSelectionModel().isEmpty()) return;
        Product p = tableViewProducts.getSelectionModel().getSelectedItem();
        Inventory.deleteProduct(p);
    }
    
    @FXML
    void btnSearchPartsAction(ActionEvent event) {
        tableViewParts.setItems(Inventory.lookupPart(fieldSearchParts.getText()));
    }
    
    @FXML
    void btnSearchProductsAction(ActionEvent event) {
        tableViewProducts.setItems(Inventory.lookupProduct(fieldSearchProducts.getText()));
    }
    
    @FXML
    void btnExit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getButtonTypes().remove(ButtonType.OK);
        alert.getButtonTypes().add(ButtonType.CANCEL);
        alert.getButtonTypes().add(ButtonType.YES);
        alert.setTitle("Exit application");
        alert.setHeaderText("Exit confirmation");
        alert.setContentText(String.format("Are you sure you want to exit?"));
        alert.initOwner(this.window.getOwner());
        Optional<ButtonType> res = alert.showAndWait();

        if(res.isPresent()) {
            if(res.get().equals(ButtonType.CANCEL))
                return;
        }
        Platform.exit();
        System.exit(0);
    }
    
    public void addWindowEvent(Stage stage) {
        this.window = stage;
        stage.setOnCloseRequest((WindowEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.getButtonTypes().remove(ButtonType.OK);
            alert.getButtonTypes().add(ButtonType.CANCEL);
            alert.getButtonTypes().add(ButtonType.YES);
            alert.setTitle("Exit application");
            alert.setHeaderText("Exit confirmation");
            alert.setContentText(String.format("Are you sure you want to exit"));
            alert.initOwner(stage.getOwner());
            Optional<ButtonType> res = alert.showAndWait();
            
            if(res.isPresent()) {
                if(res.get().equals(ButtonType.CANCEL))
                    event.consume();
            }
        });        
    }
}