package utils.settingsEngine;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class SettingsEngine {
    private final static String SETTINGS_FILE = "src/application/settings.xml";

    private static volatile SettingsEngine instance;
    private SettingsModel settings;

    public static SettingsEngine getInstance() {
        SettingsEngine localInstance = instance;
        if (localInstance == null) {
            synchronized (SettingsEngine.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new SettingsEngine();
                }
            }
        }
        return localInstance;
    }

    private SettingsEngine(){
        loadSettingsDataFromFile();
    }

    public SettingsModel getSettings() {
        return settings;
    }

    private void loadSettingsDataFromFile() {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(SettingsWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Чтение XML из файла и демаршализация.
            SettingsWrapper wrapper = (SettingsWrapper) um.unmarshal(new File(SETTINGS_FILE));

            // читаем настройки в переменную
            this.settings = wrapper.getSettingsModel();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveSettingsDataToFile(SettingsModel settings) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(SettingsWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Обёртываем наши данные.
            SettingsWrapper wrapper = new SettingsWrapper();
            wrapper.setSettingsModel(settings);

            // Маршаллируем и сохраняем XML в файл.
            m.marshal(wrapper, new File(SETTINGS_FILE));
        } catch (Exception e) {
        }
    }
}
