package view.settings.items;

import java.sql.*;
import java.io.IOException;
import java.util.List;

import application.Main;
import dbhelpers.ItemsDBHelper;
import entity.ItemsEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Items;
import utils.MessagesUtils;
import view.AbstractController;

public class ItemsViewController extends AbstractController {
	
	private Main main;
	
	private ObservableList<Items> data;

	private Items item;
	
	@FXML
	private TableView<Items> itemsTable;
	
	@FXML
    private TableColumn<Items, String> vendorCodeColumn;
	
    @FXML
    private TableColumn<Items, String> nameColumn;
    
    @FXML
    private TableColumn<Items, String> vendorCountryColumn;
    
    @FXML
	private ImageView add;
    
    @FXML
	private ImageView edit;
    
    @FXML
	private ImageView delete;
    
    @FXML
    private TextField filter;
	
	@FXML
	private void initialize() {
		getSessionData();

		add.setImage(new Image("file:resources/images/add.png"));
		edit.setImage(new Image("file:resources/images/edit.png"));
		delete.setImage(new Image("file:resources/images/delete.png"));
		
		vendorCodeColumn.setCellValueFactory(cellData -> cellData.getValue().vendorCodeProperty());
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		vendorCountryColumn.setCellValueFactory(cellData -> cellData.getValue().vendorCountryProperty());

		buildData();
	}
	
	@FXML
	private void addItem() {
		item = new Items(0, null, null, null, 0);
		
		boolean okClicked = openAddEditItemDialog(item);
		if(okClicked) {
			data.clear();
			buildData();
		}
	}
	
	@FXML
	private void editItem() {
		item = itemsTable.getSelectionModel().getSelectedItem();
		
		if(item != null) {
			boolean okClicked = openAddEditItemDialog(item);
			if(okClicked) {
				data.clear();
				buildData();
			}
		}else
			MessagesUtils.showAlert("Ошибка редактирования", "Для редактирования карточки товара выберите товар из списка");
		
	}
	
	@FXML
	private void deleteItem() {
		item = itemsTable.getSelectionModel().getSelectedItem();
		
		if(item != null) {
			ItemsDBHelper.deleteEntity(sessFact,
					ItemsEntity.createItemsEntity(item.getId(),
							"",
							"",
							new Timestamp(0),
							"",
							0));
			data.clear();
			buildData();
		}else
			MessagesUtils.showAlert("Ошибка удаления", "Для удаления выберите карточку товара из списка");
	}
	
	public void buildData(){
		data = FXCollections.observableArrayList();

		List<ItemsEntity> itemsList = ItemsDBHelper.getItemsEntitiesList(sessFact);

		for(ItemsEntity itemsItem : itemsList){
			Items item = new Items(itemsItem.getId(),
					itemsItem.getVendorCode(),
					itemsItem.getName(),
					itemsItem.getVendorCountry(),
					itemsItem.getUnitId());

			data.add(item);
		}

		itemsTable.setItems(data);

		addFilter();
	}
	
	public void addFilter() {
		FilteredList<Items> filteredData = new FilteredList<>(data, p -> true);
        
        filter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (item.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches name.
                } else if (item.getVendorCode().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches vendor code.
                }
                return false; // Does not match.
            });
        });
        
        SortedList<Items> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(itemsTable.comparatorProperty());
        itemsTable.setItems(sortedData);
	}
	
	private boolean openAddEditItemDialog(Items item) {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/settings/items/ItemCard.fxml"));
        try {
			AnchorPane page = loader.load();
			Stage dialogStage = new Stage();
	        dialogStage.getIcons().add(new Image("file:resources/images/goods.png"));
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(main.getPrimaryStage());
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        
	        ItemCardController controller = loader.getController();
	        controller.setMain(main);
	        controller.setDialogStage(dialogStage);
	        controller.setItem(item);
	        
	        dialogStage.showAndWait();
	        
	        return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return false;
	}
	
	public void setMain(Main main) {
        this.main = main;
    }

	@Override
	protected void clearForm() {

	}
}
