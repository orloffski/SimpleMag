package dbhelpers;

import entity.SalesLineEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

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
}
