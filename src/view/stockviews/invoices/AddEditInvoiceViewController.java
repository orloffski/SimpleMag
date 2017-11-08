package view.stockviews.invoices;

import application.Main;
import dbhelpers.*;
import entity.*;
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
import modes.AddEditMode;
import utils.MessagesUtils;
import utils.NumberUtils;
import view.AbstractController;
import view.stockviews.BarcodeItemsViewController;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddEditInvoiceViewController extends AbstractController{

	private Main main;
	private ObservableList<String> counterpartiesData;
	private ObservableList<InvoiceLine> InvoiceLineData;
	private List<Counterparties> counterparties;
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
		getSessionData();

		addLine.setImage(new Image("file:resources/images/add.png"));
		deleteLine.setImage(new Image("file:resources/images/delete.png"));
		
		type.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
			String newType = NumberUtils.getDocSuffix(newValue);
			number.setText(NumberUtils.getNextDocNumber(newType));

			// update salesLines invoice number
		}));

		documentSet.setText(status.getText().toLowerCase().equals("проведен")?"не проведен":"проведен");
	}

	@FXML
	private void addLine(){
		if(invoice.getStatus().toLowerCase().equals("проведен")){
			MessagesUtils.showAlert("Ошибка редактирования",
					"Ошибка редактирования документа, для редактирования отмените проведение");

			return;
		}

		int itemId = getNewItemFromBarcode();
		Items item = null;

		if(itemId != -1){
			ItemsEntity itemsEntity = ItemsDBHelper.getItemsEntityById(sessFact, itemId);

			item = new Items(itemsEntity.getId(),
					itemsEntity.getVendorCode(),
					itemsEntity.getName(),
					itemsEntity.getVendorCountry(),
					itemsEntity.getUnitId());


			if(item != null) InvoicesLineDBHelper.saveEntity(
					sessFact,
					new InvoicesLinesEntity(
							0,
							1,
							invoice.getNumber(),
							item.getId(),
							0d,
							(byte) 20,
							(byte) 40,
							0d,
							item.getName(),
							0,
							0d,
							0d
					)
			);

			invoiceLinesTable.refresh();
		}
	}

	@FXML
	private void deleteLine(){
		if(invoice.getStatus().toLowerCase().equals("проведен")){
			MessagesUtils.showAlert("Ошибка редактирования",
					"Ошибка редактирования документа, для редактирования отмените проведение");

			return;
		}

		int indexToDelete = invoiceLinesTable.getSelectionModel().getSelectedIndex();
		InvoiceLine line = invoiceLinesTable.getSelectionModel().getSelectedItem();

		deleteInvoiceLine(line.getId());
		InvoiceLineData.remove(indexToDelete);

		invoice.setCount(invoice.getCount() - line.getCount());
		invoice.setSumm(Double.parseDouble(String.format( "%.2f",
				invoice.getSumm() - line.getCount() * line.getVendorPrice()).replace(",",".")));
		invoice.setFullSumm(Double.parseDouble(String.format( "%.2f",
				invoice.getFullSumm() - line.getCount() * line.getRetailPrice()).replace(",",".")));

		setHeaderToDB(this.invoice);

		updateForm(
				Double.parseDouble(this.summIncludeVat.getText()) - line.getSummIncludeVat(),
				Double.parseDouble(this.summVat.getText()) - line.getSummVat(),
				invoice.getCount(),
				invoice.getSumm(),
				invoice.getFullSumm()
		);
	}

	@FXML
	private void saveDocument(){
		if(this.invoice == null){
			this.invoice = createHeader();
			InvoicesHeaderDBHelper.saveEntity(sessFact, createHeaderEntityFromHeader(this.invoice));
		}else{
			this.invoice = updateHeader(
				this.invoice,
				number.getText(),
				type.getValue(),
				counterparty.getValue(),
				status.getText().toLowerCase().equals("проведен")?"не проведен":"проведен",
				ttnNo.getText(),
				ttnDate.getEditor().getText());

			setHeaderToDB(this.invoice);
		}

		documentSave.setDisable(true);
		documentSet.setDisable(false);
	}
	
	@FXML
	private void documentSetAction() {
		if(type.getValue().toLowerCase().equals("поступление") || type.getValue().toLowerCase().equals("ввод начальных остатков"))
			if(status.getText().toLowerCase().equals("проведен")){
				setPrices(false, invoice.getNumber());
			}else{
				setPrices(true, invoice.getNumber());
			}

		this.invoice = updateHeader(
				this.invoice,
				number.getText(),
				type.getValue(),
				counterparty.getValue(),
				status.getText().toLowerCase().equals("проведен")?"не проведен":"проведен",
				ttnNo.getText(),
				ttnDate.getEditor().getText());

		setHeaderToDB(this.invoice);

		status.setText(status.getText().toLowerCase().equals("проведен")?"не проведен":"проведен");

		documentSet.setText(status.getText().toLowerCase().equals("проведение")?"отмена проведения":"проведение");
		this.invoice.setStatus(status.getText());
	}

	private void setPrices(boolean set, String invoiceNum){
		if(set) {
			List<InvoiceLine> lines = getInvoiceLines(invoiceNum);

			if (lines.size() > 0) {
				for (InvoiceLine line : lines) {
					setNewPrice(line.getRetailPrice(), line.getItemId(), line.getInvoiceNumber());
				}
			}
		}else{
			deletePrices(invoiceNum);
		}
	}

	private int getNewItemFromBarcode(){
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
			MessagesUtils.showAlert("Ошибка редактирования",
					"Ошибка редактирования документа, для редактирования отмените проведение");

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

		updateForm(
				Double.parseDouble(this.summIncludeVat.getText()) - oldSummInclVat + oldLine.getSummIncludeVat(),
				Double.parseDouble(this.summVat.getText()) - oldVatSumm + oldLine.getSummVat(),
				Integer.parseInt(this.count.getText()) - oldCount + oldLine.getCount(),
				invoice.getSumm(),
				invoice.getFullSumm()
		);

		setLineToDB(oldLine);
		setHeaderToDB(invoice);

		invoiceLinesTable.refresh();
	}

	private void setHeaderToDB(InvoiceHeader header){
		Date parsedDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			parsedDate = dateFormat.parse(header.getLastcreated());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		InvoicesHeaderDBHelper.updateEntity(sessFact,
				new InvoicesHeadersEntity(
						header.getId(),
						header.getNumber(),
						header.getType(),
						header.getStatus(),
						header.getCounterparty(),
						header.getCount(),
						header.getSumm(),
						header.getCounterpartyId(),
						new Timestamp(parsedDate.getTime()),
						header.getRecipientId(),
						header.getRecipientName(),
						header.getFullSumm(),
						header.getTtnNo(),
						header.getTtnDate()
				));
	}

	private void setLineToDB(InvoiceLine line){
		InvoicesLineDBHelper.updateEntity(sessFact,
				new InvoicesLinesEntity(
						line.getId(),
						line.getLineNumber(),
						line.getInvoiceNumber(),
						line.getItemId(),
						line.getVendorPrice(),
						(byte)line.getVat(),
						(byte)line.getExtraPrice(),
						line.getRetailPrice(),
						line.getItemName(),
						line.getCount(),
						line.getSummVat(),
						line.getSummIncludeVat()));
	}
	
	private void loadInvoiceLines(String invoiceNumber) {
		InvoiceLineData = FXCollections.observableArrayList();
		int itemsCount = 0;
		double itemsSumm = 0;
		double summVat = 0;
		double summIncludeVat = 0;
		double fullDocSumm = invoice != null ? invoice.getFullSumm() : 0;

		List<InvoiceLine> lines = getInvoiceLines(invoiceNumber);
		System.out.println(lines.size());

		for(InvoiceLine lineItem : lines){
			InvoiceLineData.add(lineItem);

			itemsCount += lineItem.getCount();
			itemsSumm += lineItem.getCount() * lineItem.getVendorPrice();
			summVat += lineItem.getSummVat();
			summIncludeVat += lineItem.getSummIncludeVat();
		}

		invoiceLinesTable.setItems(InvoiceLineData);

		updateForm(summIncludeVat, summVat, itemsCount, itemsSumm, fullDocSumm);
	}
	
	private void loadCounterParties(String value) {
		counterpartiesData = FXCollections.observableArrayList();

		this.counterparties = new ArrayList<>();
		List<CounterpartiesEntity> counterpartiesList = CounterpartiesDBHelper.getCounterpartiesEntitiesList(sessFact);

		for(CounterpartiesEntity counterpartiesItem : counterpartiesList){
			this.counterparties.add(new Counterparties(
					counterpartiesItem.getId(),
					counterpartiesItem.getName(),
					counterpartiesItem.getAdress(),
					counterpartiesItem.getUnn()
			));

			counterpartiesData.add(counterpartiesItem.getName());
		}

		counterparty.setItems(counterpartiesData);
		if(!value.isEmpty())
			counterparty.setValue(value);
	}

	@Override
	protected void clearForm() {

	}
	
	public void setMain(Main main) {
		this.main = main;
    }
	
	boolean isOkClicked() {
        return okClicked;
    }

    private void updateForm(double summIncludeVat, double summVat, int itemsCount, double itemsSumm, double fullDocSumm){
		this.summIncludeVat.setText(String.format( "%.2f", summIncludeVat ).replace(",","."));
		this.summVat.setText(String.format( "%.2f", summVat ).replace(",","."));
		this.count.setText(String.valueOf(itemsCount));
		this.summ.setText(String.format( "%.2f", itemsSumm ).replace(",","."));
		this.fullDocSumm.setText(String.format( "%.2f", fullDocSumm ).replace(",","."));
	}

	private List<InvoiceLine> getInvoiceLines(String invoiceNumber){

		List<InvoicesLinesEntity> invoiceLinesList = InvoicesLineDBHelper.getLinesByInvoiceNumber(sessFact, invoiceNumber);
		List<InvoiceLine> lines = new ArrayList<>();

		for(InvoicesLinesEntity invoicesLinesItem : invoiceLinesList){
			InvoiceLine line = new InvoiceLine(
					invoicesLinesItem.getId(),
					invoicesLinesItem.getLineNumber(),
					invoicesLinesItem.getInvoiceNumber(),
					invoicesLinesItem.getItemId(),
					invoicesLinesItem.getVendorPrice(),
					invoicesLinesItem.getVat().intValue(),
					invoicesLinesItem.getExtraPrice().intValue(),
					invoicesLinesItem.getRetailPrice(),
					invoicesLinesItem.getItemName(),
					invoicesLinesItem.getCount(),
					invoicesLinesItem.getSummVat(),
					invoicesLinesItem.getSummInclVat()
			);

			lines.add(line);
		}

		return lines;
	}

	private void setNewPrice(double retailPrice, int itemId, String reason){
		PricesDBHelper.saveEntity(sessFact,
				new PricesEntity(0, String.valueOf(retailPrice), itemId, new Timestamp(new Date().getTime()),reason));
	}

	private void deletePrices(String invoiceNumber){
		PricesDBHelper.deletePricesByInvoiceNumber(sessFact, invoiceNumber);
	}

	private InvoiceHeader updateHeader(InvoiceHeader headerToUpdate, String docNumber, String docType, String counterpartyName, String status, String ttnNo, String ttnDate){
		headerToUpdate.setNumber(docNumber);
		headerToUpdate.setType(docType);
		headerToUpdate.setCounterparty(counterpartyName);
		headerToUpdate.setStatus(status);
		headerToUpdate.setTtnNo(ttnNo);
		headerToUpdate.setTtnDate(ttnDate);

		return headerToUpdate;
	}

	private InvoiceHeader createHeader(){
		return new InvoiceHeader(
				0, number.getText(),
				type.getValue(),
				status.getText(),
				counterparty.getValue(),
				count.getText().equals("") ? 0 : Integer.parseInt(count.getText()),
				summ.getText().equals("") ? 0 : Double.parseDouble(summ.getText()),
				Counterparties.getCounterpartyIdByName(counterparties, counterparty.getValue()),
				String.valueOf(new Timestamp(new Date().getTime())),
				1,
				"1",
				fullDocSumm.getText().equals("") ? 0 : Double.parseDouble(fullDocSumm.getText()),
				ttnNo.getText(),
				ttnDate.getEditor().getText()
		);
	}

	private InvoicesHeadersEntity createHeaderEntityFromHeader(InvoiceHeader header){
		return new InvoicesHeadersEntity(
			header.getId(),
			header.getNumber(),
			header.getType(),
			header.getStatus(),
				header.getCounterparty(),
				header.getCount(),
				header.getSumm(),
				header.getCounterpartyId(),
				new Timestamp(new Date().getTime()),
				header.getRecipientId(),
				header.getRecipientName(),
				header.getFullSumm(),
				header.getTtnNo(),
				header.getTtnDate()
		);
	}

	private void deleteInvoiceLine(int id){
		InvoicesLineDBHelper.deleteLinesById(sessFact, id);
	}
}