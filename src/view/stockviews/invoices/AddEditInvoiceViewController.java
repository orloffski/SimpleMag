package view.stockviews.invoices;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.DBClass;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.AddEditMode;
import model.InvoiceHeader;
import model.InvoiceLine;
import model.InvoicesTypes;
import model.StatusTypes;

public class AddEditInvoiceViewController {
	
	private Main main;
	private ObservableList<String> counterpartiesData;
	private ObservableList<InvoiceLine> InvoiceLineData;
	private DBClass dbClass;
	private Connection connection;
	private Stage dialogStage;
	private InvoiceHeader invoice;
	private AddEditMode mode;
	private boolean okClicked = false;
	
	@FXML
    private TextField number;
	
	@FXML
	private ComboBox<String> type;
	
	@FXML
    private ComboBox<String> status;
	
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
	private void initialize() {
		
	}
	
	public void setInvoice(InvoiceHeader invoice) {
		this.invoice = invoice;
		
		dbClass = new DBClass();
	    try{
	    	connection = dbClass.getConnection();	        	    
	    }
	    catch(ClassNotFoundException ce){
	    	ce.printStackTrace();
	    }
	    catch(SQLException ce){
	    	ce.printStackTrace();
	    }
		
		type.setItems(InvoicesTypes.getTypes());
		status.setItems(StatusTypes.getTypes());
		
		if(this.invoice != null) {
			dialogStage.setTitle("Редактирование накладной");
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
			status.setValue(invoice.getStatus());
			
			number.setText(invoice.getNumber());
			createDate.setText(invoice.getLastcreated().split(" ")[0]);
			count.setText(String.valueOf(invoice.getCount()));
			summ.setText(String.valueOf(invoice.getSumm()));
		}else {
			dialogStage.setTitle("Создание накладной");
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
	
	public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
	
	public void setMain(Main main) {
        this.main = main;
    }
	
	public boolean isOkClicked() {
        return okClicked;
    }
}
