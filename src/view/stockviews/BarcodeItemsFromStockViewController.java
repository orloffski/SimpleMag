package view.stockviews;

import application.DBClass;
import dbhelpers.PricesDBHelper;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.BarcodeItemsFromStock;
import model.BarcodesItems;
import org.hibernate.SessionFactory;
import utils.HibernateUtil;
import utils.SelectedObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BarcodeItemsFromStockViewController {

    private ObservableList<BarcodeItemsFromStock> data;
    private Connection connection;
    private BarcodeItemsFromStock productFromStock;

    private Stage dialogStage;

    private int counterpartyId;

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
    private TableColumn<BarcodeItemsFromStock, String> priceColumn;

    @FXML
    private TextField filter;

    @FXML
    private Button select;

    @FXML
    private Button cancel;

    @FXML
    private void initialize() {

    }

    private void init(){
        data = FXCollections.observableArrayList();
        try{
            String SQL;
            ResultSet rs;
            if(counterpartyId != -1){
                SQL = "SELECT b.barcode, p.item_name, p.items_count, p.expire_date, p.invoice_number, p.item_id " +
                        "FROM barcodes b JOIN" +
                        "(SELECT products.item_name, " +
                        "        SUM(products.items_count) AS items_count, " +
                        "        products.expire_date, " +
                        "        products.invoice_number, " +
                        "        products.item_id " +
                        "FROM products_in_stock AS products " +
                        "WHERE products.counterparty_id = " + counterpartyId + " " +
                        "GROUP BY products.item_id) " +
                        "p ON b.item_id = p.item_id;";
                rs = connection.createStatement().executeQuery(SQL);
            }else{
                SQL = "SELECT b.barcode, p.item_name, p.items_count, p.expire_date, p.invoice_number, p.item_id " +
                        "FROM barcodes b JOIN" +
                        "(SELECT products.item_name, " +
                        "        SUM(products.items_count) AS items_count, " +
                        "        products.expire_date, " +
                        "        products.invoice_number, " +
                        "        products.item_id " +
                        "FROM products_in_stock AS products " +
                        "GROUP BY products.item_id) " +
                        "p ON b.item_id = p.item_id;";
                rs = connection.createStatement().executeQuery(SQL);
            }

            while(rs.next()){
                String price = PricesDBHelper.getLastPriceByItemId(HibernateUtil.getSessionFactory(), rs.getInt("item_id"))
                        == null ? "0.0" :
                        PricesDBHelper.getLastPriceByItemId(HibernateUtil.getSessionFactory(), rs.getInt("item_id")).getPrice();

                BarcodeItemsFromStock item = new BarcodeItemsFromStock(
                        rs.getString("barcode"),
                        rs.getString("item_name"),
                        rs.getInt("items_count"),
                        rs.getString("expire_date"),
                        rs.getString("invoice_number"),
                        rs.getInt("item_id"),
                        price);
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
                } else if(barcodeItemsFromStock.getPrice().toLowerCase().contains(lowerCaseFilter))
                    return true;
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
        SelectedObject.setObject(this.productFromStock);
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

    public void setCounterpartyId(int counterpartyId){
        this.counterpartyId = counterpartyId;

        barcodeColumn.setCellValueFactory(cellData -> cellData.getValue().barcodeProperty());
        itemNameColumn.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        itemCountColumn.setCellValueFactory(cellData -> cellData.getValue().itemCountProperty());
        expireDateColumn.setCellValueFactory(cellData -> cellData.getValue().expireDateProperty());
        invoiceNumColumn.setCellValueFactory(cellData -> cellData.getValue().invoiceNumProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());

        try {
            connection = new DBClass().getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        init();
    }

    public BarcodeItemsFromStock getStockLine(){
        return null;
    }
}
