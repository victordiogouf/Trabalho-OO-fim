package trabalho.screens;

import trabalho.screens.admin.CreateStudent;
import trabalho.screens.admin.HomeAdmin;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import trabalho.Dialog;
import trabalho.components.TextField;
import trabalho.components.Title;
import trabalho.entities.users.Admin;
import trabalho.exceptions.CpfIvalidException;
import trabalho.exceptions.EmailInvalidException;
import trabalho.exceptions.EmptyFieldException;
import trabalho.exceptions.PhoneNumberException;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Create extends JFrame {
    protected TextField cName;
    protected TextField cEmail;
    protected TextField cNPhone;
    protected TextField cCpf;

    public Create(String userType) {
        // Configure JFrame
        setTitle("Criar " + userType);
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

        Title cTitle = new Title("Criar " + userType);
        cPanel.add(cTitle);

        cPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        this.cName = new TextField("Nome");
        cPanel.add(this.cName);

        cPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        this.cNPhone = new TextField("Telefone: 32999999999");
        cPanel.add(this.cNPhone);
        cPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        this.cCpf = new TextField("CPF");
        cPanel.add(this.cCpf);
        cPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        this.cEmail = new TextField("E-mail");
        cPanel.add(this.cEmail);
        cPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        JButton cRegister = new JButton("Criar");
        cRegister.setAlignmentX(JButton.CENTER_ALIGNMENT);
        cRegister.addActionListener(this::onCreate);
        cPanel.add(cRegister);

        cPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        JButton back = new JButton("Voltar");
        back.setAlignmentX(JButton.CENTER_ALIGNMENT);
        back.addActionListener(this::goBack);
        cPanel.add(back);

        cPanel.add(Box.createVerticalGlue());
        add(cPanel);
        setVisible(true);
    }

    protected abstract void goBack(ActionEvent event);

    // Cadastra usuário no BD
    protected abstract void onCreate(ActionEvent event);

    protected void isValidEmail(String email) throws EmailInvalidException {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches())
            throw new EmailInvalidException("Email inválido");
    }

    protected void isEmpty() throws EmptyFieldException {
        if (this.cName.getText().length() == 0)
            throw new EmptyFieldException("O campo nome está vazio");

        if (this.cEmail.getText().length() == 0)
            throw new EmptyFieldException("O campo email está vazio");

        if (this.cNPhone.getText().length() == 0)
            throw new EmptyFieldException("O campo número de telefone está vazio");

        if (this.cCpf.getText().length() == 0)
            throw new EmptyFieldException("O campo cpf está vazio");
    }

    protected void validateCPF(String cpf) throws CpfIvalidException {
        // Remove any non-digit characters from the CPF
        cpf = cpf.replaceAll("\\D", "");

        // CPF must be 11 digits long
        if (cpf.length() != 11)
            throw new CpfIvalidException("Cpf inválido: Não respeita padrão de 11 caracteres");

        // Check for repetitive digits (e.g., 11111111111)
        boolean repetitiveDigits = cpf.matches("(\\d)\\1{10}");
        if (repetitiveDigits)
            throw new CpfIvalidException("Cpf inválido: possui apenas dígitos repetidos");

        // Calculate and verify the first verification digit
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int digit1 = 11 - (sum % 11);
        if (digit1 > 9) {
            digit1 = 0;
        }
        if (Character.getNumericValue(cpf.charAt(9)) != digit1)
            throw new CpfIvalidException("Cpf inválido");
        // Calculate and verify the second verification digit
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        int digit2 = 11 - (sum % 11);
        if (digit2 > 9) {
            digit2 = 0;
        }
        if (Character.getNumericValue(cpf.charAt(10)) != digit2)
            throw new CpfIvalidException("Cpf inválido");
    }

    protected void isValidPhone(String number) throws PhoneNumberException {

        if (number.length() != 11)
            throw new PhoneNumberException("Número de telefone inválido: Não respeita padrão de 11 caracteres");
        // Checa se há só números digitados
        for (int i = 0; i < number.length(); i++) {
            char c = number.charAt(i);
            if (!(c >= '0' && c <= '9'))
                throw new PhoneNumberException("Número de telefone inválido");
        }
    }

    public static void main(String[] args) {
        new CreateStudent(new HomeAdmin(new Admin("admin", "admin")));
    }
}