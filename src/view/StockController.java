package view;

import java.io.IOException;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class StockController {
	
	@FXML
    private BorderPane stockRootLayout;
	
	@FXML
	private ImageView items;
	
	@FXML
	private ImageView receipts;
	
	@FXML
	private ImageView returns;
	
	@FXML
	private ImageView transfers;
	
	public StockController() {
	}
	
	@FXML
	private void initialize() {
		
	}
	
	@FXML
	private void openItemsView(){
        stockRootLayout.setCenter(getLoadedPane("/view/stockviews/ItemsView.fxml"));
	}
	
	@FXML
	private void openReceiptsView(){
        stockRootLayout.setCenter(getLoadedPane("/view/stockviews/ReceiptsView.fxml"));
	}
	
	@FXML
	private void openReturnsView(){
        stockRootLayout.setCenter(getLoadedPane("/view/stockviews/ReturnsView.fxml"));
	}
	
	@FXML
	private void openTransfersView(){
        stockRootLayout.setCenter(getLoadedPane("/view/stockviews/TransfersView.fxml"));
	}
	
	public AnchorPane getRootNode() {
		return getLoadedPane("/view/StockView.fxml");
	}
	
	public AnchorPane getLoadedPane(String viewLocation) {
		AnchorPane paneView = null;
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource(viewLocation));
        try {
        	paneView = (AnchorPane) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return paneView;
	}
}
