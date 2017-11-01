package view.stockviews.settings;

import entity.UnitsEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.AddEditMode;
import model.Units;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utils.HibernateUtil;
import utils.MessagesUtils;

import java.util.List;

public class UnitsDirectoryViewController {

	private Stage dialogStage;
	
	private ObservableList<Units> data;
	private AddEditMode mode;
	private Units unit;

	private Session session;
	private SessionFactory sessFact;
	private org.hibernate.Transaction tr;
	
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
	private Button closeBtn;

	@FXML
	private void initialize() {
		getSessionData();

		mode = AddEditMode.ADD;
		
        unitColumn.setCellValueFactory(cellData -> cellData.getValue().unitProperty());

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
    private void close() {
        dialogStage.close();
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
		if(addEditUnit.getText().toString().length() == 0){
			MessagesUtils.showAlert("Ошибка", "Нельзя сохранять пустые элементы");
			return;
		}

		session = sessFact.openSession();
		tr = session.beginTransaction();

		if(mode.equals(AddEditMode.ADD)) {
			session.save(createUnitsEntity(0));
		}else if(mode.equals(AddEditMode.EDIT)){
			session.update(createUnitsEntity(unit.getId()));
		}else if(mode.equals(AddEditMode.DELETE)){
			session.delete(createUnitsEntity(unit.getId()));
		}

		tr.commit();
		session.close();
		
		mode = AddEditMode.ADD;
		
		data.clear();
		buildData();
		clearForm();
	}
	
	private void buildData(){
		data = FXCollections.observableArrayList();

		session = sessFact.openSession();
		tr = session.beginTransaction();

		List<UnitsEntity> unitsList = session.createCriteria(UnitsEntity.class).list();

		for (UnitsEntity unitItem : unitsList) {
			Units unit = new Units(unitItem.getId(),
	            		unitItem.getUnit());
			data.add(unit);
		}

		tr.commit();
		session.close();

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
	
	void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;      
    }

    private void getSessionData(){
		sessFact = HibernateUtil.getSessionFactory();
	}

	private void clearForm(){
		addEditBtn.setText("Добавить");
		addEditUnit.setText("");
	}

	private UnitsEntity createUnitsEntity(int id){
		return new UnitsEntity(id, addEditUnit.getText().toString());
	}
}
