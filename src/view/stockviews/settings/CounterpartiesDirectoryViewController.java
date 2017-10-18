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
import model.Counterparties;

public class CounterpartiesDirectoryViewController {
	
	private Stage dialogStage;
	private AddEditMode mode;
	private ObservableList<Counterparties> data;
	private DBClass dbClass;
	private Connection connection;
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
	private Button closeBtn;
	
	@FXML
	private void initialize() {
		mode = AddEditMode.ADD;
		
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		unnColumn.setCellValueFactory(cellData -> cellData.getValue().unnProperty());
		adressColumn.setCellValueFactory(cellData -> cellData.getValue().adressProperty());
		
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
	    
	    counterpartiesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	        if (newSelection != null) {
	        	counterparty = (Counterparties) counterpartiesTable.getSelectionModel().getSelectedItem();
	            mode = AddEditMode.EDIT;
	            nameTextField.setText(counterparty.getName());
	            unnTextField.setText(counterparty.getUnn());
	            adressTextField.setText(counterparty.getAdress());
	            addEditBtn.setText("изменить");
	        }
	    });
	}
	
	@FXML
    private void close() {
        dialogStage.close();
    }
	
	@FXML
    private void delete() {
		int indexToDelete = counterpartiesTable.getSelectionModel().getSelectedIndex();
		Counterparties counterparty = (Counterparties) counterpartiesTable.getSelectionModel().getSelectedItem();
		
		if(counterparty != null) {
			try {
		    	PreparedStatement statement = connection.prepareStatement("DELETE FROM counterparties WHERE id = ?");
				statement.setInt(1, counterparty.getId());
				statement.executeUpdate();
				
				data.remove(indexToDelete);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else {
			Alert alert = new Alert(AlertType.WARNING);
	        alert.setTitle("Не выбран еконтрагент для удаления");
	        alert.setContentText("Для удаления еконтрагент выберите из списка");

	        alert.showAndWait();
		}
		
        addEditBtn.setText("добавить");
		data.clear();
		buildData();
    }
	
	@FXML
	private void addEdit() {		
		if(mode.equals(AddEditMode.ADD)) {
			try {
				connection = new DBClass().getConnection();
				String SQL = "INSERT INTO counterparties SET name = '" 
						+ nameTextField.getText().toString() + "', "
						+ "unn = '"
						+ unnTextField.getText().toString() + "', "
						+ "adress = '"
						+ adressTextField.getText().toString() + "';";
				connection.createStatement().executeUpdate(SQL);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else if(mode.equals(AddEditMode.EDIT)){
			try {
				connection = new DBClass().getConnection();
				String SQL = "UPDATE counterparties SET name = '" 
						+ nameTextField.getText().toString() + "', "
						+ "unn = '"
						+ unnTextField.getText().toString() + "', "
						+ "adress = '"
						+ adressTextField.getText().toString() + "' "
						+ "WHERE id = "
						+ counterparty.getId() + ";";
				connection.createStatement().executeUpdate(SQL);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		mode = AddEditMode.ADD;
        addEditBtn.setText("добавить");
		
		data.clear();
		buildData();
		nameTextField.setText("");
		unnTextField.setText("");
		adressTextField.setText("");
	}
	
	public void buildData(){			
		data = FXCollections.observableArrayList();
	    try{      
	        String SQL = "SELECT * FROM counterparties ORDER BY id";            
	        ResultSet rs = connection.createStatement().executeQuery(SQL);  
	        while(rs.next()){
	        	Counterparties counterparties = new Counterparties(rs.getInt("id"), 
	            		rs.getString("name"),
	            		rs.getString("adress"),
	            		rs.getString("unn"));
	            data.add(counterparties);   	       
	        }
	        counterpartiesTable.setItems(data);
	        
	        addFilter();
	    }
	    catch(Exception e){
	          e.printStackTrace();           
	    }
	}
	
	public void addFilter() {
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

	public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;      
    }
}
