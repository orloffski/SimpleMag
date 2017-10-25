package view.stockviews.invoices;

import application.DBClass;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import javafx.util.converter.DoubleStringConverter;
import model.*;
import utils.NumberUtils;
import view.stockviews.BarcodeItemsViewController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    private Text status;
	
	@FXML
    private TextField createDate;

	@FXML
	private TextField ttnNo;

	@FXML
	private DatePicker ttnDate;
	
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
    private TableColumn<InvoiceLine, Double> vendorPrice;

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
	private Button documentSave;

	@FXML
	private ImageView addLine;
	
	@FXML
	private ImageView deleteLine;

	@FXML
	private void initialize() {
		addLine.setImage(new Image("file:resources/images/add.png"));
		deleteLine.setImage(new Image("file:resources/images/delete.png"));
		
		type.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
			String newType = NumberUtils.getDocSuffix(newValue);
			number.setText(NumberUtils.getNextDocNumber(newType));
		}));

		documentSet.setText(status.getText().toLowerCase().equals("проведен")?"не проведен":"проведен");
	}

	@FXML
	private void addLine(){
		if(invoice.getStatus().toLowerCase().equals("проведен")){
			viewErrorMessage();

			return;
		}

		int itemId = getNewItemBarcode();
		Items item = null;

		if(itemId != -1){
			String SQL = "SELECT * FROM items " + "WHERE id = '" + itemId + "';";
			ResultSet rs = null;
			try {
				rs = connection.createStatement().executeQuery(SQL);
				if(rs.next()){
					item = new Items(
							rs.getInt("id"),
							rs.getString("vendor_code"),
							rs.getString("name"),
							rs.getString("vendor_country"),
							rs.getInt("unit_id"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			if(item != null){
				SQL = "INSERT INTO invoices_lines SET " +
						"invoice_number = '" + invoice.getNumber() + "', " +
						"item_id = " + item.getId() + ", " +
						"item_name = '" + item.getName() + "'," +
						"vat = " + 20 + ", " +
						"extra_price = " + 40 + ";";
				try {
					connection.createStatement().executeUpdate(SQL);

					InvoiceLineData.clear();

					setInvoice(invoice);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			invoiceLinesTable.refresh();
		}

	}

	@FXML
	private void deleteLine(){
		if(invoice.getStatus().toLowerCase().equals("проведен")){
			viewErrorMessage();

			return;
		}

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
		invoice.setSumm(Double.parseDouble(String.format( "%.2f",
				invoice.getSumm() - line.getCount() * line.getVendorPrice()).replace(",",".")));
//		invoice.setSumm(invoice.getSumm() - line.getCount() * line.getVendorPrice());
		invoice.setFullSumm(Double.parseDouble(String.format( "%.2f",
				invoice.getFullSumm() - line.getCount() * line.getRetailPrice()).replace(",",".")));
//		invoice.setFullSumm(invoice.getFullSumm() - line.getCount() * line.getRetailPrice());

		try{
			String SQL = "UPDATE invoices_headers "
					+ "SET count = '" + invoice.getCount() + "', "
					+ "summ = '" + invoice.getSumm() + "', "
					+ "full_summ = '" + invoice.getFullSumm() + "' "
					+ "WHERE id = " + invoice.getId() + ";";

			connection.createStatement().executeUpdate(SQL);
		}catch (SQLException e){
			e.printStackTrace();
		}

		this.count.setText(String.valueOf(invoice.getCount()));
		this.summ.setText(String.valueOf(invoice.getSumm()));
		this.summVat.setText(String.format( "%.2f",
				Double.parseDouble(this.summVat.getText()) - line.getSummVat()).replace(",","."));
		this.summIncludeVat.setText(String.valueOf(Double.parseDouble(this.summIncludeVat.getText()) - line.getSummIncludeVat()));
		this.fullDocSumm.setText(String.valueOf(invoice.getFullSumm()));
	}

	@FXML
	private void saveDocument(){
		try{
			if(this.invoice == null){
				String SQL = "INSERT INTO invoices_headers SET "
						+ "number = '" + number.getText() + "', "
						+ "type = '" + type.getValue() + "', "
						+ "status = '" + status.getText() + "', "
						+ "ttn_number = '" + ttnNo.getText() + "', "
						+ "ttn_date = '" + ttnDate.getEditor().getText() + "', "
						+ "counterparty = '" + counterparty.getValue() + "';";

				String SQL2 = "SELECT * FROM invoices_headers ORDER BY id DESC LIMIT 1";
				connection.createStatement().executeUpdate(SQL);

				ResultSet rs = connection.createStatement().executeQuery(SQL2);
				if (rs.next()) {
					InvoiceHeader invoice = new InvoiceHeader(
							rs.getInt("id"),
							rs.getString("number"),
							rs.getString("type"),
							rs.getString("status"),
							rs.getString("counterparty"),
							rs.getInt("count"),
							rs.getDouble("summ"),
							rs.getInt("counterparty_id"),
							rs.getString("lastcreated"),
							rs.getInt("recipient_id"),
							rs.getString("recipient_name"),
							rs.getDouble("full_summ"),
							rs.getString("ttn_number"),
							rs.getString("ttn_date"));

					this.invoice = invoice;
				}
			}else{
				PreparedStatement statement =
						connection.prepareStatement("UPDATE invoices_headers SET number = ?, type = ?, counterparty = ?, status = ?, ttn_number = ?, ttn_date = ? WHERE number = ?");
				statement.setString(1, number.getText());
				statement.setString(2, type.getValue());
				statement.setString(3, counterparty.getValue());
				statement.setString(4, status.getText());
				statement.setString(5, ttnNo.getText());
				statement.setString(6, ttnDate.getEditor().getText());
				statement.setString(7, invoice.getNumber());
				statement.executeUpdate();
			}

			documentSave.setDisable(true);
			documentSet.setDisable(false);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@FXML
	private void documentSetAction() {
		if(type.getValue().toLowerCase().equals("РїРѕСЃС‚СѓРїР»РµРЅРёРµ"))
			if(status.getText().toLowerCase().equals("РїСЂРѕРІРµРґРµРЅ")){
				setPrices(false, invoice.getNumber());
			}else{
				setPrices(true, invoice.getNumber());
			}

		try{
			PreparedStatement statement =
					connection.prepareStatement("UPDATE invoices_headers SET number = ?, type = ?, counterparty = ?, status = ?, ttn_number = ?, ttn_date = ? WHERE number = ?");
			statement.setString(1, number.getText());
			statement.setString(2, type.getValue());
			statement.setString(3, counterparty.getValue());
			statement.setString(4, status.getText().toLowerCase().equals("проведен")?"не проведен":"проведен");
			statement.setString(5, ttnNo.getText());
			statement.setString(6, ttnDate.getEditor().getText());
			statement.setString(7, invoice.getNumber());
			statement.executeUpdate();

			status.setText(status.getText().toLowerCase().equals("проведен")?"не проведен":"проведен");

			documentSet.setText(status.getText().toLowerCase().equals("проведение")?"отмена проведения":"проведение");
			this.invoice.setStatus(status.getText());
		}catch(Exception e){
	          e.printStackTrace();           
	    }
	}

	private void setPrices(boolean set, String invoiceNum){
		PreparedStatement statement = null;

		if(set) {
			List<InvoiceLine> lines = new ArrayList<>();

			try {
				String SQL = "SELECT * FROM invoices_lines WHERE invoice_number = '" + invoiceNum + "' ORDER BY id";
				ResultSet rs = connection.createStatement().executeQuery(SQL);
				while (rs.next()) {
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
					lines.add(line);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (lines.size() > 0) {
				for (InvoiceLine line : lines) {
					String SQL = "INSERT INTO prices SET " +
							"price = '" + line.getRetailPrice() + "', " +
							"item_id = " + line.getItemId() + ", " +
							"reason = '" + invoiceNum + "';";
					try {
						connection.createStatement().executeUpdate(SQL);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}else{
			try {
				statement = connection.prepareStatement("DELETE FROM prices WHERE reason = ?");
				statement.setString(1, invoiceNum);
				statement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void viewErrorMessage(){
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle("ошибка редактирования");
		alert.setContentText("Ошибка редактирования документа, для редактирования отмените проведение");

		alert.showAndWait();
	}

	private int getNewItemBarcode(){
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/view/stockviews/BarcodeItemsView.fxml"));
		try {
			BorderPane page = loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Список товаров");
			dialogStage.getIcons().add(new Image("file:resources/images/barcode.png"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(main.getPrimaryStage());
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			BarcodeItemsViewController controller = loader.getController();
			controller.setDialogStage(dialogStage);

			dialogStage.showAndWait();

			return controller.getItemId();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return -1;
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
			dialogStage.setTitle("редактирование документа");
			this.mode = AddEditMode.EDIT;

			initDocumentForEdit();
			
			documentSet.setText(invoice.getStatus().toLowerCase().equals("проведение")?"отмена проведения":"проведение");
			ttnNo.setText(invoice.getTtnNo());
			ttnDate.getEditor().setText(invoice.getTtnDate());

			documentSave.setDisable(false);
			documentSet.setDisable(false);
		}else {
			dialogStage.setTitle("создание нового документа");
			this.mode = AddEditMode.ADD;
			loadCounterParties("");
			loadInvoiceLines("0");

			documentSave.setDisable(false);
			documentSet.setDisable(true);
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

		vendorPrice.setCellValueFactory(cellData -> cellData.getValue().vendorPriceProperty().asObject());
		vendorPrice.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		vendorPrice.setOnEditCommit(t -> updateInvoiceLine(
				invoiceLinesTable.getSelectionModel().getSelectedItem().getCount(),
				t.getNewValue().doubleValue(),
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
		if(invoice.getStatus().toLowerCase().equals("проведен")){
			viewErrorMessage();

			return;
		}

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
		newVendorSumm = Double.parseDouble(String.format( "%.2f", newVendorSumm ).replace(",","."));

		double newLineVat = (vendorPrice * (vat + 100)/100 - vendorPrice) * count;
		newLineVat = Double.parseDouble(String.format( "%.2f", newLineVat ).replace(",","."));

		oldLine.setSummVat(newLineVat);
		oldLine.setSummIncludeVat(Double.parseDouble(String.format( "%.2f", newLineVat + newVendorSumm ).replace(",",".")));

		double newRetailPrice = vendorPrice * (vat + 100)/100 * (extraPrice + 100)/100;
		newRetailPrice = Double.parseDouble(String.format( "%.2f", newRetailPrice ).replace(",","."));

		oldLine.setRetailPrice(newRetailPrice);

		invoice.setSumm(
				Double.parseDouble(String.format( "%.2f",
						invoice.getSumm() - (oldCount * oldVendorPrice) + (oldLine.getCount() * oldLine.getVendorPrice())).replace(",","."))
				);

		invoice.setCount(invoice.getCount() - oldCount + oldLine.getCount());

		double invoiceRetailPrice = invoice.getFullSumm() - (oldCount * oldRetailPrice) + (oldLine.getCount() * oldLine.getRetailPrice());
		invoiceRetailPrice = Double.parseDouble(String.format( "%.2f", invoiceRetailPrice ).replace(",","."));
		invoice.setFullSumm(invoiceRetailPrice);

		this.count.setText(String.valueOf(Integer.parseInt(this.count.getText()) - oldCount + oldLine.getCount()));
		this.summ.setText(String.format( "%.2f", invoice.getSumm() ).replace(",","."));
		this.summVat.setText(String.format( "%.2f",
				Double.parseDouble(this.summVat.getText()) - oldVatSumm + oldLine.getSummVat()).replace(",","."));
		this.summIncludeVat.setText(String.format( "%.2f",
				Double.parseDouble(this.summIncludeVat.getText()) - oldSummInclVat + oldLine.getSummIncludeVat()).replace(",","."));
		this.fullDocSumm.setText(String.format( "%.2f", invoice.getFullSumm() ).replace(",","."));

		setLineToDB(oldLine);
		setHeaderToDB(invoice);

		invoiceLinesTable.refresh();
	}

	private void setHeaderToDB(InvoiceHeader header){
		try{
			String SQL = "UPDATE invoices_headers "
					+ "SET summ = " + header.getSumm() + ", "
					+ "count = " + header.getCount() + ", "
					+ "full_summ = " + header.getFullSumm() + " "
					+ "WHERE id = " + header.getId() + ";";

			connection.createStatement().executeUpdate(SQL);
		}catch (SQLException e){
			e.printStackTrace();
		}
	}

	private void setLineToDB(InvoiceLine line){
		try{
			String SQL = "UPDATE invoices_lines "
					+ "SET count = " + line.getCount() + ", "
					+ "vendor_price = " + line.getVendorPrice() + ", "
					+ "vat = " + line.getVat() + ", "
					+ "extra_price = " + line.getExtraPrice() + ", "
					+ "summ_vat = " + line.getSummVat() + ", "
					+ "summ_incl_vat = " + line.getSummIncludeVat() + ", "
					+ "retail_price = " + line.getRetailPrice() + " "
					+ "WHERE id = " + line.getId() + ";";

			connection.createStatement().executeUpdate(SQL);
		}catch (SQLException e){
			e.printStackTrace();
		}
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

		this.summIncludeVat.setText(String.format( "%.2f", summIncludeVat ).replace(",","."));
		this.summVat.setText(String.format( "%.2f", summVat ).replace(",","."));
		this.count.setText(String.valueOf(itemsCount));
		this.summ.setText(String.format( "%.2f", itemsSumm ).replace(",","."));
		this.fullDocSumm.setText(String.format( "%.2f", fullDocSumm ).replace(",","."));
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
