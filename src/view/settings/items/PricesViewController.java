package view.settings.items;

import dbhelpers.PricesDBHelper;
import entity.PricesEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Items;
import model.Prices;
import org.hibernate.query.Query;
import view.AbstractController;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class PricesViewController extends AbstractController{
	
	private ObservableList<Prices> data;
	private static int itemId;
	
	@FXML
	private Label itemLabel;
	
	@FXML
	private Label priceLabel;
	
	@FXML
	private Button changePriceBtn;
	
	@FXML
	private Button closeBtn;
	
	@FXML
	private TextField priceChangeField;

	@FXML
	private void initialize() {
		getSessionData();
	}
	
	public void buildData(int id){
		data = FXCollections.observableArrayList();

		PricesEntity pricesEntity = PricesDBHelper.getLastPriceByItemId(sessFact, id);

		if(pricesEntity != null)
			data.add(new Prices(pricesEntity.getId(),
					pricesEntity.getPrice(),
					pricesEntity.getItemId()));

	}
	
	void setItem(Items item) {
		itemLabel.setText(item.getVendorCode() + " " + item.getName());
		itemId = item.getId();
		buildData(itemId);
		
		priceLabel.setText(data.isEmpty() ? "0" : data.get(0).getPrice());
	}
	
	@FXML
	private void close() {
		dialogStage.close();
	}
	
	@FXML
	private void addNewPrice() {
		PricesDBHelper.saveEntity(sessFact,
				new PricesEntity(0,
				priceChangeField.getText(),
				itemId,
				new Timestamp(new Date().getTime()),
				"manual"));

		data.clear();
		buildData(itemId);
		clearForm();
	}

	@Override
	protected void clearForm() {
		priceLabel.setText(data.isEmpty() ? "0" : data.get(0).getPrice());
		priceChangeField.setText("");
	}
}
