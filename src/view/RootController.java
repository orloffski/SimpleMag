package view;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RootController {
	
	@FXML
	private ImageView cashbox;
	
	@FXML
	private ImageView stock;
	
	@FXML
	private ImageView finance;
	
	@FXML
	private ImageView chart;
		
	private Main main;
	
	public RootController() {
	}
	
	@FXML
	private void initialize() {
		
	}
	
	public void setMain(Main main) {
		this.main = main;
	}
	
	@FXML
	private void openCashboxView() {
		System.out.print("cashbox opened");
	}
	
	@FXML
	private void openStockView() {
		System.out.print("stock opened");
	}
	
	@FXML
	private void openFinanceView() {
		System.out.print("finance opened");
	}
	
	@FXML
	private void openChartView() {
		System.out.print("chart opened");
	}

}
