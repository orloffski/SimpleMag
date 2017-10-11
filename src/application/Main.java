package application;
	
import java.io.IOException;

import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.TimelineBuilder;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.util.Duration;
import view.MainController;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorAdjustBuilder;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	private Stage primaryStage;
    private BorderPane rootLayout;
    private Scene scene;
	
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("МиниМаг");
        
        this.primaryStage.getIcons().add(new Image("file:resources/images/shop.png"));

        initRootLayout();

        showMainView();
    }
    
    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            scene = new Scene(rootLayout);

            initMainPaneImages();
            
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void initMainPaneImages() {
    	ImageView cashbox = (ImageView)scene.lookup("#cashbox");
    	cashbox.setImage(new Image("file:resources/images/cashbox.png")); 
    	
    	ImageView stock = (ImageView)scene.lookup("#stock");
        stock.setImage(new Image("file:resources/images/stock.png"));   
        
        ImageView finance = (ImageView)scene.lookup("#finance");
        finance.setImage(new Image("file:resources/images/finance.png")); 
        
        ImageView chart = (ImageView)scene.lookup("#chart");
        chart.setImage(new Image("file:resources/images/chart.png"));
    }
    
    public void showMainView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/view/MainView.fxml"));
            AnchorPane mainView = (AnchorPane) loader.load();
            
            MainController controller = loader.getController();
            controller.setMain(this);

            rootLayout.setCenter(mainView);
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