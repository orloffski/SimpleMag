package view;

import application.Main;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import modes.TabMode;

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
			openNewTab(TabMode.CASHBOX);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void openStock(){
		try {
			openNewTab(TabMode.STOCK);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void openFinance(){
		try {
			openNewTab(TabMode.FINANCE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void openCharts(){
		try {
			openNewTab(TabMode.CHARTS);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void openSettings(){
		try {
			openNewTab(TabMode.SETTINGS);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void openNewTab(TabMode mode) throws IOException {
		BorderPane paneView = null;
		FXMLLoader loader = new FXMLLoader();
		String tabTitle = "";
		String tabId = mode.toString();

		if(checkIssetTab(mode))
			return;

		switch (mode){
			case CASHBOX:
				tabTitle = "Продажи";
				loader.setLocation(Main.class.getResource("/view/CashView.fxml"));
				paneView = loader.load();
				break;
			case STOCK:
				tabTitle = "Склад";
				loader.setLocation(Main.class.getResource("/view/StockView.fxml"));
				paneView = loader.load();
				break;
			case FINANCE:
				tabTitle = "Финансы";
				loader.setLocation(Main.class.getResource("/view/FinanceView.fxml"));
				paneView = loader.load();
				break;
			case CHARTS:
				tabTitle = "Статистика";
				loader.setLocation(Main.class.getResource("/view/ChartView.fxml"));
				paneView = loader.load();
				break;
			case SETTINGS:
				tabTitle = "Настройки";
				loader.setLocation(Main.class.getResource("/view/SettingsView.fxml"));
				paneView = loader.load();
				break;
		}

		AbstractRootController controller = loader.getController();
		controller.setMain(this.main);

		Tab tab = new Tab(tabTitle);
		rootTab.getTabs().add(tab);

		tab.setId(tabId);
		tab.setContent(paneView);

		selectionModel.select(tab);
	}

	private boolean checkIssetTab(TabMode mode){
		ObservableList<Tab> tabs = rootTab.getTabs();

		for(Tab tab : tabs){
			if(tab.getId().equals(mode.toString())) {
				selectionModel.select(tab);
				return true;
			}
		}

		return false;
	}

	public void setMain(Main main) {
		this.main = main;
	}
}
