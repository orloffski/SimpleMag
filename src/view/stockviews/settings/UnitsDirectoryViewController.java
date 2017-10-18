package view.stockviews.settings;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.DBClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.AddEditMode;
import model.Items;
import model.Units;

public class UnitsDirectoryViewController {

	private Stage dialogStage;
	
	private ObservableList<Units> data;
	private DBClass dbClass;
	private Connection connection;
	private AddEditMode mode;
	
	@FXML
	private TableView unitsTable;
	
	@FXML
    private TableColumn<Units, String> unitColumn;
	
	@FXML
    private TextField filter;
	
	@FXML
    private TextField addEditUnit;
	
	@FXML
	private Button addEditBtn;
	
	@FXML
	private Button closeBtn;

	@FXML
	private void initialize() {
        unitColumn.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
        
		dbClass = new DBClass();
	    try{
	    	connection = dbClass.getConnection();
	        buildData();	        	    
	    }
	    catch(ClassNotFoundException ce){
	    	ce.printStackTrace();
	    }
	    catch(SQLException ce){
	    	ce.printStackTrace();
	    }
	}
	
	@FXML
    private void close() {
        dialogStage.close();
    }
	
	@FXML
	private void addEdit() {
		mode = AddEditMode.ADD;
		
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
		}else {
			
		}
		
		data.clear();
		buildData();
		addEditUnit.setText("");
	}
	
	public void buildData(){			
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
	
	public void addFilter() {
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
	
	public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;      
    }
}
