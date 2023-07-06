package trabalho.screens.teacher;

import trabalho.Router;
import trabalho.components.Title;
import trabalho.entities.Turma;
import trabalho.entities.users.Student;
import trabalho.entities.users.Teacher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;

public class ClassTeacher extends JFrame {

    private Teacher user;
    private Turma.Disciplina disciplinaCursada;
    private Map<String, Student> alunos;
    private HomeTeacher abaAnterior;
    private JList lista;

    public ClassTeacher(Teacher user, Turma turma, String disciplina, Turma.Disciplina disciplinaCursada,
            HomeTeacher abaAnterior) {
        this.user = user;
        this.abaAnterior = abaAnterior;
        this.disciplinaCursada = disciplinaCursada;
        this.alunos = new HashMap<>();
        setSize(1280, 720);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel cPanel = new JPanel();
        cPanel.setLayout(new BorderLayout());
        cPanel.setBackground(Color.WHITE);

        Title cTitle = new Title(disciplina);
        cPanel.add(cTitle, BorderLayout.NORTH);
        List<Student> estudantes = turma.getAlunos();
        String[] elementos = new String[estudantes.size()];
        for (int i = 0; i < elementos.length; i++) {
            elementos[i] = estudantes.get(i).getName();
            this.alunos.put(estudantes.get(i).getName(), estudantes.get(i));
        }

        JList<String> lista = new JList<>(elementos);
        lista.setFont(new Font("Arial", Font.PLAIN, 20));
        lista.setSelectionBackground(new Color(135, 206, 250));
        lista.setSelectionForeground(Color.WHITE);
        lista.setFixedCellWidth(200);
        lista.setFixedCellHeight(50);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setCellRenderer(new ListItemRenderer());
        lista.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting())
                    try {
                        toGrades(getAlunos(lista.getSelectedValue()));
                        lista.clearSelection();
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
            }
        });
        JScrollPane scrollPane = new JScrollPane(lista);
        scrollPane.setPreferredSize(new Dimension(250, 400));

        cPanel.add(scrollPane, BorderLayout.CENTER);
        add(cPanel);
        // Cria um painel para o botão
        JPanel buttonPanel = new JPanel();
        // Espaçador para posicionar o botão mais para cima
        JLabel spacerButton = new JLabel();
        spacerButton.setPreferredSize(new Dimension(0, 250));
        buttonPanel.add(spacerButton);
        // Botão de voltar
        JButton back = new JButton("Voltar");
        back.setPreferredSize(new Dimension(200, 30));
        back.addActionListener((e) -> {
            Router.getInstance().goTo(this.abaAnterior);
        });
        buttonPanel.add(back);
        cPanel.add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void toGrades(Student estudante) throws Exception {
        Router.getInstance().goTo(new GradesTeacher(this.user, estudante, this.disciplinaCursada, this));
    }

    private static class ListItemRenderer extends DefaultListCellRenderer {

        private static final Color BACKGROUND_COLOR = new Color(240, 240, 240);
        private static final Color BORDER_COLOR = new Color(200, 200, 200);

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
            label.setBackground(isSelected ? list.getSelectionBackground() : BACKGROUND_COLOR);
            label.setForeground(list.getForeground());
            return label;
        }
    }

    protected Student getAlunos(String nome) {
        return this.alunos.get(nome);
    }

    public static void main(String[] args) {
        // Teacher prof = new Teacher("mamamam", "Kleber", "sasasa", "sasasa",
        // "aasasas");
        // ClassTeacher teste = new ClassTeacher(prof);
    }
}