package trabalho.screens.teacher;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import trabalho.Database;

import trabalho.Dialog;
import trabalho.Router;
import trabalho.entities.Turma;
import trabalho.entities.users.Student;
import trabalho.entities.users.Teacher;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

public class GradesTeacher extends JFrame {

    // User Infos - Home
    private Teacher user;
    private Turma.Disciplina disciplinaCursada;
    private Student estudante;
    private JButton bSalvar;
    private DefaultTableModel model;
    private JTable table;
    private ClassTeacher abaAnterior;

    public GradesTeacher(Teacher user, Student estudante, Turma.Disciplina disciplina, ClassTeacher abaAnterior)
            throws Exception {

        super("Notas");
        this.user = user;
        this.abaAnterior = abaAnterior;
        this.disciplinaCursada = disciplina;
        this.estudante = estudante;
        // Cria uma nova janela

        // Cria um painel para os labels
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));

        // Espaçador para o nome
        JLabel spacer_name = new JLabel();
        spacer_name.setPreferredSize(new Dimension(0, 0));

        // Printa o nome do aluno
        JLabel wLabel = new JLabel("Aluno: " + estudante.getName());
        labelPanel.add(spacer_name);
        labelPanel.add(wLabel);

        // Importa o cara responsável por alinhar
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        // centerRenderer.setPreferredSize(new Dimension(400, 64));

        // Espaçador para a matrícula
        JLabel spacer_mat = new JLabel();
        spacer_mat.setPreferredSize(new Dimension(0, 100));

        // Define o modelo da tabela
        String[] colunas = { "Bimestres", "AV1", "AV2", "AV3", "AV4" };

        this.model = new DefaultTableModel(colunas, 0) {
            // ...
            @Override
            public boolean isCellEditable(int row, int column) {
                // Retorna false para a coluna 1 (segunda coluna)
                return column != 0;
            }
        };

        this.table = new JTable(this.model);

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
        this.table.setFont(fontCorpo);
        // Define a altura do cabeçalho da tabela
        this.table.getTableHeader().setPreferredSize(new Dimension(0, 50));
        Font fontCabecalho = new Font(getName(), Font.BOLD, 14);
        this.table.getTableHeader().setFont(fontCabecalho);

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

        // Botão
        this.bSalvar = new JButton(getName(), null);
        this.bSalvar.setText("Salvar alterações");
        // Define o tamanho da fonte do botão
        Font fontButton = new Font(this.bSalvar.getFont().getName(), Font.PLAIN, 16);
        this.bSalvar.setFont(fontButton);
        this.bSalvar.addActionListener(event -> {
            try {
                salvar(event);
            } catch (Exception e) {
                // Chamo o Dialog para relatar o que deu errado
                Dialog.showError(e);
            }
        });

        // Cria um painel para o botão
        JPanel buttonPanel = new JPanel();

        // Espaçador para posicionar o botão mais para cima
        JLabel spacerButton = new JLabel();
        spacerButton.setPreferredSize(new Dimension(0, 250));
        buttonPanel.add(spacerButton);

        // Adiciona o botão ao painel
        buttonPanel.add(this.bSalvar);

        // Adiciona o painel do botão abaixo da tabela
        add(buttonPanel, BorderLayout.SOUTH);

        // Configura o tamanho da janela
        setSize(1280, 720);

        // Adiciona algumas linhas à tabela
        this.model
                .addRow(new Object[] { "1° Bimetre",
                        Float.toString(estudante.getGrade(this.disciplinaCursada, 0, 0)),
                        Float.toString(estudante.getGrade(this.disciplinaCursada, 0, 1)),
                        Float.toString(estudante.getGrade(this.disciplinaCursada, 0, 2)),
                        Float.toString(estudante.getGrade(this.disciplinaCursada, 0, 3)) });
        this.model
                .addRow(new Object[] { "2° Bimetre",
                        Float.toString(estudante.getGrade(this.disciplinaCursada, 1, 0)),
                        Float.toString(estudante.getGrade(this.disciplinaCursada, 1, 1)),
                        Float.toString(estudante.getGrade(this.disciplinaCursada, 1, 2)),
                        Float.toString(estudante.getGrade(this.disciplinaCursada, 1, 3)) });
        this.model
                .addRow(new Object[] { "3° Bimetre",
                        Float.toString(estudante.getGrade(this.disciplinaCursada, 2, 0)),
                        Float.toString(estudante.getGrade(this.disciplinaCursada, 2, 1)),
                        Float.toString(estudante.getGrade(this.disciplinaCursada, 2, 2)),
                        Float.toString(estudante.getGrade(this.disciplinaCursada, 2, 3)) });
        this.model
                .addRow(new Object[] { "4° Bimetre",
                        Float.toString(estudante.getGrade(this.disciplinaCursada, 3, 0)),
                        Float.toString(estudante.getGrade(this.disciplinaCursada, 3, 1)),
                        Float.toString(estudante.getGrade(this.disciplinaCursada, 3, 2)),
                        Float.toString(estudante.getGrade(this.disciplinaCursada, 3, 3)) });

        JButton back = new JButton("Voltar");
        back.setPreferredSize(new Dimension(200, 30));
        back.addActionListener((e) -> {
            Router.getInstance().goTo(this.abaAnterior);
        });
        buttonPanel.add(back);
        // Define o comportamento padrão do botão "X" na janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private void salvar(ActionEvent event) throws Exception {
        boolean tudoCerto = true;
        for (int row = 0; row < this.model.getRowCount(); row++) {
            for (int column = 1; column < this.model.getColumnCount(); column++) {
                float value = Float.parseFloat(this.model.getValueAt(row, column).toString());
                if (value < 0 || value > 10) {
                    throw new Exception("Há uma nota inválida no boletim !");
                }
                try {
                    this.estudante.setGrade(this.disciplinaCursada, row, column - 1, value);
                } catch (Exception ex) {
                    Dialog.showError(
                            "[DEV ERROR] Não foi possivel salvar nota para disciplina "
                                    + this.disciplinaCursada.toString()
                                    + ", na linha " + Integer.toString(row)
                                    + " e na coluna " + Integer.toString(column));
                }
                            }
        }

        try {
            Database.update(this.estudante);
            // Se salvar no bd:
            JOptionPane.showMessageDialog(null, "Salvo com sucesso", "Resultado", JOptionPane.INFORMATION_MESSAGE);
            Router.getInstance().goTo(new GradesTeacher(user, estudante, disciplinaCursada, this.abaAnterior));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            // Se não salvar no bd
            throw new Exception("Não foi possível salvar as mudanças cadastradas");
        }
    }

    public static void main(String[] args) {
        // GradesTeacher alal = new GradesTeacher(new Teacher("mamamam", "Kleber",
        // "sasasa", "sasasa", "aasasas"));
    }

}