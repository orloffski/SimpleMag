package view;

import java.io.IOException;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class ChartController {

	ChartController() {
	}
	
	@FXML
	private void initialize() {
		
	}
	
	AnchorPane getRootNode() {
		AnchorPane rootView = null;
		
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/ChartView.fxml"));
        try {
        	rootView = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rootView;
	}
}
