package utils.settingsEngine;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "main_app_settings")
public class SettingsWrapper {

    private SettingsModel settingsModel;

    @XmlElement(name = "products_in_stock")
    public SettingsModel getSettingsModel() {
        return settingsModel;
    }

    public void setSettingsModel(SettingsModel settingsModel) {
        this.settingsModel = settingsModel;
    }
}
