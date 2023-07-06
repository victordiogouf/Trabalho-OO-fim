package trabalho.screens.student;

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

import trabalho.Dialog;
import trabalho.Router;
import trabalho.components.Title;
import trabalho.entities.Turma;
import trabalho.entities.users.Student;
import trabalho.screens.Login;
import trabalho.screens.admin.CreateTeacher;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeStudent extends JFrame {

    // User Infos - Home
    private Student user;

    public HomeStudent(Student user) {

        this.user = user;

        // Configure JFrame
        setSize(1280, 720);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Components
        JPanel cPanel = new JPanel();
        cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.Y_AXIS));
        cPanel.setBounds(0, 0, getWidth(), getHeight());
        cPanel.setVisible(true);
        cPanel.add(Box.createVerticalGlue());

        Title cTitle = new Title("Home");
        cPanel.add(cTitle);

        cPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        JLabel wLabel = new JLabel("Bem vindo " + user.getName());
        wLabel.setAlignmentX(CENTER_ALIGNMENT);
        cPanel.add(wLabel);

        cPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Cria os elementos da lista com título e descrição
        Turma turma = this.user.getTurma();
        if (turma == null) {
            Dialog.showError("Você não está em nenhuma turma");
            Router.getInstance().goTo(new Login());
        }

        Dialog.show(Integer.toString(turma.getDisciplinas().size()));

        // Cria a lista com o modelo padrão
        JList<Turma.Disciplina> lista = new JList<>(turma.getDisciplinas().toArray(new Turma.Disciplina[0]));
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
                if (!e.getValueIsAdjusting()) return;
                Turma.Disciplina disc = lista.getSelectedValue();
                if (disc == null) return;
                try {
                    Dialog.show(disc.toString());
                    toGrades(disc);
                    lista.clearSelection();
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });

        // Cria o painel de rolagem e adiciona a lista a ele
        JScrollPane scrollPane = new JScrollPane(lista);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        // Adiciona o painel de rolagem ao frame
        cPanel.add(scrollPane);

        // Adiciona um botão centralizado
        JButton backButton = new JButton("Logout");
        backButton.addActionListener((e) -> {

            Router.getInstance().goTo(new Login());
        });
        backButton.setAlignmentX(CENTER_ALIGNMENT);
        cPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cPanel.add(backButton);

        add(cPanel);
        setVisible(true);

        cPanel.add(Box.createVerticalGlue());
        cPanel.setVisible(true);
        add(cPanel);

    }

    private void toGrades(Turma.Disciplina d) throws Exception {
        Router.getInstance().goTo(new GradesStudent(this.user, d, this));
    }

    private static class ListItemRenderer implements ListCellRenderer<Turma.Disciplina> {
        private static final Color BACKGROUND_COLOR = new Color(240, 240, 240);
        private static final Color BORDER_COLOR = new Color(200, 200, 200);

        @Override
        public Component getListCellRendererComponent(JList<? extends Turma.Disciplina> list, Turma.Disciplina value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = new JLabel(value.converte());
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
            g.setColor(HomeStudent.ListItemRenderer.BORDER_COLOR);
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

    public static void main(String[] args) {
        HomeStudent teste = new HomeStudent(new Student("mamamam", "Kleber", "sasasa", "sasasa", "aasasas"));
        teste.setVisible(true);

    }
}