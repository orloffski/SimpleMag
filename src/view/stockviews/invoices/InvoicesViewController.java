package view.stockviews.invoices;

import application.Main;
import dbhelpers.InvoicesHeaderDBHelper;
import dbhelpers.InvoicesLineDBHelper;
import entity.InvoicesHeadersEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
import view.AbstractController;

import java.io.IOException;
import java.util.List;

public class InvoicesViewController extends AbstractController{
	
	private Main main;
	
	private ObservableList<InvoiceHeader> data;
	
	@FXML
	private TableView<InvoiceHeader> invoicesTable;
	
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
	        
	        return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return false;
	}
	
	private void buildData(){
		data = FXCollections.observableArrayList();

		List<InvoicesHeadersEntity> invoicesList = InvoicesHeaderDBHelper.getInvoicesHeadersEntitiesList(sessFact);

		for (InvoicesHeadersEntity invHeader : invoicesList) {
			InvoiceHeader headerItem = new InvoiceHeader(
					invHeader.getId(),
					invHeader.getNumber(),
					invHeader.getType(),
					invHeader.getStatus(),
					invHeader.getCounterparty(),
					invHeader.getCount(),
					invHeader.getSumm(),
					invHeader.getCounterpartyId(),
					invHeader.getLastcreated().toString(),
					invHeader.getRecipientId(),
					invHeader.getRecipientName(),
					invHeader.getFullSumm(),
					invHeader.getTtnNumber(),
					invHeader.getTtnDate());
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

                if (invoice.getNumber().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches name.
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
		InvoicesHeaderDBHelper.deleteHeaderById(sessFact, id);
	}
}
