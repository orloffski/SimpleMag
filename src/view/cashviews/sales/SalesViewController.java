package view.cashviews.sales;

import application.Main;
import dbhelpers.SalesHeaderDBHelper;
import dbhelpers.SalesLinesDBHelper;
import entity.SalesHeaderEntity;
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
import utils.MessagesUtils;
import view.AbstractController;

import java.io.IOException;
import java.util.List;

public class SalesViewController extends AbstractController{

    private Main main;
    private ObservableList<SalesHeader> data;

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
    private ImageView edit;

    @FXML
    private void initialize() {
        getSessionData();

    	add.setImage(new Image("file:resources/images/add.png"));
    	delete.setImage(new Image("file:resources/images/delete.png"));
    	edit.setImage(new Image("file:resources/images/edit.png"));

        salesNumber.setCellValueFactory(cellData -> cellData.getValue().salesNumberProperty());
        salesSumm.setCellValueFactory(cellData -> cellData.getValue().salesSummProperty().asObject());
        salesType.setCellValueFactory(cellData -> cellData.getValue().salesTypeProperty());
        paymentType.setCellValueFactory(cellData -> cellData.getValue().paymentTypeProperty());

        loadSalesHeaders();
    }

    @FXML
    private void delHeader(){
        SalesHeader header = salesHeaderTable.getSelectionModel().getSelectedItem();

        if(header != null){
            String saleNumber = header.getSalesNumber();
            int id = header.getId();

            deleteLines(saleNumber);
            deleteHeader(id);

            data.clear();
            loadSalesHeaders();
        }else{
            MessagesUtils.showAlert("Ошибка удаления продажи","Выберите чек продажи для удаления.");
        }
    }

    @FXML
    private void editHeader(){
        SalesHeader header = salesHeaderTable.getSelectionModel().getSelectedItem();
        if(header == null){
            MessagesUtils.showAlert("Ошибка открытия продажи","Выберите чек продажи для редактирования.");
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/cashviews/sales/AddEditSaleView.fxml"));
        try {
            BorderPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.getIcons().add(new Image("file:resources/images/check.png"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(main.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            AddEditSaleViewController controller = loader.getController();
            controller.setMain(main);
            controller.setDialogStage(dialogStage);
            controller.setHeader(header);

            dialogStage.showAndWait();

            data.clear();
            loadSalesHeaders();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addHeader() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/cashviews/sales/AddEditSaleView.fxml"));
        try {
            BorderPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.getIcons().add(new Image("file:resources/images/check.png"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(main.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            AddEditSaleViewController controller = loader.getController();
            controller.setMain(main);
            controller.setDialogStage(dialogStage);
            controller.setHeader(null);

            dialogStage.showAndWait();

            data.clear();
            loadSalesHeaders();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSalesHeaders(){
        data = FXCollections.observableArrayList();

        List<SalesHeaderEntity> salesList = SalesHeaderDBHelper.getSalesHeadersEntitiesList(sessFact);

        for (SalesHeaderEntity saleHeader : salesList) {
            SalesHeader headerItem = SalesHeader.createHeaderFromEntity(saleHeader);
            data.add(headerItem);
        }

        salesHeaderTable.setItems(data);

        addFilter();
    }

    private void addFilter() {
        FilteredList<SalesHeader> filteredData = new FilteredList<>(data, p -> true);

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

    private void deleteLines(String saleNumber){
        SalesLinesDBHelper.deleteLinesBySalesNumber(sessFact, saleNumber);
    }

    private void deleteHeader(int id){
        SalesHeaderDBHelper.deleteHeaderById(sessFact, id);
    }

    @Override
    protected void clearForm() {

    }
}
