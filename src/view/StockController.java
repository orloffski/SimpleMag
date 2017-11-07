package view;

import java.io.IOException;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import modes.TabMode;
import utils.NodeGeneratorUtils;

public class StockController extends AbstractRootController{
	
	private Main main;

	@FXML
	private Text invoicesList;
	
	public StockController() {
	}
	
	@FXML
	private void initialize() {
	}
	
	@FXML
	private void openInvoicesView(){
		try {
			NodeGeneratorUtils.openNewTab(rootTab, TabMode.INVOICES_LIST, main);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void setInvoicesTextUnderline(){
		invoicesList.setUnderline(true);
	}

	@FXML
	private void removeInvoicesTextUnderline(){
		invoicesList.setUnderline(false);
	}
	
	public void setMain(Main main) {
        this.main = main;
    }
}
