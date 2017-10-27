package entity;


public class Sales_Header {

  private long id;
  private String sales_Number;
  private double summ;
  private String sales_Type;
  private String payment;
  private java.sql.Timestamp lastcreateupdate;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getSales_Number() {
    return sales_Number;
  }

  public void setSales_Number(String sales_Number) {
    this.sales_Number = sales_Number;
  }


  public double getSumm() {
    return summ;
  }

  public void setSumm(double summ) {
    this.summ = summ;
  }


  public String getSales_Type() {
    return sales_Type;
  }

  public void setSales_Type(String sales_Type) {
    this.sales_Type = sales_Type;
  }


  public String getPayment() {
    return payment;
  }

  public void setPayment(String payment) {
    this.payment = payment;
  }


  public java.sql.Timestamp getLastcreateupdate() {
    return lastcreateupdate;
  }

  public void setLastcreateupdate(java.sql.Timestamp lastcreateupdate) {
    this.lastcreateupdate = lastcreateupdate;
  }

}
