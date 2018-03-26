package view;

import application.Main;
import dbhelpers.ItemsInStockDBHelper;
import entity.ItemsInStockEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import model.ItemsInStock;
import utils.HibernateUtil;
import reports.ItemsInStockUtils;

import java.io.IOException;

public class FinanceController extends AbstractController{

	private ObservableList<ItemsInStock> ItemsInStockData;

	@FXML
	private Button checkItemsInStock;

	@FXML
	private Button openItemsInStock;

	@FXML
	private TableView<ItemsInStock> ItemsInStockTable;

	@FXML
	private TableColumn<ItemsInStock, String> DateColumn;
	
	public FinanceController() {
	}
	
	@FXML
	private void initialize() {
		getSessionData();

		loadData();
		loadTableView();
	}

	private void loadData(){
		ItemsInStockData = FXCollections.observableArrayList();

		for(ItemsInStockEntity itemsInStockEntity : ItemsInStockDBHelper.getAll(sessFact))
			ItemsInStockData.add(ItemsInStock.createItemsInStockFromItemsInStockEntity(itemsInStockEntity));
	}

	private void loadTableView(){
		ItemsInStockTable.setEditable(false);

		DateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

		ItemsInStockTable.setItems(ItemsInStockData);
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
	private void getItemsInStock(){
		ItemsInStockData.clear();

		ItemsInStockUtils.getItemsInStock(HibernateUtil.getSessionFactory());

		loadData();
		loadTableView();
	}

	@Override
	protected void clearForm() {

	}
}
