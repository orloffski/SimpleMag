package entity;

import org.hibernate.annotations.Entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sales_line")
public class Sales_Line implements Serializable {

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  @Column(name = "sales_number")
  private String sales_Number;

  @Column(name = "item_id")
  private long item_Id;

  @Column(name = "item_name")
  private String item_Name;

  @Column(name = "count")
  private long count;

  @Column(name = "item_price")
  private double item_Price;

  @Column(name = "full_line_price")
  private double full_Line_Price;

  public Sales_Line() {
  }

  public Sales_Line(String sales_Number, long item_Id, String item_Name, long count, double item_Price, double full_Line_Price) {
    this.sales_Number = sales_Number;
    this.item_Id = item_Id;
    this.item_Name = item_Name;
    this.count = count;
    this.item_Price = item_Price;
    this.full_Line_Price = full_Line_Price;
  }

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


  public long getItem_Id() {
    return item_Id;
  }

  public void setItem_Id(long item_Id) {
    this.item_Id = item_Id;
  }


  public String getItem_Name() {
    return item_Name;
  }

  public void setItem_Name(String item_Name) {
    this.item_Name = item_Name;
  }


  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }


  public double getItem_Price() {
    return item_Price;
  }

  public void setItem_Price(double item_Price) {
    this.item_Price = item_Price;
  }


  public double getFull_Line_Price() {
    return full_Line_Price;
  }

  public void setFull_Line_Price(double full_Line_Price) {
    this.full_Line_Price = full_Line_Price;
  }

}
