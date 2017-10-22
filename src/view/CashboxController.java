package view;

import java.io.IOException;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class CashboxController {
	
	CashboxController() {
	}
	
	@FXML
	private void initialize() {
		
	}
	
	AnchorPane getRootNode() {
		AnchorPane rootView = null;
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/CashboxView.fxml"));
        try {
        	rootView = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rootView;
	}

}