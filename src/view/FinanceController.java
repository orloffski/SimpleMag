package view;

import java.io.IOException;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class FinanceController {
	
	public FinanceController() {
	}
	
	@FXML
	private void initialize() {
		
	}
	
	public AnchorPane getRootNode() {
		AnchorPane rootView = null;
		
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/FinanceView.fxml"));
        try {
        	rootView = (AnchorPane) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rootView;
	}

}
