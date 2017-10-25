package view.cashviews.sales;

import application.DBClass;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.SalesLine;
import model.Units;
import utils.NumberUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddSaleViewController {

    @FXML
    private Text checkNumber;

    @FXML
    private Text checkSumm;

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


    private Main main;
    private Stage dialogStage;
    private Connection connection;
    private ObservableList<String> salesTypes;
    private ObservableList<String> paymentTypes;
    private ObservableList<SalesLine> salesLinedata;

    @FXML
    private void initialize() {
        try {
            connection = new DBClass().getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        init();
    }

    private void init() {
        salesTypes = FXCollections.observableArrayList();
        salesTypes.add("покупка");
        salesTypes.add("возврат");

        paymentTypes = FXCollections.observableArrayList();
        paymentTypes.add("наличный");
        paymentTypes.add("безналичный");

        salesLinedata = FXCollections.observableArrayList();

        paymentType.setItems(paymentTypes);
        paymentType.setValue(paymentTypes.get(0));

        checkNumber.setText("0");
        checkSumm.setText("0");

        saleType.setItems(salesTypes);
        saleType.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            String newType = NumberUtils.getCheckSuffix(newValue);
            checkNumber.setText(NumberUtils.getNextCheckNumber(newType));
        }));
        saleType.setValue(salesTypes.get(0));
    }

    public void setMain(Main main) {
        this.main = main;
    }

    void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
