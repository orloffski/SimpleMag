package entity;


public class Invoices_Headers {

  private long id;
  private String number;
  private String type;
  private String status;
  private String counterparty;
  private long count;
  private double summ;
  private long counterparty_Id;
  private java.sql.Timestamp lastcreated;
  private long recipient_Id;
  private String recipient_Name;
  private double full_Summ;
  private String ttn_Number;
  private String ttn_Date;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }


  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  public String getCounterparty() {
    return counterparty;
  }

  public void setCounterparty(String counterparty) {
    this.counterparty = counterparty;
  }


  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }


  public double getSumm() {
    return summ;
  }

  public void setSumm(double summ) {
    this.summ = summ;
  }


  public long getCounterparty_Id() {
    return counterparty_Id;
  }

  public void setCounterparty_Id(long counterparty_Id) {
    this.counterparty_Id = counterparty_Id;
  }


  public java.sql.Timestamp getLastcreated() {
    return lastcreated;
  }

  public void setLastcreated(java.sql.Timestamp lastcreated) {
    this.lastcreated = lastcreated;
  }


  public long getRecipient_Id() {
    return recipient_Id;
  }

  public void setRecipient_Id(long recipient_Id) {
    this.recipient_Id = recipient_Id;
  }


  public String getRecipient_Name() {
    return recipient_Name;
  }

  public void setRecipient_Name(String recipient_Name) {
    this.recipient_Name = recipient_Name;
  }


  public double getFull_Summ() {
    return full_Summ;
  }

  public void setFull_Summ(double full_Summ) {
    this.full_Summ = full_Summ;
  }


  public String getTtn_Number() {
    return ttn_Number;
  }

  public void setTtn_Number(String ttn_Number) {
    this.ttn_Number = ttn_Number;
  }


  public String getTtn_Date() {
    return ttn_Date;
  }

  public void setTtn_Date(String ttn_Date) {
    this.ttn_Date = ttn_Date;
  }

}
