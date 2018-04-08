package view.stockviews.invoices;

import application.Main;
import dbhelpers.*;
import entity.*;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import javafx.util.converter.DoubleStringConverter;
import model.*;
import modes.AddEditMode;
import reports.PriceListReport;
import reports.RetailPriceRegisterReport;
import reports.TTNOneListReport;
import reports.TTNWithApplicationReport;
import utils.HibernateUtil;
import utils.MessagesUtils;
import utils.NumberUtils;
import utils.SelectedObject;
import utils.settingsEngine.SettingsEngine;
import view.AbstractController;
import view.stockviews.BarcodeItemsFromStockViewController;
import view.stockviews.BarcodeItemsViewController;
import view.stockviews.ProductsInStockController;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static model.PrintForms.*;

public class AddEditInvoiceViewController extends AbstractController implements ChangeListener{

	private Main main;
	private ObservableList<String> counterpartiesData;
	private ObservableList<String> printForms;
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
	private ComboBox<String> print;
	
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
	private TableColumn<InvoiceLine, String> expireDate;
	
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
		
		type.getSelectionModel().selectedItemProperty().addListener(this);

		documentSet.setText(status.getText().toLowerCase().equals("проведен")?"отмена проведения":"проведение");

		getPrintForms();
	}

	private void getPrintForms(){
		print.setItems(PrintForms.getTypes());
		print.getSelectionModel().select(0);
		print.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(invoice.getStatus().equalsIgnoreCase("не проведен")){
                MessagesUtils.showAlert("Ошибка печати отчета",
                        "Запрещена печать отчетов из непроведенных накладных!");
                return;
            }

            if(newValue.equalsIgnoreCase(RETAIL_PRICE_REGISTER.toString())){
				new RetailPriceRegisterReport(HibernateUtil.getSessionFactory(), invoice);
            }else if(newValue.equalsIgnoreCase(PRICES.toString())){
                new PriceListReport(HibernateUtil.getSessionFactory(), invoice);
            }else if(newValue.equalsIgnoreCase(TTN_ONE_LIST.toString())) {
				new TTNOneListReport(HibernateUtil.getSessionFactory(), invoice);
			}else if(newValue.equalsIgnoreCase(TTN_WITH_APPLICATION.toString())){
            	new TTNWithApplicationReport(HibernateUtil.getSessionFactory(), invoice);
			}

            print.getSelectionModel().select(0);
        });
	}

	@FXML
	private void addLine(){
		if(invoice.getStatus().toLowerCase().equals("проведен")){
			MessagesUtils.showAlert("Ошибка редактирования",
					"Ошибка редактирования документа, для редактирования отмените проведение");

			return;
		}


		int itemId;
		Items item;
		InvoicesLinesEntity lineEntity = null;
		if(type.getValue().toLowerCase().equalsIgnoreCase(InvoicesTypes.RETURN.toString())
				&& SettingsEngine.getInstance().getSettings().productsInStockEnabled
				&& SettingsEngine.getInstance().getSettings().invoicesFromStock){
			// добавляем товар из остатков склада
			getItemFromStock(invoice.getCounterpartyId());
			BarcodeItemsFromStock product = (BarcodeItemsFromStock)SelectedObject.getObject();

			List<InvoicesHeadersEntity> headersEntities = InvoicesHeaderDBHelper.getHeadersByCounterpartyId(sessFact, invoice.getCounterpartyId());
			InvoicesLinesEntity linesEntity = InvoicesLineDBHelper.getLastInvoiceLineByItemId(sessFact, product.getItemId(), headersEntities);

			double vendorPrice = linesEntity == null ? 0d : linesEntity.getVendorPrice();
			byte vat = linesEntity == null ? 0 : linesEntity.getVat();

			lineEntity = new InvoicesLinesEntity(
					0,
					1,
					invoice.getNumber(),
					product.getItemId(),
					vendorPrice,
					vat,
					(byte) 0,
					0d,
					product.getItemName(),
					0,
					0d,
					0d,
					""
			);

		}else{
			// выбираем любой товар из системы
			itemId = getNewItemFromBarcode();

			double vendorPrice = 0d;
			byte vat = 20;
			byte extraPrice = 40;

			if(type.getValue().toLowerCase().equalsIgnoreCase(InvoicesTypes.DELIVERY.toString())) {
				List<InvoicesHeadersEntity> headersEntities = InvoicesHeaderDBHelper.getHeaders(sessFact);
				InvoicesLinesEntity linesEntity = InvoicesLineDBHelper.getLastInvoiceLineByItemId(sessFact, itemId, headersEntities);

				vendorPrice = linesEntity == null ? 0d : linesEntity.getVendorPrice();
				vat = linesEntity == null ? 20 : linesEntity.getVat();
				extraPrice = linesEntity == null ? 40 : linesEntity.getExtraPrice();
			}

			if(itemId != -1){
				ItemsEntity itemsEntity = ItemsDBHelper.getItemsEntityById(sessFact, itemId);

				item = new Items(itemsEntity.getId(),
						itemsEntity.getVendorCode(),
						itemsEntity.getName(),
						itemsEntity.getVendorCountry(),
						itemsEntity.getUnitId());


				if(item != null) {
					lineEntity = new InvoicesLinesEntity(
							0,
							1,
							invoice.getNumber(),
							item.getId(),
							vendorPrice,
							vat,
							extraPrice,
							0d,
							item.getName(),
							0,
							0d,
							0d,
							""
					);
				}
			}
		}

		if(lineEntity != null){
			InvoicesLineDBHelper.saveEntity(sessFact, lineEntity);
			InvoiceLineData.clear();
		}

		loadInvoiceLines(invoice.getNumber());
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
		if(ttnDate.getEditor().getText().equals("") || type.getValue() == null || counterparty.getValue() == null){
			MessagesUtils.showAlert("Ошибка сохранения накладной",
					"Укажите тип документа, контрагента и дату для сохранения.");
			return;
		}

		if(this.invoice == null){
			this.invoice = createHeader();
			InvoicesHeadersEntity headersEntity = createHeaderEntityFromHeader(this.invoice);
			InvoicesHeaderDBHelper.saveEntity(sessFact, headersEntity);
			this.invoice.setId(headersEntity.getId());
		}else{
			this.invoice = updateHeader(
				this.invoice,
				number.getText(),
				type.getValue(),
				counterparty.getValue(),
				status.getText().toLowerCase().equals("проведен")?"проведен":"не проведен",
				ttnNo.getText(),
				ttnDate.getEditor().getText());

			setHeaderToDB(this.invoice);
		}

		documentSet.setDisable(false);
		updateElements("проведен");
	}
	
	@FXML
	private void documentSetAction() {
		// проверка настройки контроля остатков на складе
		if(SettingsEngine.getInstance().getSettings().productsInStockEnabled) {
			// проверка документа на возможность проведения/отмены проведения
			if (!ProductsInStockController.checkItemsInStock(type.getValue(),
					status.getText().toLowerCase().equals("проведен") ? "не проведен" : "проведен",
					sessFact,
					number.getText()))
				return;

			if (type.getValue().toLowerCase().equals("поступление") || type.getValue().toLowerCase().equals("ввод начальных остатков"))
				if (status.getText().toLowerCase().equals("проведен")) {
					setPrices(false, invoice.getNumber());
				} else {
					setPrices(true, invoice.getNumber());
				}
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

		documentSet.setText(status.getText().toLowerCase().equals("проведен")?"отмена проведения":"проведение");
		this.invoice.setStatus(status.getText());

		updateElements(this.invoice.getStatus());
	}

	private void setPrices(boolean set, String invoiceNum){
		if(set) {
			List<InvoiceLine> lines = getInvoiceLines(invoiceNum);

			if (lines.size() > 0) {
				for (InvoiceLine line : lines) {
					try {
						setNewPrice(line.getRetailPrice(), line.getItemId(), line.getInvoiceNumber(), invoice.getTtnDate());
					} catch (ParseException e) {
						e.printStackTrace();
					}
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

	private BarcodeItemsFromStock getItemFromStock(int counterpartyId){
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/view/stockviews/BarcodeItemsFromStockView.fxml"));
		try {
			BorderPane page = loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Список товаров на складе");
			dialogStage.getIcons().add(new Image("file:resources/images/barcode.png"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(main.getPrimaryStage());
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			BarcodeItemsFromStockViewController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setCounterpartyId(counterpartyId);

			dialogStage.showAndWait();

			return controller.getStockLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	void setInvoice(InvoiceHeader invoice) {
		this.invoice = invoice;

		type.setItems(InvoicesTypes.getTypes());
		
		if(this.invoice != null) {
			dialogStage.setTitle("редактирование документа");
			this.mode = AddEditMode.EDIT;

			initInvoiceLineTable();
			
			documentSet.setText(invoice.getStatus().toLowerCase().equals("проведен")?"отмена проведения":"проведение");
			ttnNo.setText(invoice.getTtnNo());
			ttnDate.getEditor().setText(invoice.getTtnDate());

			documentSet.setDisable(false);
			updateElements(invoice.getStatus());
		}else {
			dialogStage.setTitle("создание нового документа");
			this.mode = AddEditMode.ADD;
			loadCounterParties("");
			loadInvoiceLines("0");

			initInvoiceLineTable();

			documentSave.setDisable(false);
			documentSet.setDisable(true);
		}
	}

	private void updateElements(String status){
		if(status.toLowerCase().equals("проведен")){
			documentSave.setDisable(true);
			type.setDisable(true);
			counterparty.setDisable(true);
			ttnDate.setDisable(true);
		}else{
			documentSave.setDisable(false);
			type.setDisable(false);
			counterparty.setDisable(false);
			ttnDate.setDisable(false);
		}
	}

	private void initInvoiceLineTable(){
		invoiceLinesTable.setEditable(true);

		itemName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());

		itemCount.setCellValueFactory(cellData -> cellData.getValue().countProperty());
		itemCount.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
		itemCount.setOnEditCommit(t -> updateInvoiceLine(
				t.getNewValue().intValue(),
				invoiceLinesTable.getSelectionModel().getSelectedItem().getVendorPrice(),
				invoiceLinesTable.getSelectionModel().getSelectedItem().getVat(),
				invoiceLinesTable.getSelectionModel().getSelectedItem().getExtraPrice(),
				invoiceLinesTable.getSelectionModel().getSelectedItem(),
				invoiceLinesTable.getSelectionModel().getSelectedItem().getExpireDate()));

		vendorPrice.setCellValueFactory(cellData -> cellData.getValue().vendorPriceProperty().asObject());
		vendorPrice.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		vendorPrice.setOnEditCommit(t -> updateInvoiceLine(
				invoiceLinesTable.getSelectionModel().getSelectedItem().getCount(),
				t.getNewValue().doubleValue(),
				invoiceLinesTable.getSelectionModel().getSelectedItem().getVat(),
				invoiceLinesTable.getSelectionModel().getSelectedItem().getExtraPrice(),
				invoiceLinesTable.getSelectionModel().getSelectedItem(),
				invoiceLinesTable.getSelectionModel().getSelectedItem().getExpireDate()));

		vendorSummVat.setCellValueFactory(cellData -> cellData.getValue().summVatProperty());

		vendorSummInclVat.setCellValueFactory(cellData -> cellData.getValue().summIncludeVatProperty());

		vat.setCellValueFactory(cellData -> cellData.getValue().vatProperty());
		vat.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
		vat.setOnEditCommit(t -> updateInvoiceLine(
				invoiceLinesTable.getSelectionModel().getSelectedItem().getCount(),
				invoiceLinesTable.getSelectionModel().getSelectedItem().getVendorPrice(),
				t.getNewValue().intValue(),
				invoiceLinesTable.getSelectionModel().getSelectedItem().getExtraPrice(),
				invoiceLinesTable.getSelectionModel().getSelectedItem(),
				invoiceLinesTable.getSelectionModel().getSelectedItem().getExpireDate()));

		extraPrice.setCellValueFactory(cellData -> cellData.getValue().extraPriceProperty());
		extraPrice.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
		extraPrice.setOnEditCommit(t -> updateInvoiceLine(
				invoiceLinesTable.getSelectionModel().getSelectedItem().getCount(),
				invoiceLinesTable.getSelectionModel().getSelectedItem().getVendorPrice(),
				invoiceLinesTable.getSelectionModel().getSelectedItem().getVat(),
				t.getNewValue().intValue(),
				invoiceLinesTable.getSelectionModel().getSelectedItem(),
				invoiceLinesTable.getSelectionModel().getSelectedItem().getExpireDate()));

		retailPrice.setCellValueFactory(cellData -> cellData.getValue().retailPriceProperty());

		expireDate.setCellValueFactory(cellData -> cellData.getValue().expireDateProperty());
		expireDate.setCellFactory(col -> new DatePickerCell());
		expireDate.setOnEditCommit(t -> updateInvoiceLine(
				invoiceLinesTable.getSelectionModel().getSelectedItem().getCount(),
				invoiceLinesTable.getSelectionModel().getSelectedItem().getVendorPrice(),
				invoiceLinesTable.getSelectionModel().getSelectedItem().getVat(),
				invoiceLinesTable.getSelectionModel().getSelectedItem().getExtraPrice(),
				invoiceLinesTable.getSelectionModel().getSelectedItem(),
				t.getNewValue()));

		if(invoice != null) {
			loadInvoiceLines(invoice.getNumber());

			loadCounterParties(invoice.getCounterparty());

			type.getSelectionModel().selectedItemProperty().removeListener(this);
			type.setValue(invoice.getType());
			type.getSelectionModel().selectedItemProperty().addListener(this);

			type.setEditable(false);
			status.setText(invoice.getStatus());

			number.setText(invoice.getNumber());
			createDate.setText(invoice.getLastcreated().split(" ")[0]);
		}
	}

	private void updateInvoiceLine(int count, double vendorPrice, int vat, int extraPrice, InvoiceLine oldLine, String expireDate){
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
		oldLine.setExpireDate(expireDate);

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
		setHeaderToDB(this.invoice);

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
						line.getSummIncludeVat(),
						line.getExpireDate()));
	}
	
	private void loadInvoiceLines(String invoiceNumber) {
		InvoiceLineData = FXCollections.observableArrayList();
		int itemsCount = 0;
		double itemsSumm = 0;
		double summVat = 0;
		double summIncludeVat = 0;
		double fullDocSumm = invoice != null ? invoice.getFullSumm() : 0;

		List<InvoiceLine> lines = getInvoiceLines(invoiceNumber);

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
					invoicesLinesItem.getSummInclVat(),
					invoicesLinesItem.getExpireDate()
			);

			lines.add(line);
		}

		return lines;
	}

	private void setNewPrice(double retailPrice, int itemId, String reason, String ttnDate) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		Date parsedDate = dateFormat.parse(ttnDate);
		PricesDBHelper.saveEntity(sessFact,
				new PricesEntity(0, String.valueOf(retailPrice), itemId,
						new java.sql.Timestamp(parsedDate.getTime()),reason));
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

	@Override
	public void changed(ObservableValue observable, Object oldValue, Object newValue) {
		String newType = NumberUtils.getDocSuffix(String.valueOf(newValue));
		number.setText(NumberUtils.getNextDocNumber(newType));

		ObservableList<InvoiceLine> lines = invoiceLinesTable.getItems();
		for(InvoiceLine line : lines) {
			line.setInvoiceNumber(number.getText());
			InvoicesLineDBHelper.updateEntity(sessFact, InvoicesLinesEntity.createInvoiceLineEntityFromInvoiceLine(line));
		}
	}

	public static class DatePickerCell extends TableCell<InvoiceLine, String> {

		private final DateTimeFormatter formatter ;
		private final DatePicker datePicker ;

		public DatePickerCell() {

			formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") ;
			datePicker = new DatePicker() ;

			datePicker.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
				if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
					datePicker.setValue(datePicker.getConverter().fromString(datePicker.getEditor().getText()));
					commitEdit(LocalDate.from(datePicker.getValue()).toString());
				}
				if (event.getCode() == KeyCode.ESCAPE) {
					cancelEdit();
				}
			});

			datePicker.setDayCellFactory(picker -> {
				DateCell cell = new DateCell();
				cell.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
					datePicker.setValue(cell.getItem());
					if (event.getClickCount() == 2) {
						datePicker.hide();
						commitEdit(LocalDate.from(cell.getItem()).toString());
					}
					event.consume();
				});
				cell.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
					if (event.getCode() == KeyCode.ENTER) {
						commitEdit(LocalDate.from(datePicker.getValue()).toString());
					}
				});
				return cell ;
			});

			contentDisplayProperty().bind(Bindings.when(editingProperty())
					.then(ContentDisplay.GRAPHIC_ONLY)
					.otherwise(ContentDisplay.TEXT_ONLY));
		}

		@Override
		protected void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);
			if (empty) {
				setText(null);
				setGraphic(null);
			} else {
				setText(item);
				setGraphic(datePicker);
			}
		}

		@Override
		public void startEdit() {
			super.startEdit();
			if (!isEmpty()) {
				datePicker.setValue(LocalDate.parse(getItem(), formatter));
			}
		}
	}
}