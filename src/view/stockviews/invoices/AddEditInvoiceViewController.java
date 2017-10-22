package view.stockviews.invoices;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import application.DBClass;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.AddEditMode;
import model.InvoiceHeader;
import model.InvoiceLine;
import model.InvoicesTypes;
import utils.NumberUtils;

public class AddEditInvoiceViewController {

	private Main main;
	private ObservableList<String> counterpartiesData;
	private ObservableList<InvoiceLine> InvoiceLineData;
	private DBClass dbClass;
	private Connection connection;
	private Stage dialogStage;
	private InvoiceHeader invoice;
	private List<InvoiceLine> invoiceLines;
	private AddEditMode mode;
	private boolean okClicked = false;
	
	@FXML
    private TextField number;
	
	@FXML
	private ComboBox<String> type;
	
	@FXML
    private Text status;
	
	@FXML
    private TextField createDate;
	
	@FXML
    private ComboBox<String> counterparty;
	
	@FXML
    private TextField count;
	
	@FXML
    private TextField summ;
	
	@FXML
	private TableView<InvoiceLine> invoiceLinesTable;
	
	@FXML
    private TableColumn<InvoiceLine, String> itemName;
	
	@FXML
    private TableColumn<InvoiceLine, Number> itemCount;
	
	@FXML
    private TableColumn<InvoiceLine, Number> vendorPrice;
	
	@FXML
    private TableColumn<InvoiceLine, Number> vat;
	
	@FXML
    private TableColumn<InvoiceLine, Number> extraPrice;
	
	@FXML
    private TableColumn<InvoiceLine, Number> retailPrice;
	
	@FXML
	private Button documentSet; 
	
	@FXML
	private ImageView addLine;

	@FXML
	private void initialize() {
		type.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
			String newType = "";

			switch (newValue){
				case "Поступление":
					newType = "REC";
					break;
				case "Возврат":
					newType = "RET";
					break;
				case "Перемещение":
					newType = "DEL";
					break;
				case "Ввод начальных остатков":
					newType = "INI";
					break;
			}
			number.setText(NumberUtils.getNextDocNumber(newType));
		}));
	}
	
	@FXML
	private void documentSetAction() {
		String newStatus = invoice.getStatus().equals("Проведен")?"Не проведен":"Проведен";
		try{      
			PreparedStatement statement = 
					connection.prepareStatement("UPDATE invoices_headers SET status = ?  WHERE number = ?");
			statement.setString(1, newStatus);
			statement.setString(2, invoice.getNumber());
			statement.executeUpdate();
			
			status.setText(newStatus);
			documentSet.setText(newStatus.equals("Проведен")?"Отмена проведения":"Проведение");
			this.invoice.setStatus(newStatus);
		}catch(Exception e){
	          e.printStackTrace();           
	    }
	}
	
	void setInvoice(InvoiceHeader invoice) {
		this.invoice = invoice;
		
		dbClass = new DBClass();
	    try{
	    	connection = dbClass.getConnection();	        	    
	    }
	    catch(ClassNotFoundException | SQLException ce){
	    	ce.printStackTrace();
	    }

		type.setItems(InvoicesTypes.getTypes());
		
		if(this.invoice != null) {
			dialogStage.setTitle("Изменение документа");
			this.mode = AddEditMode.EDIT;
			
			itemName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
			itemCount.setCellValueFactory(cellData -> cellData.getValue().countProperty());
			vendorPrice.setCellValueFactory(cellData -> cellData.getValue().vendorPriceProperty());
			vat.setCellValueFactory(cellData -> cellData.getValue().vatProperty());
			extraPrice.setCellValueFactory(cellData -> cellData.getValue().extraPriceProperty());
			retailPrice.setCellValueFactory(cellData -> cellData.getValue().retailPriceProperty());
			
			loadInvoiceLines(invoice.getNumber());
			
			loadCounterParties(invoice.getCounterparty());
			
			type.setValue(invoice.getType());
			status.setText(invoice.getStatus());
			
			number.setText(invoice.getNumber());
			createDate.setText(invoice.getLastcreated().split(" ")[0]);
			count.setText(String.valueOf(invoice.getCount()));
			summ.setText(String.valueOf(invoice.getSumm()));
			
			documentSet.setText(invoice.getStatus().equals("Проведен")?"Отмена проведения":"Проведение");
		}else {
			dialogStage.setTitle("Создание документа");
			this.mode = AddEditMode.ADD;
			loadCounterParties("");
		}
	}
	
	private void loadInvoiceLines(String invoiceNimber) {
		InvoiceLineData = FXCollections.observableArrayList();
		try{      
	        String SQL = "SELECT * FROM invoices_lines WHERE invoice_number = '" + invoiceNimber + "' ORDER BY id";            
	        ResultSet rs = connection.createStatement().executeQuery(SQL);  
	        while(rs.next()){
	        	InvoiceLine line = new InvoiceLine(
	        			rs.getInt("id"),
	        			rs.getInt("line_number"),
	        			rs.getString("invoice_number"),
	        			rs.getInt("item_id"),
	        			rs.getDouble("vendor_price"),
	        			rs.getInt("vat"),
	        			rs.getInt("extra_price"),
	        			rs.getDouble("retail_price"),
	        			rs.getString("item_name"),
	        			rs.getInt("count")
	        			);
	        	InvoiceLineData.add(line);   	       
	        }
	        
	        invoiceLinesTable.setItems(InvoiceLineData);
	    }
	    catch(Exception e){
	          e.printStackTrace();           
	    }
	}
	
	private void loadCounterParties(String value) {
		counterpartiesData = FXCollections.observableArrayList();
	    try{      
	        String SQL = "SELECT * FROM counterparties ORDER BY id";            
	        ResultSet rs = connection.createStatement().executeQuery(SQL);  
	        while(rs.next()){
	        	counterpartiesData.add(rs.getString("name"));   	       
	        }
	        
	        counterparty.setItems(counterpartiesData);
	        if(!value.isEmpty())
	        	counterparty.setValue(value);
	    }
	    catch(Exception e){
	          e.printStackTrace();           
	    }
	}
	
	void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
	
	public void setMain(Main main) {
		this.main = main;
    }
	
	boolean isOkClicked() {
        return okClicked;
    }
}
