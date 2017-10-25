package view.cashviews.sales;

import application.DBClass;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.SalesHeader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
    private ImageView add;

    @FXML
    private ImageView delete;

    @FXML
    private void initialize() {
        loadConnection();

        salesNumber.setCellValueFactory(cellData -> cellData.getValue().salesNumberProperty());
        salesSumm.setCellValueFactory(cellData -> cellData.getValue().salesSummProperty().asObject());
        salesType.setCellValueFactory(cellData -> cellData.getValue().salesTypeProperty());
        paymentType.setCellValueFactory(cellData -> cellData.getValue().paymentTypeProperty());

        loadSalesHeaders();
    }

    @FXML
    private void delHeader(){
        SalesHeader header = salesHeaderTable.getSelectionModel().getSelectedItem();
        int indexToDelete = salesHeaderTable.getSelectionModel().getSelectedIndex();

        try {
            PreparedStatement statement = null;

            statement = connection.prepareStatement("DELETE FROM sales_header WHERE id = ?");
            statement.setInt(1, header.getId());
            statement.executeUpdate();

            statement = connection.prepareStatement("DELETE FROM sales_line WHERE sales_number = ?");
            statement.setString(1, header.getSalesNumber());
            statement.executeUpdate();

            salesHeadersData.remove(indexToDelete);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addHeader() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/cashviews/sales/AddSaleView.fxml"));
        try {
            BorderPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.getIcons().add(new Image("file:resources/images/check.png"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(main.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            AddSaleViewController controller = loader.getController();
            controller.setMain(main);
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
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