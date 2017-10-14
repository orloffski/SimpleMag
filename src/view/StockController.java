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
	private ImageView invoices;
	
	public StockController() {
	}
	
	@FXML
	private void initialize() {
		
	}
	
	@FXML
	private void openItemsView(){
		BorderPane paneView = null;
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/stockviews/ItemsView.fxml"));
        try {
        	paneView = (BorderPane) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        stockRootLayout.setCenter(paneView);
	}
	
	@FXML
	private void openInvoicesView(){
        stockRootLayout.setCenter(getLoadedPane("/view/stockviews/InvoicesView.fxml"));
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
