package view.settings.items;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import application.Main;
import dbhelpers.ItemsDBHelper;
import dbhelpers.UnitsDBHelper;
import entity.ItemsEntity;
import entity.UnitsEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Items;
import utils.MessagesUtils;
import view.AbstractController;

public class ItemCardController extends AbstractController{
	
	private Main main;

	private Items item;
	private boolean okClicked = false;
	private boolean newItem = true;
	private ObservableList<String> unitsData;
	private int unitId = -1;
	
	@FXML
	private ComboBox<String> unitComboBox;
	
	@FXML
	private TextField vendorCode; 
	
	@FXML
	private TextField name; 
	
	@FXML
	private TextField vendorCountry; 
	
	@FXML
	private Button barcodeBtn;
	
	@FXML
	private Button saveBtn;

	@FXML
	private void initialize() {
		getSessionData();

		initComboBox();
	}
	
	@FXML
    private void handleCancel() {
        dialogStage.close();
    }
	
	@FXML
    private void handleOK() {
		if(checkItem()) {
			ItemsEntity item;

			if(newItem) {
				item = ItemsEntity.createItemsEntity(0,
						vendorCode.getText().toString(),
						name.getText().toString(),
						new Timestamp(new Date().getTime()),
						vendorCountry.getText().toString(),
						unitId);
				ItemsDBHelper.saveEntity(sessFact, item);
				saveBtn.setDisable(true);
			}else{
				item = ItemsEntity.createItemsEntity(this.item.getId(),
						vendorCode.getText().toString(),
						name.getText().toString(),
						new Timestamp(new Date().getTime()),
						vendorCountry.getText().toString(),
						unitId);
				ItemsDBHelper.updateEntity(sessFact, item);
			}

			this.item = new Items(item.getId(), item.getVendorCode(), item.getName(), item.getVendorCountry(), item.getUnitId());
			okClicked = true;
		}else
			MessagesUtils.showAlert("Ошибка сохранения", "Для сохранения карточки товара заполните все поля");
    }
	
	private void initComboBox() {
		unitsData = FXCollections.observableArrayList();

		List<UnitsEntity> unitsList = UnitsDBHelper.getUnitsEntitiesList(sessFact);

		for(UnitsEntity unitsItem : unitsList){
			unitsData.addAll(unitsItem.getUnit());
		}

		unitComboBox.setItems(unitsData);

		unitComboBox.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
			for(UnitsEntity unitsItem : unitsList)
				if(unitsItem.getUnit().equals(newValue))
					unitId = unitsItem.getId();
		}));
	}
	
	boolean isOkClicked() {
        return okClicked;
    }

	void setItem(Items item) {
		this.item = item;
		
		if(this.item.getId() > 0) {
			newItem = false;
			dialogStage.setTitle("Изменение карточки товара");
			
			vendorCode.setText(item.getVendorCode());
			name.setText(item.getName());
			vendorCountry.setText(item.getVendorCountry());
			unitComboBox.setValue(unitsData.get(item.getUnitId() - 1));
		}else{
			dialogStage.setTitle("Создание карточки товара");
		}
	}
	
	private boolean checkItem() {
		return vendorCode.getText().length() != 0 &&
				name.getText().length() != 0 &&
				vendorCountry.getText().length() != 0 &&
				unitId != -1;

	}
	
	@FXML
	private void openBarcodeTable() {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/settings/items/BarcodesView.fxml"));
        try {
        	BorderPane page = loader.load();
			Stage dialogStage = new Stage();
	        dialogStage.setTitle("Штрихкоды");
	        dialogStage.getIcons().add(new Image("file:resources/images/barcode.png"));
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(main.getPrimaryStage());
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        
	        BarcodesViewController cardController = loader.getController();
	        cardController.setDialogStage(dialogStage);
	        cardController.setItem(item);
	        
	        dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void openPriceView() {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/settings/items/PricesView.fxml"));
        try {
        	BorderPane page = loader.load();
			Stage dialogStage = new Stage();
	        dialogStage.setTitle("Цены");
	        dialogStage.getIcons().add(new Image("file:resources/images/price.png"));
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(main.getPrimaryStage());
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        
	        PricesViewController cardController = loader.getController();
	        cardController.setDialogStage(dialogStage);
	        cardController.setItem(item);
	        
	        dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setMain(Main main) {
        this.main = main;
    }

	@Override
	protected void clearForm() {
		vendorCode.setText("");
		name.setText("");
		vendorCountry.setText("");
		unitComboBox.getSelectionModel().select(0);
	}
}
