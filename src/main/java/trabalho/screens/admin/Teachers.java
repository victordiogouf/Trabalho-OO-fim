package trabalho.screens.admin;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
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

import trabalho.Database;
import trabalho.Dialog;
import trabalho.Router;
import trabalho.components.Title;
import trabalho.entities.Turma;
import trabalho.entities.Turma.Disciplina;
import trabalho.entities.users.Teacher;

public class Teachers extends JFrame {
    private DefaultListModel<Teacher> teachersModel;
    private JFrame home;

    public Teachers(JFrame home) {
        super("Professores");
        this.home = home;
        setSize(1280, 720);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(new Title("Professores"), BorderLayout.NORTH);
        showTeachers();

        showButtons();
    }

    private void showButtons() {
        JButton create = new JButton("Criar professor");
        create.setPreferredSize(new Dimension(200, 30));
        create.addActionListener((e) -> {
            Router.getInstance().goTo(new CreateTeacher(this.home));
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

    private void showTeachers() {
        try {
            List<Teacher> teachers = Database.readAll(Teacher.class);

            teachersModel = new DefaultListModel<>();
            for (Teacher t : teachers) {
                teachersModel.addElement(t);
            }

            JList<Teacher> teachersList = new JList<>(teachersModel);
            teachersList.setCellRenderer(new TeacherListRenderer());
            teachersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            teachersList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        Teacher teacher = teachersList.getSelectedValue();
                        teachersList.clearSelection();
                        if (teacher != null) {
                            showTeacherDialog(teacher);
                        }
                    }
                }
            });
            add(new JScrollPane(teachersList), BorderLayout.CENTER);
        } catch (Exception e) {
            Dialog.showError("Não é possível mostrar os estudantes");
        }
    }

    void showTeacherDialog(Teacher teacher) {
        StringBuilder sb = new StringBuilder();
        sb.append("Nome: ").append(teacher.getName()).append("\n");
        sb.append("CPF: ").append(teacher.getCPF()).append("\n");
        sb.append("Email: ").append(teacher.getEmail()).append("\n");
        sb.append("Número de telefone: ").append(teacher.getPhoneNumber()).append("\n");

        Object[] options = { "Excluir", "Atrelar", "Cancelar" };
        int choice = JOptionPane.showOptionDialog(this, sb.toString(), "Detalhes do Professor",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);

        if (choice == 0) {
            try {
                this.teachersModel.removeElement(teacher);
                Database.delete(teacher);
            } catch (Exception e) {
                Dialog.showError("Não é possível remover o professor");
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
                // Reseta o contador usado anteriormente
                cont = 0;
                String[] disciplinas = new String[Turma.Disciplina.values().length];
                Map<String, Disciplina> disciplinasTurma = new HashMap<>();
                for (Disciplina disciplina : Turma.Disciplina.values()) {
                    if (turmaSelecionada.getAno() >= 6 && disciplina != Turma.Disciplina.matematica
                            && disciplina != Turma.Disciplina.ciencias) {
                        disciplinas[cont] = disciplina.toString();
                        disciplinasTurma.put(disciplinas[cont], disciplina);
                        cont++;
                    } else if (turmaSelecionada.getAno() < 6 && disciplina != Turma.Disciplina.algebra
                            && disciplina != Turma.Disciplina.geografia && disciplina != Turma.Disciplina.fisica
                            && disciplina != Turma.Disciplina.quimica && disciplina != Turma.Disciplina.biologia
                            && disciplina != Turma.Disciplina.filosofia && disciplina != Turma.Disciplina.sociologia
                            && disciplina != Turma.Disciplina.literatura) {
                        disciplinas[cont] = disciplina.toString();
                        disciplinasTurma.put(disciplinas[cont], disciplina);
                        cont++;
                    }
                }

                JComboBox<String> comboBoxDisciplinas = new JComboBox<>(disciplinas);
                comboBoxDisciplinas.setEditable(false);

                Object[] optionsDisciplinas = { "Selecionar", "Cancelar" };

                int choiceDisciplina = JOptionPane.showOptionDialog(
                        this,
                        comboBoxDisciplinas,
                        "Qual Disciplina na " + (String) comboBox.getSelectedItem() + "?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        optionsDisciplinas,
                        optionsDisciplinas[0]);

                if (choiceDisciplina == JOptionPane.YES_OPTION) {
                    // Colocar a turma e a disciplina para o professor selecionado anteriormente
                    turmaSelecionada.getProfessoresMap().put(disciplinasTurma.get((String) comboBoxDisciplinas.getSelectedItem()), teacher);
                    try {
                        Database.update(turmaSelecionada);
                        Dialog.show("Professor atrelado com sucesso !");
                    } catch (Exception e) {
                        Dialog.showError("Impossivel atrelar o professor selecionado à turma");
                    }
                }
            }

        }
    }

    private class TeacherListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Teacher teacher = (Teacher) value;
            label.setText(teacher.getName() + "-" + teacher.getCPF());
            return label;
        }
    }
}
