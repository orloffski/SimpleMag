package view;

import java.io.IOException;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import modes.TabMode;
import utils.NodeGeneratorUtils;
import view.stockviews.invoices.InvoicesViewController;
import view.stockviews.items.ItemsViewController;

public class StockController extends AbstractRootController{
	
	private Main main;
	
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
		items.setImage(new Image("file:resources/images/goods.png"));
		invoices.setImage(new Image("file:resources/images/invoices.png"));
	}
	
	@FXML
	private void openItemsView(){
		try {
			NodeGeneratorUtils.openNewTab(rootTab, TabMode.ITEMS_LIST, main);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void openInvoicesView(){
		try {
			NodeGeneratorUtils.openNewTab(rootTab, TabMode.INVOICES_LIST, main);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setMain(Main main) {
        this.main = main;
    }
}
