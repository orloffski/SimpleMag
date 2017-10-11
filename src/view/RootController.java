package view;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

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
	
	private void biggerImage(ImageView image) {
		final double SCALE = 1.3; // коэффициент увеличения
	    final double DURATION = 300; // время анимации в мс

	    // создаём анимацию увеличения картинки     
	    final ScaleTransition animationGrow = 
	    		new ScaleTransition(Duration.millis(DURATION), image);
	    animationGrow.setToX(SCALE);
	    animationGrow.setToY(SCALE);

	    // и уменьшения
	    final ScaleTransition animationShrink = 
	    		new ScaleTransition(Duration.millis(DURATION), image);
	    animationShrink.setToX(1);
	    animationShrink.setToY(1);
	    
	    image.toFront();
	    
	    animationShrink.stop();
        animationGrow.playFromStart();
	}
	
	private void smallerImage(ImageView image) {
		final double SCALE = 1.3; // коэффициент увеличения
	    final double DURATION = 300; // время анимации в мс

	    // создаём анимацию увеличения картинки     
	    final ScaleTransition animationGrow = 
	    		new ScaleTransition(Duration.millis(DURATION), image);
	    animationGrow.setToX(SCALE);
	    animationGrow.setToY(SCALE);

	    // и уменьшения
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
	
	
}
