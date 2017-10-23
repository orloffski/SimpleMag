package view.stockviews;

import application.DBClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.BarcodesItems;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BarcodeItemsViewController {

    private ObservableList<BarcodesItems> data;
    private Connection connection;
    private int itemId;

    private Stage dialogStage;

    @FXML
    private TableView<BarcodesItems> barcodesItemsTable;

    @FXML
    private TableColumn<BarcodesItems, String> barcodeColumn;

    @FXML
    private TableColumn<BarcodesItems, String> vendorCodeColumn;

    @FXML
    private TableColumn<BarcodesItems, String> nameColumn;

    @FXML
    private TextField filter;

    @FXML
    private Button select;

    @FXML
    private Button cancel;

    @FXML
    private void initialize() {
        barcodeColumn.setCellValueFactory(cellData -> cellData.getValue().barcodeProperty());
        vendorCodeColumn.setCellValueFactory(cellData -> cellData.getValue().vendorCodeProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        try {
            connection = new DBClass().getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        init();
    }

    @FXML
    private void onSelect(){
        BarcodesItems item = barcodesItemsTable.getSelectionModel().getSelectedItem();
        this.itemId = item.getItemId();
        dialogStage.close();
    }

    @FXML
    private void onCancel(){
        this.itemId = -1;
        dialogStage.close();
    }

    private void init(){
        data = FXCollections.observableArrayList();
        try{
            String SQL = "SELECT barcodes.barcode, items.vendor_code, items.name, items.id " +
                    "FROM barcodes, items " +
                    "WHERE barcodes.item_id = items.id;";
            ResultSet rs = connection.createStatement().executeQuery(SQL);
            while(rs.next()){
                BarcodesItems item = new BarcodesItems(
                        rs.getString("barcode"),
                        rs.getString("vendor_code"),
                        rs.getString("name"),
                        rs.getInt("id"));
                data.add(item);
            }
            barcodesItemsTable.setItems(data);

            addFilter();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void addFilter() {
        FilteredList<BarcodesItems> filteredData = new FilteredList<>(data, p -> true);

        filter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(barcodeItem -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (barcodeItem.getBarcode().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches barcode.
                } else if (barcodeItem.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches name.
                } else if (barcodeItem.getVendorCode().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches vendor code.
                }
                return false; // Does not match.
            });
        });

        SortedList<BarcodesItems> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(barcodesItemsTable.comparatorProperty());
        barcodesItemsTable.setItems(sortedData);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public int getItemId(){
        return this.itemId;
    }
}
