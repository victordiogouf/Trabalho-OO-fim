package trabalho.screens.student;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import trabalho.Router;
import trabalho.entities.Turma;
import trabalho.entities.users.Student;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

public class GradesStudent extends JFrame {

    // User Infos - Home
    private Student user;
    private JButton bSalvar;
    private HomeStudent abaAnterior;
    private DefaultTableModel model;
    private JTable table;

    public GradesStudent(Student user, Turma.Disciplina disciplina, HomeStudent abaAnterior) throws Exception {
        super("Notas");

        this.user = user;
        this.abaAnterior = abaAnterior;
        // Cria uma nova janela

        // Cria um painel para os labels
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));

        // Espaçador para o nome
        JLabel spacer_name = new JLabel();
        spacer_name.setPreferredSize(new Dimension(0, 0));

        // Printa o nome do aluno
        JLabel wLabel = new JLabel("Aluno: " + user.getName());
        labelPanel.add(spacer_name);
        labelPanel.add(wLabel);

        // Importa o cara responsável por alinhar
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        // centerRenderer.setPreferredSize(new Dimension(400, 64));

        // Espaçador para a matrícula
        JLabel spacer_mat = new JLabel();
        spacer_mat.setPreferredSize(new Dimension(0, 100));

        // Printa Turma e Disciplina
        JLabel wLabel3 = new JLabel("MATEMÁTICA-TURMA - A");
        labelPanel.add(spacer_mat);
        labelPanel.add(wLabel3);

        // Define o modelo da tabela
        String[] colunas = { "Bimestres", "AV1", "AV2", "AV3", "AV4" };

        this.model = new DefaultTableModel(colunas, 0) {
            // ...
            @Override
            public boolean isCellEditable(int row, int column) {
                // Retorna false para a coluna 1 (segunda coluna)
                return false;
            }
        };

        this.table = new JTable(model);

        this.table.getColumnModel().getColumn(0).setHeaderValue(colunas[0]);
        this.table.getColumnModel().getColumn(1).setHeaderValue(colunas[1]);
        this.table.getColumnModel().getColumn(2).setHeaderValue(colunas[2]);
        this.table.getColumnModel().getColumn(3).setHeaderValue(colunas[3]);

        // Seto o tamanho da tabela
        this.table.setPreferredSize(new Dimension(1000, 200));
        // Seto a altura da linha
        this.table.setRowHeight(200 / 4);
        // Define o tamanho preferencial da tabela
        this.table.setPreferredScrollableViewportSize(table.getPreferredSize());
        // Define o tamanho da fonte da tabela
        Font fontCorpo = table.getFont().deriveFont(Font.PLAIN, 14);
        table.setFont(fontCorpo);
        // Define a altura do cabeçalho da tabela
        this.table.getTableHeader().setPreferredSize(new Dimension(0, 50));
        Font fontCabecalho = new Font(getName(), Font.BOLD, 14);
        table.getTableHeader().setFont(fontCabecalho);

        // Adiciona uma barra de rolagem à tabela
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(table.getBackground());

        // Cria um painel para a tabela
        JPanel tablePanel = new JPanel();
        tablePanel.add(scrollPane);

        // Configura o alinhamento da tabela no centro
        tablePanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        tablePanel.setAlignmentY(JPanel.CENTER_ALIGNMENT);

        // Adiciona o painel com os labels e a tabela à janela
        add(labelPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        // Adiciona o painel com os labels e a tabela à janela
        add(labelPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        // Aplica a centralização
        this.table.setDefaultRenderer(Object.class, centerRenderer);

        // Configura o tamanho da janela
        setSize(1280, 720);

        // Adiciona algumas linhas à tabela
        this.model.addRow(new Object[] { "1° Bimetre", 
                Float.toString(this.user.getGrade(disciplina, 0, 0)),
                Float.toString(this.user.getGrade(disciplina, 0, 1)),
                Float.toString(this.user.getGrade(disciplina, 0, 2)),
                Float.toString(this.user.getGrade(disciplina, 0, 3)) });
        this.model.addRow(new Object[] { "2° Bimetre", Float.toString(this.user.getGrade(disciplina, 1, 0)),
                Float.toString(this.user.getGrade(disciplina, 1, 1)),
                Float.toString(this.user.getGrade(disciplina, 1, 2)),
                Float.toString(this.user.getGrade(disciplina, 1, 3)) });
        this.model.addRow(new Object[] { "3° Bimetre", Float.toString(this.user.getGrade(disciplina, 0, 2)),
                Float.toString(this.user.getGrade(disciplina, 2, 1)),
                Float.toString(this.user.getGrade(disciplina, 2, 2)),
                Float.toString(this.user.getGrade(disciplina, 2, 3)) });
        this.model.addRow(new Object[] { "4° Bimetre", Float.toString(this.user.getGrade(disciplina, 3, 0)),
                Float.toString(this.user.getGrade(disciplina, 3, 1)),
                Float.toString(this.user.getGrade(disciplina, 3, 2)),
                Float.toString(this.user.getGrade(disciplina, 3, 3)) });

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
        add(buttonPanel, BorderLayout.SOUTH);

        // Define o comportamento padrão do botão "X" na janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Exibe a janela
        setVisible(true);

    }

    public static void main(String[] args) {
        // GradesStudent alal = new GradesStudent(new Student("mamamam", "Kleber",
        // "sasasa", "sasasa", "aasasas"));
    }

}