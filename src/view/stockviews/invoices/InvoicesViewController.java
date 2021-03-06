package view.stockviews.invoices;

import application.Main;
import dbhelpers.InvoicesHeaderDBHelper;
import dbhelpers.InvoicesLineDBHelper;
import dbhelpers.ProductsInStockDBHelper;
import entity.InvoicesHeadersEntity;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.InvoiceHeader;
import org.hibernate.query.Query;
import utils.MessagesUtils;
import utils.settingsEngine.SettingsEngine;
import view.AbstractController;
import view.stockviews.ProductsInStockController;

import java.io.IOException;
import java.util.List;

public class InvoicesViewController extends AbstractController{
	
	private Main main;
	
	private ObservableList<InvoiceHeader> data;
	
	@FXML
	private TableView<InvoiceHeader> invoicesTable;

	@FXML
	private ComboBox<String> invoicesView;
	
	@FXML
    private TableColumn<InvoiceHeader, String> numberColumn;
	
	@FXML
    private TableColumn<InvoiceHeader, String> typeColumn;
	
	@FXML
    private TableColumn<InvoiceHeader, String> statusColumn;
	
	@FXML
    private TableColumn<InvoiceHeader, String> lastcreatedColumn;
	
	@FXML
    private TableColumn<InvoiceHeader, String> counterpartyColumn;
	
	@FXML
    private TableColumn<InvoiceHeader, Number> countColumn;

	@FXML
    private TableColumn<InvoiceHeader, Number> summColumn;
	
	@FXML
    private TextField filter;
	
	@FXML
	private ImageView addBtn;
	
	@FXML
	private ImageView editBtn;
	
	@FXML
	private ImageView deleteBtn;

	@FXML
	private void initialize() {
		getSessionData();

		addBtn.setImage(new Image("file:resources/images/add.png"));
		editBtn.setImage(new Image("file:resources/images/edit.png"));
		deleteBtn.setImage(new Image("file:resources/images/delete.png"));
		
		numberColumn.setCellValueFactory(cellData -> cellData.getValue().ttnNoProperty());
		typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
		statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
		lastcreatedColumn.setCellValueFactory(cellData -> cellData.getValue().lastcreatedProperty());
		counterpartyColumn.setCellValueFactory(cellData -> cellData.getValue().counterpartyProperty());
		countColumn.setCellValueFactory(cellData -> cellData.getValue().countProperty());
		summColumn.setCellValueFactory(cellData -> cellData.getValue().fullSummProperty());

		addInvoicesView();
		buildData();
	}
	
	@FXML
	private void addInvoice() {
		boolean okClicked = openAddEditItemDialog(null);
		if(okClicked) {
			data.clear();
			buildData();
		}
	}
	
	@FXML
	private void editInvoice() {
		InvoiceHeader invoice = invoicesTable.getSelectionModel().getSelectedItem();
		
		boolean okClicked = openAddEditItemDialog(invoice);
		if(okClicked) {
			data.clear();
			buildData();
		}
	}

	@FXML
	private void deleteInvoice(){
		InvoiceHeader invoice = invoicesTable.getSelectionModel().getSelectedItem();

		if(invoice != null){
			String invoiceNumber = invoice.getNumber();
			int invoiceId = invoice.getId();

			// проверка включена ли настройка контроля остатков на складе при удалении проведенной накладной
			if(invoice.getStatus().toLowerCase().equals("проведен") && SettingsEngine.getInstance().getSettings().productsInStockEnabled) {
				// контроль остатков на складе
				if (!ProductsInStockController.checkItemsInStock(invoice.getType(),
						invoice.getStatus().toLowerCase().equals("проведен") ? "не проведен" : "проведен",
						sessFact, invoiceNumber))
					return;
			}

			deleteLines(invoiceNumber);
			deleteHeader(invoiceId);

			data.clear();
			buildData();
		}else{
			MessagesUtils.showAlert("Ошибка удаления накладной","Выберите накладную для удаления.");
		}
	}
	
	private boolean openAddEditItemDialog(InvoiceHeader invoice) {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/stockviews/invoices/AddEditInvoiceView.fxml"));
        try {
			BorderPane page = loader.load();
			Stage dialogStage = new Stage();
	        dialogStage.getIcons().add(new Image("file:resources/images/invoices.png"));
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(main.getPrimaryStage());
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        
	        AddEditInvoiceViewController controller = loader.getController();
	        controller.setMain(main);
	        controller.setDialogStage(dialogStage);
	        controller.setInvoice(invoice);
	        
	        dialogStage.showAndWait();

	        data.clear();
	        buildData();
	        
	        return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return false;
	}
	
	private void buildData(){
		String viewType = "";

		if(invoicesView.getSelectionModel().getSelectedItem() != null)
			viewType = invoicesView.getSelectionModel().getSelectedItem();

		data = FXCollections.observableArrayList();

		List<InvoicesHeadersEntity> invoicesList = InvoicesHeaderDBHelper.getInvoicesHeadersEntitiesList(sessFact, true, viewType);

		for (InvoicesHeadersEntity invHeader : invoicesList) {
			InvoiceHeader headerItem = InvoiceHeader.createHeaderFromEntity(invHeader);
			data.add(headerItem);
		}

		invoicesTable.setItems(data);

		addFilter();
	}
	
	private void addFilter() {
		FilteredList<InvoiceHeader> filteredData = new FilteredList<>(data, p -> true);
        
        filter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(invoice -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (invoice.getTtnNo().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches ttnNo.
                }
                return false; // Does not match.
            });
        });
        
        SortedList<InvoiceHeader> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(invoicesTable.comparatorProperty());
        invoicesTable.setItems(sortedData);
	}
	
	public void setMain(Main main) {
        this.main = main;
    }

	@Override
	protected void clearForm() {

	}

	private void deleteLines(String invoiceNumber){
		InvoicesLineDBHelper.deleteLinesByInvoiceNumber(sessFact, invoiceNumber);
	}

	private void deleteHeader(int id){
	    String invoiceNum = InvoicesHeaderDBHelper.getInvoiceHeaderEntityById(sessFact, id).getNumber();

		InvoicesHeaderDBHelper.deleteHeaderById(sessFact, id);
        ProductsInStockDBHelper.deleteByInvoiceNumber(sessFact, invoiceNum);
	}

	private void addInvoicesView(){
		ObservableList<String> options =
				FXCollections.observableArrayList(
						"все",
						"проведенные",
						"не проведенные"
				);
		invoicesView.getItems().addAll(options);

		invoicesView.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				data.clear();
				buildData();
			}
		});
	}
}
