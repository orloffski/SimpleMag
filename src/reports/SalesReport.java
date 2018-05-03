package reports;

import org.hibernate.SessionFactory;
import view.AbstractController;

public class SalesReport implements Runnable {

    private Thread t;
    private SessionFactory sessFact;
    private AbstractController controller;

    public SalesReport(SessionFactory sessFact, AbstractController controller) {
        this.controller = controller;
        this.sessFact = sessFact;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {

    }
}
