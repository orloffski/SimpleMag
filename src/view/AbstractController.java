package view;

import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utils.HibernateUtil;

public abstract class AbstractController {
    protected Stage dialogStage;

    protected Session session;
    protected SessionFactory sessFact;
    protected org.hibernate.Transaction tr;

    protected abstract void clearForm();

    protected void getSessionData(){
        sessFact = HibernateUtil.getSessionFactory();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
