package view;

import java.io.IOException;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import view.stockviews.invoices.InvoicesViewController;
import view.stockviews.items.ItemsViewController;
import view.stockviews.settings.SettingsViewController;

public class StockController {
	
	private Main main;
	
	@FXML
    private BorderPane stockRootLayout;
	
	@FXML
	private ImageView items;
	
	@FXML
	private ImageView invoices;
	
	@FXML
	private ImageView settings;
	
	public StockController() {
	}
	
	@FXML
	private void initialize() {
		
	}
	
	@FXML
	private void openItemsView(){
		BorderPane paneView = null;
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/stockviews/items/ItemsView.fxml"));
        try {
        	paneView = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        ItemsViewController controller = loader.getController();
		controller.setMain(main);
        stockRootLayout.setCenter(paneView);
	}
	
	@FXML
	private void openInvoicesView(){
		BorderPane paneView = null;
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/stockviews/invoices/InvoicesView.fxml"));
        try {
        	paneView = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        InvoicesViewController controller = loader.getController();
		controller.setMain(main);
        stockRootLayout.setCenter(paneView);
	}
	
	@FXML
	private void openSettingsView(){
		BorderPane paneView = null;
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/stockviews/settings/SettingsView.fxml"));
        try {
        	paneView = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        SettingsViewController controller = loader.getController();
		controller.setMain(main);
        stockRootLayout.setCenter(paneView);
	}
	
	public AnchorPane getRootNode() {
		return getLoadedPane("/view/StockView.fxml");
	}
	
	private AnchorPane getLoadedPane(String viewLocation) {
		AnchorPane paneView = null;
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource(viewLocation));
        try {
        	paneView = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return paneView;
	}
	
	public void setMain(Main main) {
        this.main = main;
    }
}
