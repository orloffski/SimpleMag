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
import reports.ItemsInStockReport;
import utils.HibernateUtil;
import utils.MessagesUtils;

import java.awt.*;
import java.io.File;
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
			ItemsInStockData.add(
					ItemsInStock.createItemsInStockFromItemsInStockEntity(itemsInStockEntity));
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

		new ItemsInStockReport(HibernateUtil.getSessionFactory());

		loadData();
		loadTableView();
	}

	@FXML
	private void openItemsInStock(){
		ItemsInStock itemsInStock = ItemsInStockTable.getSelectionModel().getSelectedItem();

		if(itemsInStock != null){
			StringBuilder filename =
					new StringBuilder("../items_in_stock/")
							.append(itemsInStock.getDate())
							.append(".xls");
			try {
				Desktop.getDesktop().open(new File(filename.toString()));
			} catch (IOException e) {
				MessagesUtils.showAlert("Ошибка открытия отчета",
						"Для открытия отчета необходимо наличие установленного MicroSoft Excel либо аналога!");
			}
		}else
			MessagesUtils.showAlert("Ошибка открытия отчета",
					"Для открытия отчета выберите дату отчета из списка!");
	}

	@Override
	protected void clearForm() {

	}
}
