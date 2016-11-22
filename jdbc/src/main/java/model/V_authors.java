package model;

public class V_authors {
  private Long author_id;
  private String f_name;
  private String i_name;
  private String o_name;
  private java.sql.Date dob;
  private String country_name;
  private Long cnt_book;

  public Long getAuthor_id() {
    return author_id;
  }

  public void setAuthor_id(Long author_id) {
    this.author_id = author_id;
  }

  public String getF_name() {
    return f_name;
  }

  public void setF_name(String f_name) {
    this.f_name = f_name;
  }

  public String getI_name() {
    return i_name;
  }

  public void setI_name(String i_name) {
    this.i_name = i_name;
  }

  public String getO_name() {
    return o_name;
  }

  public void setO_name(String o_name) {
    this.o_name = o_name;
  }

  public java.sql.Date getDob() {
    return dob;
  }

  public void setDob(java.sql.Date dob) {
    this.dob = dob;
  }

  public String getCountry_name() {
    return country_name;
  }

  public void setCountry_name(String country_name) {
    this.country_name = country_name;
  }

  public Long getCnt_book() {
    return cnt_book;
  }

  public void setCnt_book(Long cnt_book) {
    this.cnt_book = cnt_book;
  }
}
