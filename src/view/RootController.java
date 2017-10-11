package view;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class RootController {
	
	@FXML
    private BorderPane rootLayout;
	
	@FXML
	private ImageView cashbox;
	
	@FXML
	private ImageView stock;
	
	@FXML
	private ImageView finance;
	
	@FXML
	private ImageView chart;
	
	public RootController() {
	}
	
	@FXML
	private void initialize() {
		
	}
	
	@FXML
	private void openCashboxView(){
		CashboxController controller = new CashboxController();
		rootLayout.setCenter(controller.getRootNode());
	}
	
	@FXML
	private void openStockView() {
		StockController controller = new StockController();
		rootLayout.setCenter(controller.getRootNode());
	}
	
	@FXML
	private void openFinanceView() {
		FinanceController controller = new FinanceController();
		rootLayout.setCenter(controller.getRootNode());
	}
	
	@FXML
	private void openChartView() {
		ChartController controller = new ChartController();
		rootLayout.setCenter(controller.getRootNode());
	}

}
