package dbhelpers;

import entity.SalesLineEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SalesLinesDBHelper extends AbstractDBHelper{

    public static List<SalesLineEntity> getLinesBySalesNumber(SessionFactory sessFact, String salesNumber){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        Query query = session.createQuery("FROM SalesLineEntity WHERE salesNumber =:salesNumber");
        query.setParameter("salesNumber", salesNumber);
        List<SalesLineEntity> linesList = query.list();

        tr.commit();
        session.close();

        return linesList;
    }

    public static void deleteLinesBySalesNumber(SessionFactory sessFact, String salesNumber){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        Query query = session.createQuery("DELETE FROM SalesLineEntity WHERE salesNumber =:salesNumber");
        query.setParameter("salesNumber", salesNumber);
        query.executeUpdate();

        tr.commit();
        session.close();
    }

    public static List<SalesLineEntity> getLinesByHeadersList(SessionFactory sessFact, List<String> headersList){
        List<SalesLineEntity> linesList = new ArrayList<>();

        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        StringBuilder queryString = new StringBuilder();

        queryString.append("SELECT s.itemId, s.itemName, SUM(s.count), s.counterpartyId ");
        queryString.append("FROM SalesLineEntity s ");
        queryString.append("WHERE s.salesNumber IN(:headersList) ");
        queryString.append("GROUP BY s.itemId, s.counterpartyId ");
        queryString.append("ORDER BY s.counterpartyId");

        Query query = session.createQuery(queryString.toString());
        query.setParameterList("headersList", headersList);

        for(Iterator it = query.iterate(); it.hasNext();){
            Object[] row = (Object[])it.next();

            linesList.add(new SalesLineEntity(
                    0,
                    "",
                    (int)row[0],
                    (String)row[1],
                    (double)row[2],
                    0d,
                    0d,
                    (int)row[3]));
        }

        tr.commit();
        session.close();

        return linesList;
    }
}
