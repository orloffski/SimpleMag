package dbhelpers;

import entity.SalesReportsEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class SalesReportsDBHelper extends AbstractDBHelper  {
    public static List<SalesReportsEntity> getAll(SessionFactory sessFact){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        Query query = session.createQuery("FROM SalesReportsEntity ORDER BY date DESC");

        List<SalesReportsEntity> datesList = query.list();

        tr.commit();
        session.close();

        return datesList;
    }
}
