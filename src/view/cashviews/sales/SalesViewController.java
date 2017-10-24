package view.cashviews.sales;

import application.DBClass;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.InvoiceLine;
import model.SalesHeader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SalesViewController {

    private Main main;
    private DBClass dbClass;
    private Connection connection;
    private ObservableList<SalesHeader> salesHeadersData;

    @FXML
    private TextField filter;

    @FXML
    private TableView<SalesHeader> salesHeaderTable;

    @FXML
    private TableColumn<SalesHeader, String> salesNumber;

    @FXML
    private TableColumn<SalesHeader, Double> salesSumm;

    @FXML
    private TableColumn<SalesHeader, String> salesType;

    @FXML
    private TableColumn<SalesHeader, String> paymentType;

    @FXML
    private void initialize() {
        loadConnection();

        salesNumber.setCellValueFactory(cellData -> cellData.getValue().salesNumberProperty());
        salesSumm.setCellValueFactory(cellData -> cellData.getValue().salesSummProperty().asObject());
        salesType.setCellValueFactory(cellData -> cellData.getValue().salesTypeProperty());
        paymentType.setCellValueFactory(cellData -> cellData.getValue().paymentTypeProperty());

        loadSalesHeaders();
    }

    private void loadConnection(){
        dbClass = new DBClass();
        try{
            connection = dbClass.getConnection();
        }
        catch(ClassNotFoundException | SQLException ce){
            ce.printStackTrace();
        }
    }

    private void loadSalesHeaders(){
        salesHeadersData = FXCollections.observableArrayList();

        try{
            String SQL = "SELECT * FROM sales_header";
            ResultSet rs = connection.createStatement().executeQuery(SQL);
            while(rs.next()){
                SalesHeader header = new SalesHeader(
                        rs.getInt("id"),
                        rs.getString("sales_number"),
                        rs.getDouble("summ"),
                        rs.getString("sales_type"),
                        rs.getString("payment"),
                        rs.getString("lastcreateupdate")
                );
                salesHeadersData.add(header);
            }

            salesHeaderTable.setItems(salesHeadersData);

            addFilter();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void addFilter() {
        FilteredList<SalesHeader> filteredData = new FilteredList<>(salesHeadersData, p -> true);

        filter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(header -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (header.getSalesNumber().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches number.
                }else if (header.getSalesType().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches sales type.
                }else if (header.getPaymentType().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches payment.
                }
                return false; // Does not match.
            });
        });

        SortedList<SalesHeader> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(salesHeaderTable.comparatorProperty());
        salesHeaderTable.setItems(sortedData);
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
