package dbhelpers;

import entity.SalesHeaderEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
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

    public static List<SalesHeaderEntity> getSalesHeadersEntitiesList(SessionFactory sessFact, String salesType){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("FROM SalesHeaderEntity");
        if(salesType.equalsIgnoreCase("проведенные"))
            stringBuilder.append(" WHERE setHeader = 'проведен'");
        if(salesType.equalsIgnoreCase("не проведенные"))
            stringBuilder.append(" WHERE setHeader = 'не проведен'");

        Query query = session.createQuery(stringBuilder.toString());

        List<SalesHeaderEntity> invoicesHeadersList = query.list();

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

    public static List<SalesHeaderEntity> getSalesFromDateToDate(SessionFactory sessFact, LocalDate dateFrom, LocalDate dateTo){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        StringBuilder queryString = new StringBuilder();
        queryString.append("FROM SalesHeaderEntity WHERE setHeader = 'проведен' AND DATE(lastcreateupdate) BETWEEN '");
        queryString.append(dateFrom.toString());
        queryString.append("' AND '");
        queryString.append(dateTo.toString());
        queryString.append("'");

        Query query = session.createQuery(queryString.toString());

        List<SalesHeaderEntity> salesList = query.list();

        tr.commit();
        session.close();

        return salesList;
    }
}
