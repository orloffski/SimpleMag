package view.stockviews.settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.DBClass;
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

public class UnitsDirectoryViewController {

	private Stage dialogStage;
	
	private ObservableList<Units> data;
	private DBClass dbClass;
	private Connection connection;
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
	private Button closeBtn;

	@FXML
	private void initialize() {
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
	            unit = (Units) unitsTable.getSelectionModel().getSelectedItem();
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
		Units unit = (Units) unitsTable.getSelectionModel().getSelectedItem();
		
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
		if(mode.equals(AddEditMode.ADD)) {
			try {
				connection = new DBClass().getConnection();
				String SQL = "INSERT INTO units SET unit = '" 
						+ addEditUnit.getText().toString() + "';";
				connection.createStatement().executeUpdate(SQL);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else if(mode.equals(AddEditMode.EDIT)){
			try {
				connection = new DBClass().getConnection();
				String SQL = "UPDATE units SET unit = '" 
						+ addEditUnit.getText().toString() + "'"
						+ "WHERE id = "
						+ unit.getId() + ";";
				connection.createStatement().executeUpdate(SQL);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		mode = AddEditMode.ADD;
        addEditBtn.setText("Добавить");
		
		data.clear();
		buildData();
		addEditUnit.setText("");
	}
	
	private void buildData(){
		data = FXCollections.observableArrayList();
	    try{      
	        String SQL = "SELECT * FROM units ORDER BY id";            
	        ResultSet rs = connection.createStatement().executeQuery(SQL);  
	        while(rs.next()){
	            Units unit = new Units(rs.getInt("id"), 
	            		rs.getString("unit"));
	            data.add(unit);   	       
	        }
	        unitsTable.setItems(data);
	        
	        addFilter();
	    }
	    catch(Exception e){
	          e.printStackTrace();           
	    }
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
}
