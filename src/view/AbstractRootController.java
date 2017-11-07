package view;

import application.Main;
import javafx.scene.control.TabPane;

public abstract class AbstractRootController {
    protected Main main;
    protected TabPane rootTab;

    public void setMain(Main main) {
        this.main = main;
    }

    public void setTabPane(TabPane rootTab){
        this.rootTab = rootTab;
    }
}
