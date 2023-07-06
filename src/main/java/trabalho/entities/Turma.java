package trabalho.entities;

import javax.persistence.*;

import trabalho.entities.users.Student;
import trabalho.entities.users.Teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "turma")
public class Turma {
    public static enum Disciplina {
        algebra,
        matematica,
        geometria,
        portugues,
        geografia,
        historia,
        ciencias,
        fisica,
        quimica,
        biologia,
        literatura,
        filosofia,
        sociologia,
        educacaoFisica;

        public String converte() {
            switch (this) {
                case algebra:
                    return "Álgebra";
                case matematica:
                    return "Matemática";
                case geometria:
                    return "Geometria";
                case portugues:
                    return "Português";
                case geografia:
                    return "Geografia";
                case historia:
                    return "História";
                case ciencias:
                    return "Ciências";
                case fisica:
                    return "Física";
                case quimica:
                    return "Química";
                case biologia:
                    return "Biologia";
                case filosofia:
                    return "Filosofia";
                case sociologia:
                    return "Sociologia";
                case educacaoFisica:
                    return "Educacão Física";
                case literatura:
                    return "Literatura";
                default:
                    return "???";
            }
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Disciplina> disciplinas = new ArrayList<>();

    @Column(nullable = false)
    private int ano;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "turma_professor",
            joinColumns = @JoinColumn(name = "turma_id"),
            inverseJoinColumns = @JoinColumn(name = "professor_id"))
    private Map<Disciplina, Teacher> professoresMap = new HashMap<>();

    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Student> alunos;

    public Turma() {
        this.alunos = new ArrayList<>();
    }

    public Turma(int ano) {
        this.ano = ano;
        this.alunos = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public Map<Disciplina, Teacher> getProfessoresMap() {
        return professoresMap;
    }

    public void setProfessoresMap(Map<Disciplina, Teacher> professoresMap) {
        this.professoresMap = professoresMap;
    }

    public List<Student> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Student> alunos) {
        this.alunos = alunos;
    }
}