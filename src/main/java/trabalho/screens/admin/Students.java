package trabalho.screens.admin;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import trabalho.Database;
import trabalho.Dialog;
import trabalho.Router;
import trabalho.components.Title;
import trabalho.entities.Turma;
import trabalho.entities.users.Student;

public class Students extends JFrame {
    private JFrame home;
    private DefaultListModel<Student> studentsModel;

    public Students(JFrame home) {
        super("Estudantes");
        this.home = home;
        setSize(1280, 720);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(new Title("Alunos"), BorderLayout.NORTH);

        showStudents();

        showButtons();
    }

    private void showButtons() {
        JButton create = new JButton("Criar aluno");
        create.setPreferredSize(new Dimension(200, 30));
        create.addActionListener((e) -> {
            Router.getInstance().goTo(new CreateStudent(this.home));
        });
        JButton back = new JButton("Voltar");
        back.setPreferredSize(new Dimension(200, 30));
        back.addActionListener((e) -> {
            Router.getInstance().goTo(this.home);
        });
        JPanel panel = new JPanel();
        panel.add(create);
        panel.add(back);
        add(panel, BorderLayout.SOUTH);
    }

    private void showStudents() {
        try {
            List<Student> students = Database.readAll(Student.class);

            studentsModel = new DefaultListModel<>();
            for (Student s : students) {
                studentsModel.addElement(s);
            }
            JList<Student> studentsList = new JList<>(studentsModel);
            studentsList.setCellRenderer(new StudentListRenderer());
            studentsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            studentsList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        Student student = studentsList.getSelectedValue();
                        studentsList.clearSelection();
                        if (student != null) {
                            showStudentDialog(student);
                        }
                    }
                }
            });
            add(new JScrollPane(studentsList), BorderLayout.CENTER);
        } catch (Exception e) {
            Dialog.showError("Não é possível mostrar os estudantes");
        }
    }

    void showStudentDialog(Student student) {
        StringBuilder sb = new StringBuilder();
        sb.append("Nome: ").append(student.getName()).append("\n");
        sb.append("CPF: ").append(student.getCPF()).append("\n");
        sb.append("Email: ").append(student.getEmail()).append("\n");
        sb.append("Número de telefone: ").append(student.getPhoneNumber()).append("\n");

        Object[] options = { "Excluir", "Atrelar", "Cancelar" };
        int choice = JOptionPane.showOptionDialog(this, sb.toString(), "Detalhes do Aluno",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);

        if (choice == 0) {
            try {
                this.studentsModel.removeElement(student);
                Database.delete(student);
            } catch (Exception e) {
                Dialog.showError("Não é possível remover o aluno");
            }
        }
        if (choice == 1) {
            List<Turma> turmasCadastradas = Database.readAll(Turma.class);
            String[] turmas = new String[Turma.Disciplina.values().length];
            int cont = 0;
            Map<String, Turma> turmasSistema = new HashMap<>();
            for (Turma turma : turmasCadastradas) {
                turmas[cont] = "Turma " + Long.toString(turma.getId());
                turmasSistema.put(turmas[cont], turma);
                cont++;
            }
            JComboBox<String> comboBox = new JComboBox<>(turmas);
            comboBox.setEditable(false);
            Object[] optionsTurma = { "Selecionar", "Cancelar" };
            int choiceTurma = JOptionPane.showOptionDialog(
                    this,
                    comboBox,
                    "Atrelar a qual Turma?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    optionsTurma,
                    optionsTurma[0]);

            if (choiceTurma == JOptionPane.YES_OPTION) {
                Turma turmaSelecionada = turmasSistema.get((String) comboBox.getSelectedItem());
                turmaSelecionada.getAlunos().add(student);
                try {
                    student.setTurma(turmaSelecionada);
                    Session session = Database.sessionFactory.openSession();
                    Transaction transaction = null;
                    try {
                        transaction = session.beginTransaction();
                        session.saveOrUpdate(turmaSelecionada);
                        session.saveOrUpdate(student);
                        transaction.commit();
                    } catch (HibernateException e) {
                        if (transaction != null) {
                            transaction.rollback();
                        }
                        throw e;
                    } finally {
                        session.close();
                    }
                    Dialog.show("Aluno atrelado à turma com sucesso!");
                } catch (Exception e) {
                    Dialog.showError("Impossivel atrelar o aluno selecionado à turma");
                }
            }
        }
    }

    private class StudentListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Student student = (Student) value;
            label.setText(student.getName() + "-" + student.getCPF());
            return label;
        }
    }
}