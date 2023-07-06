package trabalho.entities.users;

import trabalho.entities.Turma;
import javax.persistence.Entity;

import trabalho.Database;

import java.util.List;

import java.util.ArrayList;

import javax.persistence.Column;

@Entity
public class Teacher extends User {
  public Teacher() {
    super();
  }

  public Teacher(String email, String name, String password, String phoneNumber, String cpf) {
    super(email, password);
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.cpf = cpf;
  }

  public String getName() {
    return this.name;
  }

  public String getPhoneNumber() {
    return this.phoneNumber;
  }

  public String getCPF() {
    return this.cpf;
  }

  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private String phoneNumber;
  @Column(nullable = false)
  private String cpf;

  public List<Turma> getTurmas() {
    List<Turma> turmasRetorno = new ArrayList<>();
    List<Turma> turmas = Database.readAll(Turma.class);
    for (Turma turma : turmas) {
      if (turma.getProfessoresMap().containsValue(this)) {
        turmasRetorno.add(turma);
      }
    }
    return turmasRetorno;
  }
}