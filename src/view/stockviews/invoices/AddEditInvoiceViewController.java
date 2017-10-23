package view.stockviews.invoices;

import application.DBClass;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import model.AddEditMode;
import model.InvoiceHeader;
import model.InvoiceLine;
import model.InvoicesTypes;
import utils.NumberUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

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

	private DecimalFormat df = new DecimalFormat("#.##");

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
    private Text count;
	
	@FXML
    private Text summ;

	@FXML
	private Text summVat;

	@FXML
	private Text summIncludeVat;

	@FXML
	private Text fullDocSumm;
	
	@FXML
	private TableView<InvoiceLine> invoiceLinesTable;
	
	@FXML
    private TableColumn<InvoiceLine, String> itemName;
	
	@FXML
    private TableColumn<InvoiceLine, Number> itemCount;
	
	@FXML
    private TableColumn<InvoiceLine, Number> vendorPrice;

	@FXML
	private TableColumn<InvoiceLine, Number> vendorSummVat;

	@FXML
	private TableColumn<InvoiceLine, Number> vendorSummInclVat;
	
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
			String newType = NumberUtils.getDocSuffix(newValue);
			number.setText(NumberUtils.getNextDocNumber(newType));
		}));
	}

	@FXML
	private void addLine(){

	}

	@FXML
	private void deleteLine(){
		int indexToDelete = invoiceLinesTable.getSelectionModel().getSelectedIndex();
		InvoiceLine line = invoiceLinesTable.getSelectionModel().getSelectedItem();

		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement("DELETE FROM invoices_lines WHERE id = ?");
			statement.setInt(1, line.getId());
			statement.executeUpdate();

			InvoiceLineData.remove(indexToDelete);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		invoice.setCount(invoice.getCount() - line.getCount());
		invoice.setSumm(invoice.getSumm() - line.getCount() * line.getVendorPrice());
		invoice.setFullSumm(invoice.getFullSumm() - line.getCount() * line.getRetailPrice());

		this.count.setText(String.valueOf(invoice.getCount()));
		this.summ.setText(String.valueOf(invoice.getSumm()));
		this.summVat.setText(String.valueOf(Double.parseDouble(this.summVat.getText()) - line.getSummVat()));
		this.summIncludeVat.setText(String.valueOf(Double.parseDouble(this.summIncludeVat.getText()) - line.getSummIncludeVat()));
		this.fullDocSumm.setText(String.valueOf(invoice.getFullSumm()));
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

			initDocumentForEdit();
			
			documentSet.setText(invoice.getStatus().equals("Проведен")?"Отмена проведения":"Проведение");
		}else {
			dialogStage.setTitle("Создание документа");
			this.mode = AddEditMode.ADD;
			loadCounterParties("");
			loadInvoiceLines("0");
		}
	}

	private void initDocumentForEdit(){
		invoiceLinesTable.setEditable(true);

		itemName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());

		itemCount.setCellValueFactory(cellData -> cellData.getValue().countProperty());
		itemCount.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
		itemCount.setOnEditCommit(t -> updateInvoiceLine(
				t.getNewValue().intValue(),
                invoiceLinesTable.getSelectionModel().getSelectedItem().getVendorPrice(),
                invoiceLinesTable.getSelectionModel().getSelectedItem().getVat(),
                invoiceLinesTable.getSelectionModel().getSelectedItem().getExtraPrice(),
                invoiceLinesTable.getSelectionModel().getSelectedItem()));

		vendorPrice.setCellValueFactory(cellData -> cellData.getValue().vendorPriceProperty());
		vendorPrice.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
		vendorPrice.setOnEditCommit(t -> updateInvoiceLine(
				invoiceLinesTable.getSelectionModel().getSelectedItem().getCount(),
				t.getNewValue().intValue(),
				invoiceLinesTable.getSelectionModel().getSelectedItem().getVat(),
				invoiceLinesTable.getSelectionModel().getSelectedItem().getExtraPrice(),
				invoiceLinesTable.getSelectionModel().getSelectedItem()));

		vendorSummVat.setCellValueFactory(cellData -> cellData.getValue().summVatProperty());

		vendorSummInclVat.setCellValueFactory(cellData -> cellData.getValue().summIncludeVatProperty());

		vat.setCellValueFactory(cellData -> cellData.getValue().vatProperty());
		vat.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
		vat.setOnEditCommit(t -> updateInvoiceLine(
				invoiceLinesTable.getSelectionModel().getSelectedItem().getCount(),
				invoiceLinesTable.getSelectionModel().getSelectedItem().getVendorPrice(),
				t.getNewValue().intValue(),
				invoiceLinesTable.getSelectionModel().getSelectedItem().getExtraPrice(),
				invoiceLinesTable.getSelectionModel().getSelectedItem()));

		extraPrice.setCellValueFactory(cellData -> cellData.getValue().extraPriceProperty());
		extraPrice.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
		extraPrice.setOnEditCommit(t -> updateInvoiceLine(
				invoiceLinesTable.getSelectionModel().getSelectedItem().getCount(),
				invoiceLinesTable.getSelectionModel().getSelectedItem().getVendorPrice(),
				invoiceLinesTable.getSelectionModel().getSelectedItem().getVat(),
				t.getNewValue().intValue(),
				invoiceLinesTable.getSelectionModel().getSelectedItem()));

		retailPrice.setCellValueFactory(cellData -> cellData.getValue().retailPriceProperty());

		loadInvoiceLines(invoice.getNumber());

		loadCounterParties(invoice.getCounterparty());

		type.setValue(invoice.getType());
		type.setEditable(false);
		status.setText(invoice.getStatus());

		number.setText(invoice.getNumber());
		createDate.setText(invoice.getLastcreated().split(" ")[0]);
	}

	private void updateInvoiceLine(int count, double vendorPrice, int vat, int extraPrice, InvoiceLine oldLine){
		int oldCount = oldLine.getCount();
		double oldVendorPrice = oldLine.getVendorPrice();
		double oldVatSumm = oldLine.getSummVat();
		double oldSummInclVat = oldLine.getSummIncludeVat();
		double oldRetailPrice = oldLine.getRetailPrice();

		oldLine.setCount(count);
		oldLine.setVendorPrice(vendorPrice);
		oldLine.setVat(vat);
		oldLine.setExtraPrice(extraPrice);

		double newVendorSumm = count * vendorPrice;
		newVendorSumm = (double)((int)(newVendorSumm * 100))/100;

		double newLineVat = (vendorPrice * (vat + 100)/100 - vendorPrice) * count;
		newLineVat = (double)((int)(newLineVat * 100))/100;

		oldLine.setSummVat(newLineVat);
		oldLine.setSummIncludeVat(newLineVat + newVendorSumm);

		double newRetailPrice = vendorPrice * (vat + 100)/100 * (extraPrice + 100)/100;
		newRetailPrice = (double)((int)(newRetailPrice * 100))/100;

		oldLine.setRetailPrice(newRetailPrice);

		// сумма поставщика
		invoice.setSumm(invoice.getSumm() - (oldCount * oldVendorPrice) + (oldLine.getCount() * oldLine.getVendorPrice()));

		// сумма документа
		double invoiceRetailPrice = invoice.getFullSumm() - (oldCount * oldRetailPrice) + (oldLine.getCount() * oldLine.getRetailPrice());
		invoiceRetailPrice = (double)((int)(invoiceRetailPrice * 100))/100;
		invoice.setFullSumm(invoiceRetailPrice);

		this.count.setText(String.valueOf(Integer.parseInt(this.count.getText()) - oldCount + oldLine.getCount()));
		this.summ.setText(String.valueOf(invoice.getSumm()));
		this.summVat.setText(String.valueOf(Double.parseDouble(this.summVat.getText()) - oldVatSumm + oldLine.getSummVat()));
		this.summIncludeVat.setText(String.valueOf(Double.parseDouble(this.summIncludeVat.getText()) - oldSummInclVat + oldLine.getSummIncludeVat()));
		this.fullDocSumm.setText(String.valueOf(invoice.getFullSumm()));

		oldLine.setToUpdate(true);

		invoiceLinesTable.refresh();
	}
	
	private void loadInvoiceLines(String invoiceNimber) {
		InvoiceLineData = FXCollections.observableArrayList();
		int itemsCount = 0;
		double itemsSumm = 0;
		double summVat = 0;
		double summIncludeVat = 0;
		double fullDocSumm = invoice != null ? invoice.getFullSumm() : 0;

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
	        			rs.getInt("count"),
						rs.getDouble("summ_vat"),
						rs.getDouble("summ_incl_vat")
	        			);
	        	InvoiceLineData.add(line);

	        	itemsCount += line.getCount();
				itemsSumm += line.getCount() * line.getVendorPrice();
				summVat += line.getSummVat();
				summIncludeVat += line.getSummIncludeVat();
	        }

	        invoiceLinesTable.setItems(InvoiceLineData);
	    }
	    catch(Exception e){
	          e.printStackTrace();           
	    }

		this.summIncludeVat.setText(String.valueOf(summIncludeVat));
		this.summVat.setText(String.valueOf(summVat));
		this.count.setText(String.valueOf(itemsCount));
		this.summ.setText(String.valueOf(itemsSumm));
		this.fullDocSumm.setText(String.valueOf(fullDocSumm));
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
