package view.stockviews.settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import application.DBClass;
import application.HibernateSession;
import entity.UnitsEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.AddEditMode;
import model.Units;
import org.hibernate.Cache;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utils.HibernateUtil;

public class UnitsDirectoryViewController {

	private Stage dialogStage;
	
	private ObservableList<Units> data;
	private DBClass dbClass;
	private Connection connection;
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
        
		dbClass = new DBClass();
	    try{
	    	connection = dbClass.getConnection();
	        buildData();	        	    
	    }
	    catch(ClassNotFoundException | SQLException ce){
	    	ce.printStackTrace();
	    }

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
		int indexToDelete = unitsTable.getSelectionModel().getSelectedIndex();
		Units unit = unitsTable.getSelectionModel().getSelectedItem();
		
		if(unit != null) {
			try {
		    	PreparedStatement statement = connection.prepareStatement("DELETE FROM units WHERE id = ?");
				statement.setInt(1, unit.getId());
				statement.executeUpdate();
				
				data.remove(indexToDelete);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else {
			Alert alert = new Alert(AlertType.WARNING);
	        alert.setTitle("Ошибка удаления");
	        alert.setContentText("Для удаления выберите элемент из списка");

	        alert.showAndWait();
		}
		
		mode = AddEditMode.ADD;
        addEditBtn.setText("Добавить");
		data.clear();
		buildData();
		addEditUnit.setText("");
    }
	
	@FXML
	private void addEdit() {
		session = sessFact.openSession();
		tr = session.beginTransaction();

		if(mode.equals(AddEditMode.ADD)) {
			UnitsEntity units = new UnitsEntity(0, addEditUnit.getText().toString());
			session.save(units);
		}else if(mode.equals(AddEditMode.EDIT)){
			UnitsEntity units = new UnitsEntity(unit.getId(), addEditUnit.getText().toString());
			session.update(units);
		}

		tr.commit();
		session.close();
		
		mode = AddEditMode.ADD;
        addEditBtn.setText("Добавить");
		
		data.clear();
		buildData();
		addEditUnit.setText("");
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
}
