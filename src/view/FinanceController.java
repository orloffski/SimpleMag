package view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import application.Main;
import dbhelpers.ProductsInStockDBHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import model.ItemsCount;
import model.ItemsInStock;
import utils.HibernateUtil;
import utils.ItemsInStockUtils;

public class FinanceController extends AbstractRootController{

	@FXML
	private Button button;
	
	public FinanceController() {
	}
	
	@FXML
	private void initialize() {
		
	}
	
	AnchorPane getRootNode() {
		AnchorPane rootView = null;
		
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/FinanceView.fxml"));
        try {
        	rootView = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rootView;
	}

	@FXML
	private void test_button(){
		ItemsInStockUtils.getItemsInStock(HibernateUtil.getSessionFactory());

	}
}
