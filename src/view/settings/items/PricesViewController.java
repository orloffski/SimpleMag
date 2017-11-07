package view.settings.items;

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

		session = sessFact.openSession();
		tr = session.beginTransaction();

		Query query = session.createQuery("FROM PricesEntity WHERE itemId =:id ORDER BY lastcreated DESC");
		query.setParameter("id", id);
		query.setMaxResults(1);

		List<PricesEntity> pricesList = query.list();

		if(pricesList.size() > 0)
			data.add(new Prices(pricesList.get(0).getId(),
					pricesList.get(0).getPrice(),
					pricesList.get(0).getItemId()));

		tr.commit();
		session.close();
	}
	
	void setItem(Items item) {
		itemLabel.setText(item.getVendorCode().toString() + " " + item.getName().toString());
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
		session = sessFact.openSession();
		tr = session.beginTransaction();

		session.save(new PricesEntity(0,
				priceChangeField.getText().toString(),
				itemId,
				new Timestamp(new Date().getTime()),
				"manual"));

		tr.commit();
		session.close();

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
