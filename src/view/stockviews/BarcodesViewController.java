package view.stockviews;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.DBClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.Barcodes;
import model.Items;

public class BarcodesViewController {
	
	private ObservableList<Barcodes> data;
	private Connection connection;
	private Stage dialogStage;
	private static int itemId;
	
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
		barcodesColumn.setCellValueFactory(cellData -> cellData.getValue().barcodeProperty());
	}
	
	public void buildData(int id){
		data = FXCollections.observableArrayList();
		
		try{
	    	connection = new DBClass().getConnection();
	    	String SQL = "SELECT * FROM barcodes WHERE item_id = " + id + " ORDER BY id";
	        ResultSet rs = connection.createStatement().executeQuery(SQL);  
	        while(rs.next()){
	            Barcodes barcode = new Barcodes(rs.getInt("id"), 
	            		rs.getString("barcode"),
	            		rs.getInt("item_id"));
	            data.add(barcode);   	       
	        }
	        barcodesTable.setItems(data);
	    }
	    catch(ClassNotFoundException ce){
	    	ce.printStackTrace();
	    }
	    catch(SQLException ce){
	    	ce.printStackTrace();
	    }
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
		int indexToDelete = barcodesTable.getSelectionModel().getSelectedIndex();
		Barcodes barcode = barcodesTable.getSelectionModel().getSelectedItem();
		
		if(barcode != null) {
			try {
		    	PreparedStatement statement = connection.prepareStatement("DELETE FROM barcodes WHERE id = ?");
				statement.setInt(1, barcode.getId());
				statement.executeUpdate();
				
				data.remove(indexToDelete);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else {
			Alert alert = new Alert(AlertType.WARNING);
	        alert.setTitle("Не выбран штрихкод для удаления");
	        alert.setContentText("Для удаления штрихкода выберите из списка");

	        alert.showAndWait();
		}
	}
	
	@FXML
	private void addBarcode() {
		try {
			connection = new DBClass().getConnection();
			String SQL = "INSERT INTO barcodes SET barcode = '" 
					+ barcodeAddField.getText().toString() + "', "
					+ "item_id = '"
					+ itemId + "';";
			connection.createStatement().executeUpdate(SQL);
			data.clear();
			buildData(itemId);
			barcodeAddField.setText("");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void close() {
		dialogStage.close();
	}
}
