package trabalho.screens.teacher;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import trabalho.Database;
import trabalho.Router;
import trabalho.screens.Login;
import trabalho.components.Title;
import trabalho.entities.Turma;
import trabalho.entities.Turma.Disciplina;
import trabalho.entities.users.Student;
import trabalho.entities.users.Teacher;

public class HomeTeacher extends JFrame {
    // User Infos - Home
    private Teacher user;
    private Map<String, Turma> turmas;
    private Map<String, Turma.Disciplina> disciplinasProfessor;
    private JButton backButton;

    public HomeTeacher(Teacher user) {
        this.user = user;
        this.turmas = new HashMap<>();
        this.disciplinasProfessor = new HashMap<>();
        // Configure JFrame
        setSize(1280, 720);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // setLayout(null);

        // Components
        JPanel cPanel = new JPanel();
        cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.Y_AXIS));
        cPanel.setBounds(0, 0, getWidth(), getHeight());
        cPanel.setVisible(true);
        cPanel.setBackground(Color.WHITE);
        cPanel.add(Box.createVerticalGlue());
        Title cTitle = new Title("Home");
        cPanel.add(cTitle);

        cPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        JLabel wLabel = new JLabel("Bem vindo " + user.getName());
        wLabel.setAlignmentX(CENTER_ALIGNMENT);
        cPanel.add(wLabel);

        cPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Cria os elementos da lista com título e descrição
        List<Turma> turmasBD = Database.readAll(Turma.class);
        // String[] turmas = new String[turmasBD.size()];
        List<String> turmasAdicionar = new ArrayList<>();
        for (Turma d : turmasBD) {
            for (Turma.Disciplina disciplina : Turma.Disciplina.values()) {
                Teacher prof = d.getProfessoresMap().get(disciplina);
                if (prof != null) {
                    if (prof.getCPF().equals(this.user.getCPF())) {
                        this.turmas.put("Turma " + Long.toString(d.getId()) + ": " + disciplina.converte(), d);
                        this.disciplinasProfessor.put(
                                "Turma " + Long.toString(d.getId()) + ": " + disciplina.converte(), disciplina);
                        turmasAdicionar.add("Turma " + Long.toString(d.getId()) + ": " + disciplina.converte());
                    }
                }
            }
        }

        // Cria a lista com o modelo padrão
        String[] turmasFormulario = new String[turmasAdicionar.size()];
        int i = 0;
        for (String string : turmasAdicionar) {
            turmasFormulario[i] = string;
            ++i;
        }
        JList<String> lista = new JList<>(turmasFormulario);
        lista.setFont(new Font("Arial", Font.PLAIN, 20));
        lista.setSelectionBackground(new Color(135, 206, 250)); // Define a cor de fundo quando selecionado
        lista.setSelectionForeground(Color.WHITE); // Define a cor do texto quando selecionado
        lista.setFixedCellWidth(400);
        lista.setFixedCellHeight(50);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setCellRenderer(new ListItemRenderer());
        lista.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    toClass(getTurma(lista.getSelectedValue().toString()), lista.getSelectedValue().toString(),
                            getDisciplina(lista.getSelectedValue().toString()));
                    lista.clearSelection();
                }

            }
        });

        // Cria o painel de rolagem e adiciona a lista a ele
        JScrollPane scrollPane = new JScrollPane(lista);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        // Adiciona o painel de rolagem ao frame
        cPanel.add(scrollPane);
        add(cPanel);
        setVisible(true);

        backButton = new JButton("Sair");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(this::logout);
        cPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        cPanel.add(backButton);
        cPanel.add(Box.createRigidArea(new Dimension(0, 16)));
    }

    private void logout(ActionEvent event) {
        this.setVisible(false);
        Router.getInstance().goTo(new Login());
    }

    private void toClass(Turma turma, String disciplina, Turma.Disciplina disiplinaCursada) {
        Router.getInstance().goTo(new ClassTeacher(this.user, turma, disciplina, disiplinaCursada, this));
    }

    private Turma getTurma(String turma) {
        return this.turmas.get(turma);
    }

    private Turma.Disciplina getDisciplina(String turmaProf) {
        return this.disciplinasProfessor.get(turmaProf);
    }

    private static class ListItemRenderer implements ListCellRenderer<String> {
        private static final Color BACKGROUND_COLOR = new Color(240, 240, 240);
        private static final Color BORDER_COLOR = new Color(200, 200, 200);

        @Override
        public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
                boolean isSelected, boolean cellHasFocus) {
            JLabel label = new JLabel(value);
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
            g.setColor(HomeTeacher.ListItemRenderer.BORDER_COLOR);
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

    protected Map<String, Turma> getTurmas() {
        return this.turmas;
    }

    public static void main(String[] args) {
        HomeTeacher teste = new HomeTeacher(new Teacher("aaaaaa", "Kleber", "12345678", "9999999999", "09407850642"));
    }
}