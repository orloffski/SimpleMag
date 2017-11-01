package application;

import org.hibernate.SessionFactory;
import utils.HibernateUtil;

public class HibernateSession {

    private static SessionFactory sessFact;

    public static final void initSession(){
        if(sessFact == null)
            sessFact = HibernateUtil.getSessionFactory();
    }

    public static final void closeSession(){
        sessFact.close();
    }

    public static SessionFactory getSessFact() {
        return sessFact;
    }
}
