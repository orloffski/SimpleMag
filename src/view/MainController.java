package view;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainController {
	private Main main;

	@FXML
	private TabPane rootTab;

	@FXML
	private ImageView cashbox;

	@FXML
	private ImageView stock;

	@FXML
	private ImageView finance;

	@FXML
	private ImageView chart;

	@FXML
	private ImageView settings;
		
	public MainController() {
		
	}
	
	@FXML
	private void initialize() {
		cashbox.setImage(new Image("file:resources/images/cashbox.png"));
		stock.setImage(new Image("file:resources/images/stock.png"));
		finance.setImage(new Image("file:resources/images/finance.png"));
		chart.setImage(new Image("file:resources/images/chart.png"));
		settings.setImage(new Image("file:resources/images/settings.png"));
	}

	public void setMain(Main main) {
		this.main = main;
	}
}
