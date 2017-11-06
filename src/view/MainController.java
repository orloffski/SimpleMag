package view;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modes.TabMode;
import utils.NodeGeneratorUtils;

import java.io.IOException;

public class MainController {

	private Main main;
	private SingleSelectionModel<Tab> selectionModel;

	@FXML
	private TabPane rootTab;

	@FXML
	private Tab mainTab;

	@FXML
	private ImageView cashbox;

	@FXML
	private ImageView stock;

	@FXML
	private ImageView finance;

	@FXML
	private ImageView chart;

	@FXML
	private ImageView settings;
		
	public MainController() {
		
	}
	
	@FXML
	private void initialize() {
		cashbox.setImage(new Image("file:resources/images/cashbox.png"));
		stock.setImage(new Image("file:resources/images/stock.png"));
		finance.setImage(new Image("file:resources/images/finance.png"));
		chart.setImage(new Image("file:resources/images/chart.png"));
		settings.setImage(new Image("file:resources/images/settings.png"));

		selectionModel = rootTab.getSelectionModel();
	}

	@FXML
	private void openCashBox(){
		try {
			NodeGeneratorUtils.openNewTab(rootTab, TabMode.CASHBOX, main);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void openStock(){
		try {
			NodeGeneratorUtils.openNewTab(rootTab, TabMode.STOCK, main);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void openFinance(){
		try {
			NodeGeneratorUtils.openNewTab(rootTab, TabMode.FINANCE, main);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void openCharts(){
		try {
			NodeGeneratorUtils.openNewTab(rootTab, TabMode.CHARTS, main);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void openSettings(){
		try {
			NodeGeneratorUtils.openNewTab(rootTab, TabMode.SETTINGS, main);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setMain(Main main) {
		this.main = main;
	}
}
