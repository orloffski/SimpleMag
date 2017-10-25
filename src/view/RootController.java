package view;

import java.io.IOException;

import application.Main;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class RootController {
	
	private Main main;
	
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
		cashbox.setImage(new Image("file:resources/images/cashbox.png"));
		stock.setImage(new Image("file:resources/images/stock.png"));
		finance.setImage(new Image("file:resources/images/finance.png"));
		chart.setImage(new Image("file:resources/images/chart.png"));
	}
	
	@FXML
	private void openCashboxView(){
		BorderPane paneView = null;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/view/CashView.fxml"));
		try {
			paneView = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		CashViewController controller = loader.getController();
		controller.setMain(main);
		rootLayout.setCenter(paneView);
	}
	
	@FXML
	private void openStockView() {
		AnchorPane paneView = null;
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/StockView.fxml"));
        try {
			paneView = (AnchorPane) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		StockController controller = loader.getController();
		controller.setMain(main);
		rootLayout.setCenter(paneView);
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
	
	private void biggerImage(ImageView image) {
		final double SCALE = 1.3;
	    final double DURATION = 300;

	    final ScaleTransition animationGrow = 
	    		new ScaleTransition(Duration.millis(DURATION), image);
	    animationGrow.setToX(SCALE);
	    animationGrow.setToY(SCALE);

	    final ScaleTransition animationShrink = 
	    		new ScaleTransition(Duration.millis(DURATION), image);
	    animationShrink.setToX(1);
	    animationShrink.setToY(1);
	    
	    image.toFront();
	    
	    animationShrink.stop();
        animationGrow.playFromStart();
	}
	
	private void smallerImage(ImageView image) {
		final double SCALE = 1.3;
	    final double DURATION = 300;

	    final ScaleTransition animationGrow = 
	    		new ScaleTransition(Duration.millis(DURATION), image);
	    animationGrow.setToX(SCALE);
	    animationGrow.setToY(SCALE);

	    final ScaleTransition animationShrink = 
	    		new ScaleTransition(Duration.millis(DURATION), image);
	    animationShrink.setToX(1);
	    animationShrink.setToY(1);
	    
	    animationGrow.stop();
        animationShrink.playFromStart();
	}
	
	@FXML
	private void cashboxImageEntered() {
		biggerImage(cashbox);
	}
	
	@FXML
	private void cashboxImageExited() {
		smallerImage(cashbox);
	}
	
	@FXML
	private void chartImageEntered() {
		biggerImage(chart);
	}
	
	@FXML
	private void chartImageExited() {
		smallerImage(chart);
	}
	
	@FXML
	private void financeImageEntered() {
		biggerImage(finance);
	}
	
	@FXML
	private void financeImageExited() {
		smallerImage(finance);
	}
	
	@FXML
	private void stockImageEntered() {
		biggerImage(stock);
	}
	
	@FXML
	private void stockImageExited() {
		smallerImage(stock);
	}
	
	public void setMain(Main main) {
        this.main = main;
    }
}
