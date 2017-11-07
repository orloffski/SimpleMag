package view.settings;

import dbhelpers.UnitsDBHelper;
import entity.UnitsEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import modes.AddEditMode;
import model.Units;
import utils.MessagesUtils;
import view.AbstractController;

import java.util.List;

public class UnitsDirectoryViewController extends AbstractController{

	private ObservableList<Units> data;
	private AddEditMode mode;
	private Units unit;
	
	@FXML
	private TableView<Units> unitsTable;
	
	@FXML
    private TableColumn<Units, String> unitColumn;
	
	@FXML
    private TextField filter;
	
	@FXML
    private TextField addEditUnit;
	
	@FXML
	private Button addEditBtn;
	
	@FXML
	private Button deleteBtn;

	@FXML
	private void initialize() {
		getSessionData();

		mode = AddEditMode.ADD;
		
        unitColumn.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
        unitColumn.setStyle("-fx-padding: 0 0 0 10;");

		buildData();

		unitsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	        if (newSelection != null) {
	            unit = unitsTable.getSelectionModel().getSelectedItem();
	            mode = AddEditMode.EDIT;
	            addEditUnit.setText(unit.getUnit());
	            addEditBtn.setText("Изменить");
	        }
	    });
	}
	
	@FXML
    private void delete() {
		Units unit = unitsTable.getSelectionModel().getSelectedItem();
		mode = AddEditMode.DELETE;
		
		if(unit != null)
			addEditDelete();
		else
			MessagesUtils.showAlert("Ошибка удаления", "Для удаления выберите элемент из списка");
		
		mode = AddEditMode.ADD;

		data.clear();
		buildData();
		clearForm();
    }
	
	@FXML
	private void addEditDelete() {
		if(addEditUnit.getText().length() == 0){
			MessagesUtils.showAlert("Ошибка", "Нельзя сохранять пустые элементы");
			return;
		}

		if(mode.equals(AddEditMode.ADD)) {
			UnitsDBHelper.saveUnit(sessFact,
					UnitsEntity.createUnitsEntity(0, addEditUnit.getText())
			);
		}else if(mode.equals(AddEditMode.EDIT)){
			UnitsDBHelper.updateUnit(sessFact,
					UnitsEntity.createUnitsEntity(unit.getId(), addEditUnit.getText())
			);
		}else if(mode.equals(AddEditMode.DELETE)){
			UnitsDBHelper.deleteUnit(sessFact,
					UnitsEntity.createUnitsEntity(unit.getId(), addEditUnit.getText())
			);
		}
		
		mode = AddEditMode.ADD;
		
		data.clear();
		buildData();
		clearForm();
	}
	
	private void buildData(){
		data = FXCollections.observableArrayList();

		List<UnitsEntity> unitsList = UnitsDBHelper.getUnitsEntitiesList(sessFact);

		for (UnitsEntity unitItem : unitsList) {
			Units unit = new Units(unitItem.getId(),
	            		unitItem.getUnit());
			data.add(unit);
		}

		unitsTable.setItems(data);

		addFilter();
	}
	
	private void addFilter() {
		FilteredList<Units> filteredData = new FilteredList<>(data, p -> true);
        
        filter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(unit -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (unit.getUnit().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches name.
                }
                return false; // Does not match.
            });
        });
        
        SortedList<Units> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(unitsTable.comparatorProperty());
        unitsTable.setItems(sortedData);
	}

	protected void clearForm(){
		addEditBtn.setText("Добавить");
		addEditUnit.setText("");
	}
}
