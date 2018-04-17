package dbhelpers;

import entity.InvoicesHeadersEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class InvoicesHeaderDBHelper extends AbstractDBHelper {

    public static  InvoicesHeadersEntity getInvoiceHeaderEntityById(SessionFactory sessFact, int headerId){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        Query query = session.createQuery("FROM InvoicesHeadersEntity WHERE id =:headerId");
        query.setParameter("headerId", headerId);
        query.setMaxResults(1);

        List<InvoicesHeadersEntity> invoicesList = query.list();

        tr.commit();
        session.close();

        if(invoicesList.size() > 0)
            return invoicesList.get(0);

        return null;
    }

    public static List<InvoicesHeadersEntity> getInvoicesHeadersEntitiesList(SessionFactory sessFact, boolean reverse, String viewType){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();
        StringBuilder stringBuilder = new StringBuilder();

        List<InvoicesHeadersEntity> invoicesHeadersList;

        stringBuilder.append("FROM InvoicesHeadersEntity");
        if(viewType.equalsIgnoreCase("проведенные"))
            stringBuilder.append(" WHERE status = 'проведен'");
        if(viewType.equalsIgnoreCase("не проведенные"))
            stringBuilder.append(" WHERE status = 'не проведен'");
        if(reverse)
            stringBuilder.append(" ORDER BY id DESC");

        Query query = session.createQuery(stringBuilder.toString());

        invoicesHeadersList = query.list();

        tr.commit();
        session.close();

        return invoicesHeadersList;
    }

    public static void deleteHeaderById(SessionFactory sessFact, int headerId){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        Query query = session.createQuery("DELETE FROM InvoicesHeadersEntity WHERE id =:headerId");
        query.setParameter("headerId", headerId);
        query.executeUpdate();

        tr.commit();
        session.close();
    }

    public static  InvoicesHeadersEntity getInvoiceHeaderEntityByNum(SessionFactory sessFact, String invoiceNum){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        Query query = session.createQuery("FROM InvoicesHeadersEntity WHERE number =:invoiceNum");
        query.setParameter("invoiceNum", invoiceNum);
        query.setMaxResults(1);

        List<InvoicesHeadersEntity> invoicesList = query.list();

        tr.commit();
        session.close();

        if(invoicesList.size() > 0)
            return invoicesList.get(0);

        return null;
    }

    public static int getCounterpartyIdByInvoiceNumber(SessionFactory sessFact, String invoiceNumber){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        Query query = session.createQuery("FROM InvoicesHeadersEntity WHERE number =:invoiceNumber");
        query.setParameter("invoiceNumber", invoiceNumber);
        query.setMaxResults(1);

        List<InvoicesHeadersEntity> invoicesList = query.list();

        tr.commit();
        session.close();

        if(invoicesList.size() > 0)
            return invoicesList.get(0).getCounterpartyId();

        return 0;
    }

    public static List<InvoicesHeadersEntity> getHeadersByCounterpartyId(SessionFactory sessFact, int counterpartyId){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        Query query = session.createQuery(
                "FROM InvoicesHeadersEntity " +
                "WHERE counterpartyId =:counterpartyId " +
                    "AND (type ='Поступление' OR type ='Ввод начальных остатков') " +
                        "AND status ='проведен' " +
                "ORDER BY id DESC"
        );
        query.setParameter("counterpartyId", counterpartyId);

        List<InvoicesHeadersEntity> invoicesList = query.list();

        tr.commit();
        session.close();

        if(invoicesList.size() > 0)
            return invoicesList;

        return null;
    }

    public static List<InvoicesHeadersEntity> getHeaders(SessionFactory sessFact){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        Query query = session.createQuery(
                "FROM InvoicesHeadersEntity " +
                        "WHERE status ='проведен' " +
                        "AND (type ='Поступление' OR type ='Ввод начальных остатков') " +
                        "ORDER BY id DESC"
        );

        List<InvoicesHeadersEntity> invoicesList = query.list();

        tr.commit();
        session.close();

        if(invoicesList.size() > 0)
            return invoicesList;

        return null;
    }
}
