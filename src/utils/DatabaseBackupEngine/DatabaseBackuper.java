package utils.DatabaseBackupEngine;

import application.HibernateSession;
import utils.ZipUtils;
import utils.settingsEngine.SettingsEngine;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class DatabaseBackuper {

    public static void backupDatabase() {
        try {
            /* Get database url */
            String dbName = HibernateSession.getSessFact().getProperties()
                    .get("hibernate.connection.url").toString().split(Pattern.quote("/"))[3];

            /* Get database backup path */
            String folderPath = SettingsEngine.getInstance().getSettings().autoBackupPath;

            /* Add folder to backup - date-time */
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss-mmm'Z'");
            String strDate = f.format(new Date());
            folderPath += "\\" + strDate;
            File f1 = new File(folderPath);
            f1.mkdir();

            /* backup file */
            String savePath = folderPath + "\\" + "backup.sql\"";

            /* Used to create a cmd command */
            String executeCmd = "C:\\mysql\\bin\\mysqldump -uroot -pmcl65ren --database " + dbName + " > " + savePath;

            /* Executing the command here */
            Process exec = Runtime.getRuntime().exec(
                    new String[]{"cmd.exe","/c",executeCmd}
            );
            int processComplete = exec.waitFor();

            /*NOTE: processComplete=0 if correctly executed, will contain other values if not*/
            if (processComplete == 0) {

                /* zip database backup */
                ZipUtils.zipFolder(f1, new File(folderPath + ".zip"));

                /* delete directory - we have zip archive*/
                deleteDirectory(f1);
            } else {
                System.out.println("Backup Failure");
            }

        } catch (IOException | InterruptedException ex) {

        }
    }

    public static void deleteDirectory(File outputFolder){
        final File[] files = outputFolder.listFiles();
        for (File f: files) f.delete();
        outputFolder.delete();
    }
}
