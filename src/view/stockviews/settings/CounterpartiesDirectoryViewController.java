package view.stockviews.settings;

import entity.CounterpartiesEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.AddEditMode;
import model.Counterparties;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utils.HibernateUtil;
import utils.MessagesUtils;

import java.util.List;

public class CounterpartiesDirectoryViewController {
	
	private Stage dialogStage;
	private AddEditMode mode;
	private ObservableList<Counterparties> data;
	private Counterparties counterparty;

	private Session session;
	private SessionFactory sessFact;
	private org.hibernate.Transaction tr;
	
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
		getSessionData();

		mode = AddEditMode.ADD;
		
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		unnColumn.setCellValueFactory(cellData -> cellData.getValue().unnProperty());
		adressColumn.setCellValueFactory(cellData -> cellData.getValue().adressProperty());

		buildData();

		counterpartiesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	        if (newSelection != null) {
	        	counterparty = counterpartiesTable.getSelectionModel().getSelectedItem();
	            mode = AddEditMode.EDIT;
	            nameTextField.setText(counterparty.getName());
	            unnTextField.setText(counterparty.getUnn());
	            adressTextField.setText(counterparty.getAdress());
	            addEditBtn.setText("Изменить");
	        }
	    });
	}
	
	@FXML
    private void close() {
        dialogStage.close();
    }
	
	@FXML
    private void delete() {
		counterparty = counterpartiesTable.getSelectionModel().getSelectedItem();
		mode = AddEditMode.DELETE;
		
		if(counterparty != null)
			addEditDelete();
		else
			MessagesUtils.showAlert("Ошибка удаления", "Для удаления выберите элемент из списка");

		mode = AddEditMode.ADD;

		data.clear();
		buildData();
		clearForm();
    }
	
	@FXML
	private void addEditDelete() {
		if(nameTextField.getText().toString().length() == 0 ||
				unnTextField.getText().toString().length() == 0 ||
				adressTextField.getText().toString().length() == 0){
			MessagesUtils.showAlert("Ошибка", "Нельзя сохранять пустые элементы");
			return;
		}

		session = sessFact.openSession();
		tr = session.beginTransaction();

		if(mode.equals(AddEditMode.ADD)) {
			CounterpartiesEntity counterpartyItem = new CounterpartiesEntity(0,
					nameTextField.getText().toString(),
					adressTextField.getText().toString(),
					unnTextField.getText().toString());
			session.save(counterpartyItem);
		}else if(mode.equals(AddEditMode.EDIT)){
			CounterpartiesEntity counterpartyItem = new CounterpartiesEntity(counterparty.getId(),
					nameTextField.getText().toString(),
					adressTextField.getText().toString(),
					unnTextField.getText().toString());
			session.update(counterpartyItem);
		}else if(mode.equals(AddEditMode.DELETE)){
			session.delete(
					new CounterpartiesEntity(counterparty.getId(),
							counterparty.getName(),
							counterparty.getAdress(),
							counterparty.getUnn()));
		}

		tr.commit();
		session.close();
		
		mode = AddEditMode.ADD;
		
		data.clear();
		buildData();
		clearForm();
	}
	
	private void buildData(){
		data = FXCollections.observableArrayList();

		session = sessFact.openSession();
		tr = session.beginTransaction();

		List<CounterpartiesEntity> counterpartiesList = session.createCriteria(CounterpartiesEntity.class).list();

		for (CounterpartiesEntity counterpartyItem : counterpartiesList) {
			Counterparties counterparty = new Counterparties(counterpartyItem.getId(),
					counterpartyItem.getName(),
					counterpartyItem.getAdress(),
					counterpartyItem.getUnn());
			data.add(counterparty);
		}

		tr.commit();
		session.close();

		counterpartiesTable.setItems(data);

		addFilter();
	}
	
	private void addFilter() {
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

	void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;      
    }

	private void getSessionData(){
		sessFact = HibernateUtil.getSessionFactory();
	}

	private void clearForm(){
		addEditBtn.setText("Добавить");

		nameTextField.setText("");
		unnTextField.setText("");
		adressTextField.setText("");
	}
}
