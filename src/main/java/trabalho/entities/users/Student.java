package trabalho.entities.users;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import trabalho.entities.Turma;

import javax.persistence.Column;

@Entity
public class Student extends User {
  public Student() {
    super();
  }

  public Student(String email, String name, String password, String phoneNumber, String cpf) {
    super(email, password);
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.cpf = cpf;

    this.grades = "";
    for (int i = 0; i < Turma.Disciplina.values().length; ++i)
      this.grades += "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,";
    this.grades = this.grades.substring(0, this.grades.length() - 1); // tira virgula final
  }

  public String getName() {
    return name;
  }

  public String getCPF() {
    return cpf;
  }

  public String getPhoneNumber() {
    return this.phoneNumber;
  }

  public Turma getTurma() {
    return this.turma;
  }

  public void setTurma(Turma t) {
    this.turma = t;
  }

  // bimestre de 0 a 3
  // prova de 0 a 3
  public Float getGrade(Turma.Disciplina disciplina, int bimestre, int prova) throws Exception {
    if (bimestre < 0 || bimestre > 3 || prova < 0 || prova > 3) {
      throw new Exception("Bimestre ou prova inválida");
    }
    String[] gradesArray = this.grades.split(",");
    String gradeStr = gradesArray[disciplina.ordinal() * 16 + bimestre * 4 + prova];
    return Float.parseFloat(gradeStr);
  }

  // bimestre de 0 a 3
  // prova de 0 a 3
  public void setGrade(Turma.Disciplina disciplina, int bimestre, int prova, float grade) throws Exception {
    if (bimestre < 0 || bimestre >= 4 || prova < 0 || prova > 4) {
      throw new Exception("Bimestre ou prova inválida");
    }
    this.grades = updateGrade(disciplina.ordinal() * 16 + bimestre * 4 + prova, grade);
  }

  private String updateGrade(int index, float grade) {
    String[] gradesArray = this.grades.split(",");
    gradesArray[index] = String.valueOf(grade);
    return String.join(",", gradesArray);
  }

  @Column(nullable = false)
  private String name;
  @Column(nullable = false, length = 10000)
  private String grades;
  @Column(nullable = false)
  private String phoneNumber;
  @Column(nullable = false)
  private String cpf;
  @ManyToOne
  @JoinColumn(name = "turma_id")
  private Turma turma;
}