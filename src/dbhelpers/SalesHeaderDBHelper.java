package dbhelpers;

import entity.SalesHeaderEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class SalesHeaderDBHelper extends AbstractDBHelper{

    public static SalesHeaderEntity getSalesHeaderEntityBySaleNumber(SessionFactory sessFact, String salesNumber){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        Query query = session.createQuery("FROM SalesHeaderEntity WHERE salesNumber =:salesNumber");
        query.setParameter("salesNumber", salesNumber);
        query.setMaxResults(1);

        List<SalesHeaderEntity> salesList = query.list();

        tr.commit();
        session.close();

        if(salesList.size() > 0)
            return salesList.get(0);

        return null;
    }

    public static List<SalesHeaderEntity> getSalesHeadersEntitiesList(SessionFactory sessFact){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        List<SalesHeaderEntity> invoicesHeadersList = session.createQuery("FROM SalesHeaderEntity ").list();

        tr.commit();
        session.close();

        return invoicesHeadersList;
    }

    public static void deleteHeaderById(SessionFactory sessFact, int headerId){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        Query query = session.createQuery("DELETE FROM SalesHeaderEntity WHERE id =:headerId");
        query.setParameter("headerId", headerId);
        query.executeUpdate();

        tr.commit();
        session.close();
    }
}
