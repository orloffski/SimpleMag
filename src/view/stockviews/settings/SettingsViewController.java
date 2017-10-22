package view.stockviews.settings;

import java.io.IOException;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SettingsViewController {

	private Main main;
	
	@FXML
	private Button unitsBtn;
	
	@FXML
	private Button counterpartiesBtn;

	@FXML
	private void initialize() {
	}
	
	@FXML
	private void openUnits() {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/stockviews/settings/UnitsDirectoryView.fxml"));
        try {
        	BorderPane page = loader.load();
			Stage dialogStage = new Stage();
	        dialogStage.setTitle("Единицы измерения");
	        dialogStage.getIcons().add(new Image("file:resources/images/goods.png"));
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(main.getPrimaryStage());
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        
	        UnitsDirectoryViewController unitController = loader.getController();
	        unitController.setDialogStage(dialogStage);
	        
	        dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void openCounterparties() {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/stockviews/settings/CounterpartiesDirectoryView.fxml"));
        try {
        	BorderPane page = loader.load();
			Stage dialogStage = new Stage();
	        dialogStage.setTitle("Контрагенты");
	        dialogStage.getIcons().add(new Image("file:resources/images/counterparties.png"));
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(main.getPrimaryStage());
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        
	        CounterpartiesDirectoryViewController counterpartiesController = loader.getController();
	        counterpartiesController.setDialogStage(dialogStage);
	        
	        dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setMain(Main main) {
        this.main = main;
    }
}
