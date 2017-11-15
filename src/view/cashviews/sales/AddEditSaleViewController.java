package view.cashviews.sales;

import application.DBClass;
import application.Main;
import dbhelpers.SalesLinesDBHelper;
import entity.SalesLineEntity;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import model.*;
import utils.NumberUtils;
import view.AbstractController;
import view.stockviews.BarcodeItemsViewController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AddEditSaleViewController extends AbstractController {

    @FXML
    private Text checkNumber;

    @FXML
    private Text checkSumm;

    @FXML
    private TextField cash;

    @FXML
    private TextField nonCash;

    @FXML
    private ComboBox<String> saleType;

    @FXML
    private ComboBox<String> paymentType;

    @FXML
    private ImageView add;

    @FXML
    private ImageView delete;

    @FXML
    private TableView<SalesLine> salesLineTable;

    @FXML
    private TableColumn<SalesLine, String> itemName;

    @FXML
    private TableColumn<SalesLine, Integer> count;

    @FXML
    private TableColumn<SalesLine, Double> itemPrice;

    @FXML
    private TableColumn<SalesLine, Double> linePrice;

    @FXML
    private Button close;

    @FXML
    private Button save;


    private Main main;
    private Connection connection;
    private ObservableList<SalesLine> salesLinedata;
    private boolean headerCreated = false;
    private SalesHeader header;

    @FXML
    private void initialize() {
    	add.setImage(new Image("file:resources/images/add.png"));
    	delete.setImage(new Image("file:resources/images/delete.png"));

        try {
            connection = new DBClass().getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addLine(){
        int itemId = getNewItem();
        Items item = null;

        try {
            if(itemId != -1) {
                String SQL = "SELECT * FROM items WHERE id = '" + itemId + "';";
                ResultSet rs = null;
                rs = connection.createStatement().executeQuery(SQL);
                if (rs.next()) {
                    item = new Items(
                            rs.getInt("id"),
                            rs.getString("vendor_code"),
                            rs.getString("name"),
                            rs.getString("vendor_country"),
                            rs.getInt("unit_id"));
                }

                if (item != null) {
                    SQL = "SELECT id, price FROM prices WHERE item_id = " + item.getId() + " ORDER BY id DESC LIMIT 1;";
                    rs = connection.createStatement().executeQuery(SQL);
                    double price = 0;
                    if(rs.next())
                        price = rs.getDouble("price");

                    SQL = "INSERT INTO sales_line SET " +
                            "sales_number = '" + checkNumber.getText() + "', " +
                            "item_id = " + item.getId() + ", " +
                            "item_name = '" + item.getName() + "'," +
                            "count = " + 0 + ", " +
                            "item_price = " + price + ", " +
                            "full_line_price = " + 0 + ";";

                    connection.createStatement().executeUpdate(SQL);

                    salesLinedata.clear();
                    loadLines(checkNumber.getText());

                }
            }

            salesLineTable.setItems(salesLinedata);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteLine(){
        int indexToDelete = salesLineTable.getSelectionModel().getSelectedIndex();
        SalesLine line = salesLineTable.getSelectionModel().getSelectedItem();

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("DELETE FROM sales_line WHERE id = ?");
            statement.setInt(1, line.getId());
            statement.executeUpdate();

            salesLinedata.remove(indexToDelete);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Double newCheckSumm = NumberUtils.round(Double.parseDouble(checkSumm.getText()) - line.getLinePrice());
        checkSumm.setText(String.valueOf(newCheckSumm));
    }

    @FXML
    private void saveSale(){
        if(header == null){
            this.headerCreated = true;
            insertHeader();
        }else{
            updateHeader();
        }
    }

    @FXML
    private void closeStage(){

        if(salesLinedata.size() == 0){
            String saleNum = checkNumber.getText();

            PreparedStatement statement = null;
            try {
                statement = connection.prepareStatement("DELETE FROM sales_header WHERE sales_number = ?");
                statement.setString(1, saleNum);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            if(!this.headerCreated){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ошибка сохранения");
                alert.setContentText("Вы не сохранили документ, сохраните документ");

                alert.showAndWait();

                return;
            }
        }
        dialogStage.close();
    }

    private void loadLines(String saleNum){
        SalesLine line = null;
        String SQL = "SELECT * FROM sales_line WHERE sales_number = '" + saleNum + "';";
        ResultSet rs = null;
        try {
            rs = connection.createStatement().executeQuery(SQL);

            while(rs.next()){
                line = new SalesLine(
                        rs.getInt("id"),
                        rs.getString("sales_number"),
                        rs.getInt("item_id"),
                        rs.getString("item_name"),
                        rs.getInt("count"),
                        rs.getDouble("item_price"),
                        rs.getDouble("full_line_price")
                );

                salesLinedata.add(line);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getNewItem(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/stockviews/BarcodeItemsView.fxml"));
        try {
            BorderPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Выбор товара");
            dialogStage.getIcons().add(new Image("file:resources/images/barcode.png"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(main.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            BarcodeItemsViewController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            return controller.getItemId();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private void initTable(){
        getSessionData();

        salesLineTable.setItems(salesLinedata);

        itemName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());

        count.setCellValueFactory(cellData -> cellData.getValue().countProperty().asObject());
        count.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        count.setOnEditCommit(t -> updateLine(
                t.getNewValue().intValue()));

        itemPrice.setCellValueFactory(cellData -> cellData.getValue().itemPriceProperty().asObject());
        linePrice.setCellValueFactory(cellData -> cellData.getValue().linePriceProperty().asObject());

        if(this.header != null){
            List<SalesLineEntity> linesList = SalesLinesDBHelper.getLinesBySalesNumber(sessFact, this.header.getSalesNumber());

            for(SalesLineEntity entity : linesList)
                salesLinedata.add(SalesLine.createSalesLineFromSalesLineEntity(entity));

            salesLineTable.setItems(salesLinedata);
        }
    }

    private void updateLine(int newCount){
        this.headerCreated = false;

        SalesLine line = salesLineTable.getSelectionModel().getSelectedItem();
        Double oldLinePrice = line.getLinePrice();
        line.setLinePrice(NumberUtils.round(newCount * line.getItemPrice()));
        line.setCount(newCount);

        try{
            String SQL = "UPDATE sales_line "
                    + "SET full_line_price = " + line.getLinePrice() + ", "
                    + "count = " + line.getCount() + " "
                    + "WHERE id = " + line.getId() + ";";

            connection.createStatement().executeUpdate(SQL);
        }catch (SQLException e){
            e.printStackTrace();
        }

        Double newCheckSumm = NumberUtils.round(Double.parseDouble(checkSumm.getText()) + line.getLinePrice() - oldLinePrice);
        checkSumm.setText(String.valueOf(newCheckSumm));
    }

    private void init() {
        salesLinedata = FXCollections.observableArrayList();

        paymentType.setItems(PaymentTypes.getTypes());
        saleType.setItems(SaleTypes.getTypes());

        if(this.header != null){
            checkNumber.setText(this.header.getSalesNumber());
            checkSumm.setText(String.valueOf(this.header.getFullSumm()));

            paymentType.setValue(this.header.getPaymentType());
            saleType.setValue(this.header.getSalesType());

            setListeners();
        }else{
            setListeners();
            clearForm();
        }
    }

    public void setMain(Main main) {
        this.main = main;
    }

    private void insertHeader(){
        try {
            String SQL = "INSERT INTO sales_header SET " +
                    "sales_number = '" + checkNumber.getText() + "', " +
                    "summ = " + Double.parseDouble(checkSumm.getText()) + ", " +
                    "sales_type = '" + saleType.getValue() + "'," +
                    "payment = '" + paymentType.getValue() + "';";

            connection.createStatement().executeUpdate(SQL);

            SQL = "SELECT * FROM sales_header ORDER BY id DESC LIMIT 1";
            ResultSet rs = connection.createStatement().executeQuery(SQL);

            if(rs.next()){
                header = new SalesHeader(
                        rs.getInt("id"),
                        rs.getString("sales_number"),
                        rs.getDouble("cash"),
                        rs.getDouble("non_cash"),
                        rs.getString("sales_type"),
                        rs.getString("payment"),
                        rs.getString("lastcreateupdate")

                );
            }
            this.headerCreated = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateHeader(){
        try{
            String SQL = "UPDATE sales_header "
                    + "SET sales_number = '" + checkNumber.getText() + "', "
                    + "summ = '" + Double.parseDouble(checkSumm.getText()) + "', "
                    + "sales_type = '" + saleType.getValue() + "',"
                    + "payment = '" + paymentType.getValue() + "' "
                    + "WHERE id = " + header.getId() + ";";

            connection.createStatement().executeUpdate(SQL);
            this.headerCreated = true;
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void clearForm() {
        checkNumber.setText("0");
        checkSumm.setText("0");

        paymentType.setValue(PaymentTypes.getTypes().get(0));
        saleType.setValue(SaleTypes.getTypes().get(0));
    }

    private void setListeners(){
        paymentType.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            cash.setText("");
            nonCash.setText("");

            switch (String.valueOf(newValue)){
                case "наличный":
                    cash.setEditable(true);
                    nonCash.setEditable(false);
                    break;
                case "безналичный":
                    cash.setEditable(false);
                    nonCash.setEditable(true);
                    break;
                case "сложная оплата":
                    cash.setEditable(true);
                    nonCash.setEditable(true);
                    break;
            }
        }));
        saleType.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            String newType = NumberUtils.getCheckSuffix(String.valueOf(newValue));
            checkNumber.setText(NumberUtils.getNextCheckNumber(newType));
        }));
    }

    void setHeader(SalesHeader header){
        this.header = header;

        init();
        initTable();
    }
}
