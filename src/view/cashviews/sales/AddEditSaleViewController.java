package view.cashviews.sales;

import application.Main;
import dbhelpers.SalesHeaderDBHelper;
import dbhelpers.SalesLinesDBHelper;
import entity.SalesHeaderEntity;
import entity.SalesLineEntity;
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
import utils.MessagesUtils;
import utils.NumberUtils;
import view.AbstractController;
import view.stockviews.BarcodeItemsViewController;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class AddEditSaleViewController extends AbstractController {

    @FXML
    private Text checkNumber;

    @FXML
    private Text checkSumm;

    @FXML
    private Text setDocText;

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
    private Button setDoc;

    @FXML
    private Button save;


    private Main main;
    private ObservableList<SalesLine> salesLinedata;
    private SalesHeader header;

    @FXML
    private void initialize() {
        getSessionData();

    	add.setImage(new Image("file:resources/images/add.png"));
    	delete.setImage(new Image("file:resources/images/delete.png"));
    }

    @FXML
    private void saveSale(){
        if(this.header == null){
            this.header = new SalesHeader(
                    0,
                    NumberUtils.getNextCheckNumber(saleType.getValue()),
                    cash.getText().equals("") ? 0d : Double.parseDouble(cash.getText()),
                    nonCash.getText().equals("") ? 0d : Double.parseDouble(nonCash.getText()),
                    saleType.getValue(),
                    paymentType.getValue(),
                    String.valueOf(new Timestamp(new Date().getTime())),
                    setDocText.getText()
            );

            SalesHeaderDBHelper.saveEntity(sessFact, SalesHeaderEntity.createSalesHeaderEntityFromSalesHeader(this.header));
        }else{
            if(this.header.getSetHeader().equals("проведен")){
                MessagesUtils.showAlert("Ошибка изменения чека",
                        "Для изменения чека отмените проведение.");
                return;
            }

            this.header.setSalesNumber(checkNumber.getText());
            this.header.setSalesType(saleType.getValue());
            this.header.setPaymentType(paymentType.getValue());
            this.header.setCash(cash.getText().equals("") ? 0d : Double.parseDouble(cash.getText()));
            this.header.setNonCash(nonCash.getText().equals("") ? 0d : Double.parseDouble(nonCash.getText()));

            System.out.println(this.header.getId());
            SalesHeaderDBHelper.updateEntity(sessFact, SalesHeaderEntity.createSalesHeaderEntityFromSalesHeader(this.header));
        }

        save.setDisable(true);
        setDoc.setDisable(false);
    }

    @FXML
    private void setDoc(){
        if(this.header == null){
            MessagesUtils.showAlert("Ошибка проведения чека",
                    "Для проведения чека сохраните его.");
            return;
        }

        this.header.setSetHeader(this.header.getSetHeader().toLowerCase().equals("проведен") ? "не проведен" : "проведен");
        setDocText.setText(this.header.getSetHeader().toLowerCase().equals("проведен") ? "проведен" : "не проведен");
        setDoc.setText(this.header.getSetHeader().toLowerCase().equals("проведен") ? "отмена проведения" : "проведение");

        SalesHeaderDBHelper.updateEntity(sessFact, SalesHeaderEntity.createSalesHeaderEntityFromSalesHeader(this.header));
    }

    @FXML
    private void addLine(){

    }

    @FXML
    private void deleteLine(){

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

    private void init() {
        salesLinedata = FXCollections.observableArrayList();

        paymentType.setItems(PaymentTypes.getTypes());
        saleType.setItems(SaleTypes.getTypes());

        if(this.header != null){
            checkNumber.setText(this.header.getSalesNumber());
            checkSumm.setText(String.valueOf(this.header.getFullSumm()));

            setDocText.setText(this.header.getSetHeader());
            setDoc.setText(this.header.getSetHeader().toLowerCase().equals("проведен")?"отмена проведения":"проведение");

            paymentType.setValue(this.header.getPaymentType());
            saleType.setValue(this.header.getSalesType());

            save.setDisable(true);

            setListeners();
        }else{
            setDocText.setText("не проведен");

            setDoc.setDisable(true);
            setDoc.setText("проведение");

            setListeners();
            clearForm();
        }
    }

    public void setMain(Main main) {
        this.main = main;
    }

    @Override
    protected void clearForm() {
        checkNumber.setText("0");
        checkSumm.setText("0");

        paymentType.setValue(PaymentTypes.getTypes().get(0));
        saleType.setValue(SaleTypes.getTypes().get(0));
    }

    private boolean checkHeader(SalesHeader header){
        if(header.getSetHeader().equals("проведен")){
            MessagesUtils.showAlert("Ошибка изменения чека",
                    "Для изменения чека отмените проведение.");
            return true;
        }

        return false;
    }

    private void setListeners(){
        paymentType.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if(checkHeader(this.header))
                return;

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

            save.setDisable(false);
            setDoc.setDisable(true);
        }));
        saleType.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if(checkHeader(this.header))
                return;

            checkNumber.setText(NumberUtils.getNextCheckNumber(String.valueOf(newValue)));

            save.setDisable(false);
            setDoc.setDisable(true);
        }));
    }

    void setHeader(SalesHeader header){
        this.header = header;

        init();
        initTable();
    }

    private void updateLine(int newCount){

        SalesLine line = salesLineTable.getSelectionModel().getSelectedItem();
        Double oldLinePrice = line.getLinePrice();
        line.setLinePrice(NumberUtils.round(newCount * line.getItemPrice()));
        line.setCount(newCount);

//        try{
//            String SQL = "UPDATE sales_line "
//                    + "SET full_line_price = " + line.getLinePrice() + ", "
//                    + "count = " + line.getCount() + " "
//                    + "WHERE id = " + line.getId() + ";";
//
//            connection.createStatement().executeUpdate(SQL);
//        }catch (SQLException e){
//            e.printStackTrace();
//        }

        Double newCheckSumm = NumberUtils.round(Double.parseDouble(checkSumm.getText()) + line.getLinePrice() - oldLinePrice);
        checkSumm.setText(String.valueOf(newCheckSumm));
    }

    /*
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
                        rs.getString("lastcreateupdate"),
                        rs.getString("set_header")

                );
            }
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
        }catch (SQLException e){
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
    */
}
