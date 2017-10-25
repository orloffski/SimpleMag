package view;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import view.cashviews.sales.SalesViewController;

import java.io.IOException;

public class CashViewController {

    private Main main;

    @FXML
    private BorderPane cashRootLayout;

    public CashViewController() {
    }

    @FXML
    private void initialize() {
    }

    private void openSalesView(){
        BorderPane paneView = null;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/cashviews/sales/SalesView.fxml"));
        try {
            paneView = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SalesViewController controller = loader.getController();
        controller.setMain(main);
        cashRootLayout.setCenter(paneView);
    }

    public void setMain(Main main) {
        this.main = main;
        openSalesView();
    }
}