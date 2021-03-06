package view.settings;

import dbhelpers.CounterpartiesDBHelper;
import entity.CounterpartiesEntity;
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
import model.Counterparties;
import utils.MessagesUtils;
import view.AbstractController;

import java.util.List;

public class CounterpartiesDirectoryViewController extends AbstractController{

	private AddEditMode mode;
	private ObservableList<Counterparties> data;
	private Counterparties counterparty;
	
	@FXML
    private TextField filter;
	
	@FXML
	private TableView<Counterparties> counterpartiesTable;
	
	@FXML
    private TableColumn<Counterparties, String> nameColumn;
	
	@FXML
    private TableColumn<Counterparties, String> unnColumn;
	
	@FXML
    private TableColumn<Counterparties, String> adressColumn;
	
	@FXML
    private TextField nameTextField;
	
	@FXML
    private TextField unnTextField;
	
	@FXML
    private TextField adressTextField;
	
	@FXML
	private Button addEditBtn;

	@FXML
	private Button deleteBtn;
	
	@FXML
	private void initialize() {
		getSessionData();

		mode = AddEditMode.ADD;
		
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		unnColumn.setCellValueFactory(cellData -> cellData.getValue().unnProperty());
		adressColumn.setCellValueFactory(cellData -> cellData.getValue().adressProperty());

		nameColumn.setStyle("-fx-padding: 0 0 0 10;");
		unnColumn.setStyle("-fx-padding: 0 0 0 10;");
		adressColumn.setStyle("-fx-padding: 0 0 0 10;");

		buildData();

		counterpartiesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	        if (newSelection != null) {
	        	counterparty = counterpartiesTable.getSelectionModel().getSelectedItem();
	            mode = AddEditMode.EDIT;
	            nameTextField.setText(counterparty.getName());
	            unnTextField.setText(counterparty.getUnn());
	            adressTextField.setText(counterparty.getAdress());
	            addEditBtn.setText("Изменить");
	        }
	    });
	}
	
	@FXML
    private void delete() {
		counterparty = counterpartiesTable.getSelectionModel().getSelectedItem();
		mode = AddEditMode.DELETE;
		
		if(counterparty != null)
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
		if(nameTextField.getText().length() == 0 ||
				unnTextField.getText().length() == 0 ||
				adressTextField.getText().length() == 0){
			MessagesUtils.showAlert("Ошибка", "Нельзя сохранять пустые элементы");
			return;
		}

		if(mode.equals(AddEditMode.ADD)) {
			CounterpartiesDBHelper.saveEntity(sessFact,
					CounterpartiesEntity.createCounterpartiesEntity(0,
							nameTextField.getText(),
							adressTextField.getText(),
							unnTextField.getText()));
		}else if(mode.equals(AddEditMode.EDIT)){
			CounterpartiesDBHelper.updateEntity(sessFact,
					CounterpartiesEntity.createCounterpartiesEntity(counterparty.getId(),
							nameTextField.getText(),
							adressTextField.getText(),
							unnTextField.getText()));
		}else if(mode.equals(AddEditMode.DELETE)){
			CounterpartiesDBHelper.deleteEntity(sessFact,
					CounterpartiesEntity.createCounterpartiesEntity(counterparty.getId(),
							nameTextField.getText(),
							adressTextField.getText(),
							unnTextField.getText()));
		}
		
		mode = AddEditMode.ADD;
		
		data.clear();
		buildData();
		clearForm();
	}
	
	private void buildData(){
		data = FXCollections.observableArrayList();

		List<CounterpartiesEntity> counterpartiesList = CounterpartiesDBHelper.getCounterpartiesEntitiesList(sessFact);

		for (CounterpartiesEntity counterpartyItem : counterpartiesList) {
			Counterparties counterparty = new Counterparties(counterpartyItem.getId(),
					counterpartyItem.getName(),
					counterpartyItem.getAdress(),
					counterpartyItem.getUnn());
			data.add(counterparty);
		}

		counterpartiesTable.setItems(data);

		addFilter();
	}
	
	private void addFilter() {
		FilteredList<Counterparties> filteredData = new FilteredList<>(data, p -> true);
        
        filter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(counterparty -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (counterparty.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches name.
                }
                return false; // Does not match.
            });
        });
        
        SortedList<Counterparties> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(counterpartiesTable.comparatorProperty());
        counterpartiesTable.setItems(sortedData);
	}

	protected void clearForm(){
		addEditBtn.setText("Добавить");

		nameTextField.setText("");
		unnTextField.setText("");
		adressTextField.setText("");
	}
}
