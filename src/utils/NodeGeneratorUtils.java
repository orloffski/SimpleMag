package utils;

import application.Main;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import modes.TabMode;
import view.AbstractRootController;

import java.io.IOException;

public class NodeGeneratorUtils {

    public static final void openNewTab(TabPane rootTab, TabMode mode, Main main) throws IOException {
        BorderPane paneView = null;
        FXMLLoader loader = new FXMLLoader();
        String tabTitle = "";
        String tabId = mode.toString();

        if(checkIssetTab(rootTab, mode))
            return;

        switch (mode){
            case CASHBOX:
                tabTitle = "Продажи";
                loader.setLocation(Main.class.getResource("/view/CashView.fxml"));
                paneView = loader.load();
                break;
            case STOCK:
                tabTitle = "Склад";
                loader.setLocation(Main.class.getResource("/view/StockView.fxml"));
                paneView = loader.load();
                break;
            case FINANCE:
                tabTitle = "Финансы";
                loader.setLocation(Main.class.getResource("/view/FinanceView.fxml"));
                paneView = loader.load();
                break;
            case CHARTS:
                tabTitle = "Статистика";
                loader.setLocation(Main.class.getResource("/view/ChartView.fxml"));
                paneView = loader.load();
                break;
            case SETTINGS:
                tabTitle = "Настройки";
                loader.setLocation(Main.class.getResource("/view/SettingsView.fxml"));
                paneView = loader.load();
                break;
            case UNITS_LIST:
                tabTitle = "Список единиц измерения";
                loader.setLocation(Main.class.getResource("/view/settings/UnitsDirectoryView.fxml"));
                paneView = loader.load();
                break;
            case COUNTERPARTIES_LIST:
                tabTitle = "Список контрагентов";
                loader.setLocation(Main.class.getResource("/view/settings/CounterpartiesDirectoryView.fxml"));
                paneView = loader.load();
                break;
            case ITEMS_LIST:
                tabTitle = "Список товаров";
                loader.setLocation(Main.class.getResource("/view/settings/items/ItemsView.fxml"));
                paneView = loader.load();
                break;
            case INVOICES_LIST:
                tabTitle = "Накладные";
                loader.setLocation(Main.class.getResource("/view/stockviews/invoices/InvoicesView.fxml"));
                paneView = loader.load();
                break;
            case PRODUCTS_IN_STOCK_SETTINGS:
                tabTitle = "Настройка контроля остатков на складе";
                loader.setLocation(Main.class.getResource("/view/settings/productsInStock/ProductsInStockSettings.fxml"));
                paneView = loader.load();
                break;
        }

        AbstractRootController controller = loader.getController();
        controller.setMain(main);
        controller.setTabPane(rootTab);

        Tab tab = new Tab(tabTitle);
        rootTab.getTabs().add(tab);

        tab.setId(tabId);
        tab.setContent(paneView);

        rootTab.getSelectionModel().select(tab);
    }

    private static final boolean checkIssetTab(TabPane rootTab, TabMode mode){
        ObservableList<Tab> tabs = rootTab.getTabs();

        for(Tab tab : tabs){
            if(tab.getId().equals(mode.toString())) {
                rootTab.getSelectionModel().select(tab);
                return true;
            }
        }

        return false;
    }
}
