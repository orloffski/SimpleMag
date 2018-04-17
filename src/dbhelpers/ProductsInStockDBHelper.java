package dbhelpers;

import application.DBClass;
import entity.InvoicesHeadersEntity;
import entity.InvoicesLinesEntity;
import entity.ProductsInStockEntity;
import model.InvoicesTypes;
import model.ItemsCount;
import model.StatusTypes;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductsInStockDBHelper extends AbstractDBHelper {

    public static void deleteByInvoiceNumber(SessionFactory sessFact, String invoiceNumber){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        Query query = session.createQuery("DELETE FROM ProductsInStockEntity WHERE invoiceNumber =:invoiceNumber");
        query.setParameter("invoiceNumber", invoiceNumber);
        query.executeUpdate();

        tr.commit();
        session.close();
    }

    public static List<ProductsInStockEntity> findItemCountFromCounterparty(SessionFactory sessFact, int itemId, int counerpartyId, String expireDate){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();
        StringBuilder queryString = new StringBuilder();

        queryString.append("FROM ProductsInStockEntity WHERE itemId =").append(itemId);
        if(counerpartyId != -1)
            queryString.append(" AND counterpartyId =").append(counerpartyId);
        if(!expireDate.equalsIgnoreCase(""))
            queryString.append(" AND expireDate =").append(expireDate);

        Query query = session.createQuery(queryString.toString());

        List<ProductsInStockEntity> list = query.list();

        tr.commit();
        session.close();

        return list;
    }

    public static double getItemsCount(SessionFactory sessFact, InvoicesLinesEntity line, InvoicesHeadersEntity header){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();
        StringBuilder queryString = new StringBuilder();
        boolean needCounterparty = false;

        if(header.getType().equalsIgnoreCase(InvoicesTypes.RECEIPT.toString()) &&
                header.getStatus().equalsIgnoreCase(StatusTypes.ENTERED.toString()) ||
                header.getType().equalsIgnoreCase(InvoicesTypes.INITIAL.toString()) &&
                        header.getStatus().equalsIgnoreCase(StatusTypes.ENTERED.toString()) ||
                header.getType().equalsIgnoreCase(InvoicesTypes.RETURN.toString()))
            needCounterparty = true;

        queryString.append("SELECT SUM(itemsCount) FROM ProductsInStockEntity WHERE itemId =").append(line.getItemId());
        if(needCounterparty)
            queryString.append(" AND counterpartyId =").append(header.getCounterpartyId());
        if(!line.getExpireDate().equalsIgnoreCase(""))
            queryString.append(" AND expireDate =").append(line.getExpireDate());

        Query query = session.createQuery(queryString.toString());

        double counts = (double)query.list().get(0);

        tr.commit();
        session.close();

        return counts;
    }

    public static ProductsInStockEntity fullFindLines(SessionFactory sessFact, int itemId, int counerpartyId, String expireDate){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();
        StringBuilder queryString = new StringBuilder();

        queryString.append("FROM ProductsInStockEntity WHERE itemId =").append(itemId);
        if(counerpartyId != -1)
            queryString.append(" AND counterpartyId =").append(counerpartyId);
        if(!expireDate.equalsIgnoreCase(""))
            queryString.append(" AND expireDate =").append(expireDate);
        queryString.append(" ORDER BY id DESC");

        Query query = session.createQuery(queryString.toString());

        List<ProductsInStockEntity> list = query.list();

        tr.commit();
        session.close();

        if(list.size() > 0)
            return list.get(0);

        return null;
    }

    // получение товаров поставщика на складе
    public static List<ItemsCount> getStockItemsByCounterparty(int counterpartyId){
        Connection connection;
        List<ItemsCount> items = new ArrayList<>();

        try {
            connection = new DBClass().getConnection();
            String SQL = "SELECT products_in_stock.item_id as item, SUM(products_in_stock.items_count) as count " +
                    "FROM products_in_stock " +
                    "WHERE counterparty_id =" + counterpartyId + " " +
                    "GROUP BY item";
            ResultSet rs = connection.createStatement().executeQuery(SQL);
            while(rs.next()){
                items.add(new ItemsCount(rs.getInt("item"), rs.getInt("count")));
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        if(items.size() > 0)
            return items;

        return null;
    }
}
