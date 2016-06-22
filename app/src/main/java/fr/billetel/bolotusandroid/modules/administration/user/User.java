package fr.billetel.bolotusandroid.modules.administration.user;

/**
 * Created by Maël Gargadennnec on 17/06/2016.
 */
public class User {
  private String id;
  private String firstname;
  private String lastname;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }
}
