package dbhelpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class SalesLinesDBHelper extends AbstractDBHelper{

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
