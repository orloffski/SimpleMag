package view.cashviews.sales;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.SalesLine;

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

    public void setMain(Main main) {
        this.main = main;
    }

    void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
