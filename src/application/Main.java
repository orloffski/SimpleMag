package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view.MainController;

import java.io.IOException;


public class Main extends Application {

	private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        HibernateSession.initSession();

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("МиниМаг");
        
        this.primaryStage.getIcons().add(new Image("file:resources/images/shop.png"));

        showMainView();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        HibernateSession.closeSession();
    }
    
    private void showMainView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/view/MainView.fxml"));
            BorderPane mainView = loader.load();

            Scene scene = new Scene(mainView);

            primaryStage.setScene(scene);
            MainController controller = loader.getController();
            controller.setMain(this);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Stage getPrimaryStage() {
        return primaryStage;
    }
	
	public static void main(String[] args) {
		launch(args);
	}
}