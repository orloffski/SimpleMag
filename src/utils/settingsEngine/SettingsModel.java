package utils.settingsEngine;

public class SettingsModel {
    public boolean productsInStockEnabled;
    public boolean invoicesFromStock;
    public boolean sellsFromStock;

    public boolean autoVat;
    public boolean autoExtraPrice;

    public boolean autoBackupEnabled;
    public boolean autoBackupOnStart;
    public boolean autoBackupOnStop;
    public String autoBackupPath;

    public boolean sendBackupToEmail;
    public String smtpHostAdress;
    public String smtpHostPort;
    public String senderLogin;
    public String senderPassword;
    public String sendTo;
}
