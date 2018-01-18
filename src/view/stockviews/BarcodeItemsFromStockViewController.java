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
import model.BarcodeItemsFromStock;
import model.BarcodesItems;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BarcodeItemsFromStockViewController {

    private ObservableList<BarcodeItemsFromStock> data;
    private Connection connection;
    private BarcodeItemsFromStock productFromStock;

    private Stage dialogStage;

    @FXML
    private TableView<BarcodeItemsFromStock> barcodeItemsFromStockTable;

    @FXML
    private TableColumn<BarcodeItemsFromStock, String> barcodeColumn;

    @FXML
    private TableColumn<BarcodeItemsFromStock, String> itemNameColumn;

    @FXML
    private TableColumn<BarcodeItemsFromStock, Number> itemCountColumn;

    @FXML
    private TableColumn<BarcodeItemsFromStock, String> expireDateColumn;

    @FXML
    private TableColumn<BarcodeItemsFromStock, String> invoiceNumColumn;

    @FXML
    private TextField filter;

    @FXML
    private Button select;

    @FXML
    private Button cancel;

    @FXML
    private void initialize() {
        barcodeColumn.setCellValueFactory(cellData -> cellData.getValue().barcodeProperty());
        itemNameColumn.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        itemCountColumn.setCellValueFactory(cellData -> cellData.getValue().itemCountProperty());
        expireDateColumn.setCellValueFactory(cellData -> cellData.getValue().expireDateProperty());
        invoiceNumColumn.setCellValueFactory(cellData -> cellData.getValue().invoiceNumProperty());

        try {
            connection = new DBClass().getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        init();
    }

    private void init(){
        data = FXCollections.observableArrayList();
        try{
            String SQL = "SELECT barcodes.barcode, products.item_name, products.items_count, products.expire_date, products.invoice_number, products.item_id\n" +
                    "FROM barcodes, products_in_stock AS products\n" +
                    "WHERE barcodes.item_id = products.item_id;";
            ResultSet rs = connection.createStatement().executeQuery(SQL);
            while(rs.next()){
                BarcodeItemsFromStock item = new BarcodeItemsFromStock(
                        rs.getString("barcode"),
                        rs.getString("item_name"),
                        rs.getInt("items_count"),
                        rs.getString("expire_date"),
                        rs.getString("invoice_number"),
                        rs.getInt("item_id"));
                data.add(item);
            }
            barcodeItemsFromStockTable.setItems(data);

            addFilter();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void addFilter() {
        FilteredList<BarcodeItemsFromStock> filteredData = new FilteredList<>(data, p -> true);

        filter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(barcodeItemsFromStock -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (barcodeItemsFromStock.getBarcode().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches barcode.
                } else if (barcodeItemsFromStock.getItemName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches name.
                } else if (barcodeItemsFromStock.getInvoiceNum().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches invoice number.
                }
                return false; // Does not match.
            });
        });

        SortedList<BarcodeItemsFromStock> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(barcodeItemsFromStockTable.comparatorProperty());
        barcodeItemsFromStockTable.setItems(sortedData);
    }

    @FXML
    private void onSelect(){
        this.productFromStock = barcodeItemsFromStockTable.getSelectionModel().getSelectedItem();
        dialogStage.close();
    }

    @FXML
    private void onCancel(){
        this.productFromStock = null;
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public BarcodeItemsFromStock getStockLine(){
        return null;
    }
}
