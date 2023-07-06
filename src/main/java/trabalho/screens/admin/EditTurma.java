package trabalho.screens.admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import trabalho.Database;
import trabalho.Dialog;
import trabalho.Router;
import trabalho.entities.Turma;
import trabalho.entities.Turma.Disciplina;
import trabalho.entities.users.Admin;
import trabalho.entities.users.Student;
import trabalho.entities.users.Teacher;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EditTurma extends JFrame {
  private JTable table;
  private Turma turma;
  private Admin adm;
  private JComboBox<String> alunoComboBox;

  public EditTurma(Turma turma, Admin adm) {
    this.turma = turma;
    this.adm = adm;
        Dialog.show(Integer.toString(this.turma.getDisciplinas().size()) + " disciplinas constructor");
    initialize();
  }

  private void initialize() {
    // Configurar a janela JFrame
    setTitle("Editar Turma");
    setSize(1280, 720);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setResizable(false);

    // Criar o painel principal JPanel com layout BorderLayout
    JPanel panel = new JPanel(new BorderLayout());

    // Criar o painel de título JPanel
    JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JLabel titleLabel = new JLabel("Turma " + this.turma.getId());
    titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
    titlePanel.add(titleLabel);

    // Adicionar o painel de título ao painel principal
    panel.add(titlePanel, BorderLayout.NORTH);

    // Criar o painel de informações da turma JPanel
    JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JLabel alunoLabel = new JLabel("Selecione um Aluno para editar na Turma: ");
    alunoComboBox = new JComboBox<>(getAlunos());
    alunoComboBox.setPreferredSize(new Dimension(300, 25)); // Definir tamanho da JComboBox
    alunoComboBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        EditTurma.this.updateAluno((String)alunoComboBox.getSelectedItem());
      }
    });
    infoPanel.add(alunoLabel);
    infoPanel.add(alunoComboBox);

    // Adicionar o painel de informações da turma ao painel principal
    panel.add(infoPanel, BorderLayout.CENTER);

    // Criar os títulos das colunas da tabela
    String[] columnNames = { "Disciplina", "Professor" };

    // Criar o modelo de dados da tabela
    DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
      // ...
      @Override
      public boolean isCellEditable(int row, int column) {
        // Retorna false para a coluna 1 (segunda coluna)
        return false;
      }
    };

    // Obter as disciplinas possíveis da enumeração Disciplina
    List<Disciplina> disciplinas = this.turma.getDisciplinas();

    // Percorrer todas as disciplinas
    for (Disciplina disciplina : disciplinas) {
      // Obter o professor associado à disciplina, se houver
      Teacher professor = turma.getProfessoresMap().get(disciplina);

      // Adicionar a disciplina e o nome do professor como uma linha no modelo de
      // dados da tabela
      model.addRow(
          new Object[] { disciplina.converte(), professor != null ? professor.getName() : "Sem professor" });
    }

    // Criar a tabela JTable com o modelo de dados
    table = new JTable(model);

    // Obtenha o modelo de seleção da tabela
    ListSelectionModel selectionModel = table.getSelectionModel();

    // Adicione um ouvinte de eventos de seleção à tabela
    selectionModel.addListSelectionListener(e -> {
      // Verifique se a seleção está ajustada e não está vazia
      if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
        // Obtenha a linha selecionada
        int selectedRow = table.getSelectedRow();

        // Obtenha os valores das células na linha selecionada
        Object disciplina = table.getValueAt(selectedRow, 0);
        Object professor = table.getValueAt(selectedRow, 1);

        EditTurma.this.addTeacherToDiscipline((String) disciplina, (String) professor);
      }
    });

    // Adicionar a tabela JTable a um JScrollPane
    JScrollPane scrollPane = new JScrollPane(table);

    // Adicionar o JScrollPane ao painel principal
    panel.add(scrollPane, BorderLayout.BEFORE_LINE_BEGINS);

    // Criar o painel de botões JPanel
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

    // Botão "Adicionar Aluno"
    JButton addButton = new JButton("Adicionar Aluno");
    addButton.addActionListener(e -> {
      EditTurma.this.addStudentToClasse(e);
    });
    buttonPanel.add(addButton);

    // Botão "Remover turmas"
    JButton removeButton = new JButton("Remover Turma");
    removeButton.addActionListener(e -> {
      for (Student s : this.turma.getAlunos()) {
        s.setTurma(null);
      }
      Database.delete(this.turma);
      JOptionPane.showMessageDialog(this, "A turma foi excluida com sucesso!");
      Router.getInstance().goTo(new TurmasAdmin(adm));
    });
    buttonPanel.add(removeButton);

    // Botão "Sair"
    JButton exitButton = new JButton("Voltar");
    exitButton.addActionListener(e -> {
      Dialog.show(Integer.toString(this.turma.getDisciplinas().size()) + " disciplinas exit");
      Router.getInstance().goTo(new TurmasAdmin(this.adm));
    }); // Fechar a janela
    buttonPanel.add(exitButton);

    // Adicionar o painel de botões ao painel principal
    panel.add(buttonPanel, BorderLayout.PAGE_END);

    // Adicionar o painel principal à janela JFrame
    getContentPane().add(panel);

    // Exibir a janela
    setVisible(true);
  }

  private void updateAluno(String selectedItem) {
    if (selectedItem.equals("Não Selecionado")) return;
    
    List<Student> students = this.turma.getAlunos();
    Student student = null;

    for (Student std : students) {
      if (std.getCPF().equals(selectedItem.split(" - ")[1])) {
        student = std;
        break;
      }
    }

    if (student == null) return;

    StringBuilder sb = new StringBuilder();
    sb.append("Nome: ").append(student.getName()).append("\n");
    sb.append("CPF: ").append(student.getCPF()).append("\n");
    sb.append("Email: ").append(student.getEmail()).append("\n");
    sb.append("Número de telefone: ").append(student.getPhoneNumber()).append("\n");

    Object[] options = { "Excluir da Turma", "Cancelar" };
    int choice = JOptionPane.showOptionDialog(this, sb.toString(), "Detalhes do Aluno",
        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);

    if (choice == 0) {
      try {
        this.turma.getAlunos().remove(student);   

        student.setTurma(null);
        
        Session session = Database.sessionFactory.openSession();
        Transaction transaction = null;

        try {
          transaction = session.beginTransaction();
          session.saveOrUpdate(this.turma);          
          session.saveOrUpdate(student);
          transaction.commit();
        } 
        catch (HibernateException e) {
          if (transaction != null) {
            transaction.rollback();
          }
          throw e;
        } 
        finally {
          session.close();
        }

        JOptionPane.showMessageDialog(this, "Aluno " + student.getName() + " foi removido(a) da Turma " + this.turma.getId() + "!");
        
        this.updateAlunoComboBox();
      } catch (Exception e) {
        Dialog.showError("Não é possível remover o aluno");
        Dialog.showError(e);
      }
    }

  }

  private void updateAlunoComboBox() {
    List<String> alunos = new ArrayList<>();

    alunos.add("Não Selecionado");

    // Adicionar os alunos já existentes na turma
    for (Student std : this.turma.getAlunos()) {
      String alunoExistente = std.getName() + " - " + std.getCPF();
      if (!alunos.contains(std.getName() + " - " + std.getCPF())) {
        alunos.add(alunoExistente);
      }
    }

    // Converter a lista para um array de strings
    String[] alunosArray = alunos.toArray(new String[0]);

    // Atualizar o modelo do ComboBox com os alunos
    alunoComboBox.setModel(new DefaultComboBoxModel<>(alunosArray));
  }

  private void addStudentToClasse(ActionEvent e) {
    List<Student> students = Database.readAll(Student.class);
    List<String> allStudents = new ArrayList<>();

    for (Student student : students) {
      boolean alunoEncontrado = false;
      for (Student std : this.turma.getAlunos()) {
        if (std.getCPF().equals(student.getCPF())) {
          alunoEncontrado = true;
          break;
        }
      }
      if (!alunoEncontrado) {
        String studentOptions = student.getName() + " - " + student.getCPF();
        allStudents.add(studentOptions);
      }
    }

    // Converter a lista para um array de strings
    String[] allStudentsArray = allStudents.toArray(new String[0]);

    JComboBox<String> studentComboBox = new JComboBox<>(allStudentsArray);

    // Opções do diálogo
    Object[] options = { "Adicionar Aluno", "Sair" };

    int choice = JOptionPane.showOptionDialog(this, studentComboBox, "Adicionar qual Aluno a essa Turma?",
        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

    if (choice == 0) {
      String novoAluno = (String) studentComboBox.getSelectedItem();
      List<Student> newList = this.turma.getAlunos();
      Student BDNewStudent = null;

      for (Student student : students) {
        if (novoAluno.split(" - ")[1].equals(student.getCPF())) {
          BDNewStudent = student;
          newList.add(student);
          break;
        }
      }

      BDNewStudent.setTurma(this.turma);
      this.turma.setAlunos(newList);
      try {
         Session session = Database.sessionFactory.openSession();
        Transaction transaction = null;

        try {
          transaction = session.beginTransaction();
          session.saveOrUpdate(this.turma);          
          session.saveOrUpdate(BDNewStudent);
          transaction.commit();
        } 
        catch (HibernateException ex) {
          if (transaction != null) {
            transaction.rollback();
          }
          throw ex;
        } 
        finally {
          session.close();
        }

        JOptionPane.showMessageDialog(this, "Novo aluno foi adicionado!");

        updateAlunoComboBox();
      } catch (Exception exc) {
        Dialog.showError("Não foi possível adicionar aluno");
      }
    }
  }

  private void addTeacherToDiscipline(String disciplina, String professorAtual) {
    // Obter a lista de professores disponíveis
    Dialog.show(Integer.toString(this.turma.getDisciplinas().size()) + " disciplinas");
    List<Teacher> teachers = Database.readAll(Teacher.class);

    // Criar um array para armazenar as opções dos professores
    String[] professorOptions = new String[teachers.size() + 1];
    professorOptions[0] = "Sem professor";

    // Preencher o array com as opções dos professores
    for (int i = 1; i < (teachers.size() + 1); i++) {
      Teacher teacher = teachers.get(i - 1);
      String professorOption = teacher.getName() + " - " + teacher.getCPF();
      professorOptions[i] = professorOption;
    }

    // Criar um JComboBox com as opções dos professores
    JComboBox<String> professorComboBox = new JComboBox<>(professorOptions);

    // Definir o professor atualmente associado à disciplina como seleção padrão na
    // combobox
    professorComboBox.setSelectedItem(professorAtual);

    // Opções do diálogo
    Object[] options = { "Salvar", "Sair" };

    // Exibir o diálogo
    int choice = JOptionPane.showOptionDialog(this, professorComboBox, "Professor que irá dar Disciplina?",
        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

    if (choice == 0) { // Clicou em "Salvar"
      // Obter o professor selecionado na combobox
      String novoProfessor = (String) professorComboBox.getSelectedItem();

      if (novoProfessor != null) {
        // Faça algo com o novo professor, como adicionar à disciplina
        for (Disciplina discipline : Disciplina.values()) {
          if (discipline.converte().equals(disciplina)) {
            this.turma.getProfessoresMap().remove(discipline);
            if (novoProfessor == "Sem professor")
              continue;
            for (Teacher teacher : teachers) {
              if (teacher.getCPF().equals(novoProfessor.split(" - ")[1])) {
                this.turma.getProfessoresMap().put(discipline, teacher);
              }
            }

          }
        }

        try {
          Database.update(this.turma);
          // Atualize a tabela com o novo nome do professor
          int rowIndex = -1;
          for (int i = 0; i < table.getRowCount(); i++) {
            Object disciplinaCellValue = table.getValueAt(i, 0);
            if (disciplinaCellValue.equals(disciplina)) {
              rowIndex = i;
              break;
            }
          }
          if (rowIndex != -1) {
            table.setValueAt(novoProfessor.split(" - ")[0], rowIndex, 1);
          }

          JOptionPane.showMessageDialog(this, "Professor atribuido com sucesso!");
          Dialog.show(Integer.toString(this.turma.getDisciplinas().size()) + " disciplinas");
        }
        catch (Exception e) {
          Dialog.showError("Não foi possível atribuir professor");
        }
      }
    }
  }

  private String[] getAlunos() {

    List<Student> students = Database.readAll(Student.class);
    List<String> allStudents = new ArrayList<>();
    allStudents.add("Não Selecionado");

    for (Student student : students) {
      for (Student std : this.turma.getAlunos()) {
        if (std.getCPF().equals(student.getCPF())) {
          allStudents.add(student.getName() + " - " + student.getCPF());
          break;
        }
      }
    }

    // Converter a lista para um array de strings
    String[] allStudentsArray = allStudents.toArray(new String[0]);

    return allStudentsArray;
  }
}
