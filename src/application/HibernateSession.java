package application;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import utils.HibernateUtil;

public class HibernateSession {

    private static SessionFactory sessFact;
    private static Session session;
    private static org.hibernate.Transaction tr;

    public static final void initSession(){
        if(sessFact == null)
            sessFact = HibernateUtil.getSessionFactory();

        if(session == null)
            session = sessFact.getCurrentSession();

        if(tr == null)
            tr = session.beginTransaction();
    }

    public static final void closeSession(){
        sessFact.close();
    }

    public static Session getSession() {
        return session;
    }

    public static Transaction getTr() {
        return tr;
    }
}
