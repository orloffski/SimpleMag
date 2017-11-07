package view.settings.items;

import dbhelpers.BarcodesDBHelper;
import entity.BarcodesEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modes.AddEditMode;
import model.Barcodes;
import model.Items;
import org.hibernate.query.Query;
import utils.MessagesUtils;
import view.AbstractController;

import java.util.List;

public class BarcodesViewController extends AbstractController{
	
	private ObservableList<Barcodes> data;
	private static int itemId;
	private AddEditMode mode;
	private int barcodeId;
	
	@FXML
	private Label itemLabel;
	
	@FXML
	private Button addBtn;
	
	@FXML
	private Button deleteBtn;
	
	@FXML
	private Button closeBtn;
	
	@FXML
	private TextField barcodeAddField;
	
	@FXML
	private TableView<Barcodes> barcodesTable;
	
	@FXML
    private TableColumn<Barcodes, String> barcodesColumn;

	@FXML
	private void initialize() {
		getSessionData();

		mode = AddEditMode.ADD;

		barcodesColumn.setCellValueFactory(cellData -> cellData.getValue().barcodeProperty());
	}
	
	public void buildData(int id){
		data = FXCollections.observableArrayList();

		List<BarcodesEntity> barcodesList = BarcodesDBHelper.getBarcodesByItemId(sessFact, id);

		for (BarcodesEntity barcodeItem : barcodesList) {
			Barcodes barcodes = new Barcodes(barcodeItem.getId(),
					barcodeItem.getBarcode(),
					barcodeItem.getItemId());
			data.add(barcodes);
		}

		barcodesTable.setItems(data);
	}
	
	public void setItem(Items item) {
		itemLabel.setText(item.getVendorCode().toString() + " " + item.getName().toString());
		itemId = item.getId();
		buildData(item.getId());
	}
	
	public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
	
	@FXML
	private void deleteBarcode() {
		Barcodes barcode = barcodesTable.getSelectionModel().getSelectedItem();

		if(barcode != null) {
			barcodeId = barcode.getId();
			mode = AddEditMode.DELETE;
			addDeleteBarcode();
		}else
			MessagesUtils.showAlert("Не выбран штрихкод для удаления", "Для удаления выберите штрихкод из списка");
	}
	
	@FXML
	private void addDeleteBarcode() {
		if(mode.equals(AddEditMode.ADD)) {
			if (barcodeAddField.getText().toString().length() != 0) {
				BarcodesDBHelper.saveEntity(sessFact, createBarcodesEntity(0));
				barcodeAddField.setText("");
			} else
				MessagesUtils.showAlert("Ошибка создания штрихкода", "Нельзя добавлять пустой штрихкод");
		}else if(mode.equals(AddEditMode.DELETE)){
			BarcodesDBHelper.deleteEntity(sessFact, createBarcodesEntity(barcodeId));
		}

		mode = AddEditMode.ADD;

		data.clear();
		buildData(itemId);
	}
	
	@FXML
	private void close() {
		dialogStage.close();
	}

	@Override
	protected void clearForm() {

	}

	private BarcodesEntity createBarcodesEntity(int id){
		return new BarcodesEntity(id, barcodeAddField.getText().toString(), itemId);
	}
}
