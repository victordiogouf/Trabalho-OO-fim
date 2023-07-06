package trabalho.screens.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import trabalho.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import trabalho.Database;
import trabalho.Router;
import trabalho.entities.Turma;
import trabalho.entities.Turma.Disciplina;
import trabalho.entities.users.Admin;
import trabalho.entities.users.Teacher;
import trabalho.screens.teacher.HomeTeacher;

public class TurmasAdmin extends JFrame {
    private JList<Turma> list;
    private Admin user;

    public TurmasAdmin(Admin user) {
        super("Turmas");

        this.user = user;

        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        List<Turma> turmas = Database.readAll(Turma.class);

        list = new JList<Turma>(turmas.toArray(new Turma[0]));
        list.setFont(new Font("Arial", Font.PLAIN, 20));
        list.setCellRenderer(new ListItemRenderer());
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Turma t = list.getSelectedValue() ;
                    if (t == null) return;
                    Router.getInstance().goTo(new EditTurma(t, user));
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // JComboBox para selecionar o professor
        List<Teacher> teachers = Database.readAll(Teacher.class);
        String[] professorOptions = new String[teachers.size() + 1];
        professorOptions[0] = "Todos os Professores";
        for (int i = 0; i < teachers.size(); i++) {
            Teacher teacher = teachers.get(i);
            String professorOption = teacher.getName() + " - " + teacher.getCPF();
            professorOptions[i + 1] = professorOption;
        }

        JButton buttoncreateClasse = new JButton("Criar Turma");
        buttoncreateClasse.setFont(new Font("Arial", Font.PLAIN, 18));
        buttoncreateClasse.addActionListener(this::createClasse);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JButton goBack = new JButton("Voltar");
        goBack.setFont(new Font("Arial", Font.PLAIN, 18));
        goBack.addActionListener(this::goBack);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(goBack);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(buttoncreateClasse);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        setSize(new Dimension(1280, 720));
        setLocationRelativeTo(null);
    }

    private static class ListItemRenderer implements ListCellRenderer<Turma> {
        private static final Color BACKGROUND_COLOR = new Color(240, 240, 240);
        private static final Color BORDER_COLOR = new Color(200, 200, 200);

        @Override
        public Component getListCellRendererComponent(JList<? extends Turma> list, Turma value, int index,
                boolean isSelected, boolean cellHasFocus) {
                    JLabel label = new JLabel("Turma " + value.getId() + " - " + value.getAno() + "° ano - Numero de alunos: " + value.getAlunos().size());
                    label.setOpaque(true);
            label.setFont(new Font("Arial", Font.PLAIN, 20));
            label.setBorder(new CustomBorder());

            if (isSelected) {
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            } else {
                label.setBackground(BACKGROUND_COLOR);
                label.setForeground(list.getForeground());
            }

            return label;
        }
    }

    private static class CustomBorder implements javax.swing.border.Border {
        private static final int BORDER_THICKNESS = 2;

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(TurmasAdmin.ListItemRenderer.BORDER_COLOR);
            g.fillRect(x, y + height - BORDER_THICKNESS, width, BORDER_THICKNESS);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(0, 0, BORDER_THICKNESS, 0);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }
    }


    protected void createClasse(ActionEvent event) {
        String[] options = { "Criar", "Cancelar" };
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JComboBox<String> yearComboBox = new JComboBox<>(new String[] { "1 ano", "2 ano", "3 ano", "4 ano", "5 ano",
                "6 ano", "7 ano", "8 ano", "9 ano" });

        JLabel lblAno = new JLabel("Selecione o Ano da Turma");
        panel.add(lblAno);
        panel.add(Box.createVerticalStrut(10));
        panel.add(yearComboBox);

        int result = JOptionPane.showOptionDialog(this, panel, "Criar Turma",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (result == 0) { // "Criar" button clicked
            String anoTurma = (String) yearComboBox.getSelectedItem();
            int ano = Integer.parseInt(anoTurma.split(" ")[0]);

            Turma novaTurma = new Turma(ano);
            List<Turma.Disciplina> disciplinas = new ArrayList<>();
            for (Disciplina disciplina : Turma.Disciplina.values()) {
                if (novaTurma.getAno() >= 6) {
                    if (disciplina != Turma.Disciplina.matematica
                        && disciplina != Turma.Disciplina.ciencias)
                    {
                        disciplinas.add(disciplina);
                    }
                } else {
                    if (disciplina != Turma.Disciplina.algebra
                        && disciplina != Turma.Disciplina.geografia && disciplina != Turma.Disciplina.fisica
                        && disciplina != Turma.Disciplina.quimica && disciplina != Turma.Disciplina.biologia
                        && disciplina != Turma.Disciplina.filosofia && disciplina != Turma.Disciplina.sociologia
                        && disciplina != Turma.Disciplina.literatura)
                        disciplinas.add(disciplina);
                }
            }
            
            novaTurma.setDisciplinas(disciplinas);

            Database.create(novaTurma);

            // Exemplo de exibição de mensagem de sucesso
            JOptionPane.showMessageDialog(this, "Turma criada com sucesso!");
            
            // Atualize a lista exibida no JList após a criação da turma
            updateTurmaList();
        }

    }

    protected void goBack(ActionEvent event) {
        Router.getInstance().goTo(new HomeAdmin((Admin) user));
    }
    
    private void updateTurmaList() {
        List<Turma> turmas = Database.readAll(Turma.class);
        Turma[] elementos = new Turma[turmas.size()];

        for (int i = 0; i < turmas.size(); i++) {
            Turma turma = turmas.get(i);
            elementos[i] = turma;
        }

        list.setListData(elementos);
    }

    public static void main(String[] args) {
        // Create an instance of TurmasAdmin and make it visible
        Admin admin = new Admin(); // Replace with your Admin object instantiation
        TurmasAdmin turmasAdmin = new TurmasAdmin(admin);
        turmasAdmin.setVisible(true);
    }
}
