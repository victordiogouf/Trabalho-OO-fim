package trabalho.entities.users;

import javax.persistence.InheritanceType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class User {
  public User() {}
  public User(String email, String password) {
    this.email = email;
    this.password = password;
  }
  
  public String getEmail() { return email; }
  public String getPassword() { return password; }

  @Id
  private String email;
  @Column(nullable = false)
  private String password;
}