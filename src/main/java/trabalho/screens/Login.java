package trabalho.screens;

import trabalho.Database;
import trabalho.Dialog;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import org.hibernate.HibernateException;

import trabalho.Router;
import trabalho.components.PasswordField;
import trabalho.components.TextField;
import trabalho.components.Title;
import trabalho.entities.users.Admin;
import trabalho.entities.users.Student;
import trabalho.entities.users.Teacher;
import trabalho.entities.users.User;
import trabalho.exceptions.EmptyFieldException;
import trabalho.screens.admin.HomeAdmin;
import trabalho.screens.student.HomeStudent;
import trabalho.screens.teacher.HomeTeacher;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

public class Login extends JFrame {
  private TextField cEmail;
  private PasswordField cPassword;
  private JToggleButton cShowPasswordButton;
  private static final ImageIcon showIcon = new ImageIcon("src\\main\\java\\trabalho\\images\\eye_open.png");
  private static final ImageIcon hideIcon = new ImageIcon("src\\main\\java\\trabalho\\images\\eye_closed.png");

  public Login() {
    super("Login");
    // Configure JFrame
    setSize(1280, 720);
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(null);

    // Botão para mostrar a senha
    this.cShowPasswordButton = new JToggleButton(hideIcon);
    this.cShowPasswordButton.setRolloverIcon(showIcon);
    this.cShowPasswordButton.setToolTipText("Exibir/ocultar senha");
    this.cShowPasswordButton.setPreferredSize(new Dimension(0, 30));
    this.cShowPasswordButton.addActionListener(this::onShowPassword);

    // Components
    JPanel cPanel = new JPanel();
    cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.Y_AXIS));
    cPanel.setBounds(0, 0, getWidth(), getHeight());
    cPanel.setVisible(true);
    cPanel.add(Box.createVerticalGlue());

    Title cTitle = new Title("Login");
    cPanel.add(cTitle);

    cPanel.add(Box.createRigidArea(new Dimension(0, 30)));

    this.cEmail = new TextField("Email");
    cPanel.add(this.cEmail);

    cPanel.add(Box.createRigidArea(new Dimension(0, 16)));

    this.cPassword = new PasswordField("Senha");
    /* cPanel.add(this.cPassword); */

    JPanel passwordPanel = new JPanel();
    passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));
    passwordPanel.setBorder(new EmptyBorder(0, 64, 0, 0));
    passwordPanel.add(this.cPassword);
    passwordPanel.add(this.cShowPasswordButton);
    cPanel.add(passwordPanel);

    cPanel.add(Box.createRigidArea(new Dimension(0, 16)));

    JButton cEnter = new JButton("Entrar");
    cEnter.setAlignmentX(JButton.CENTER_ALIGNMENT);
    cEnter.addActionListener(this::onEnter);
    cPanel.add(cEnter);

    cPanel.add(Box.createRigidArea(new Dimension(0, 8)));

    JButton cRegister = new JButton("Registrar");
    cRegister.setAlignmentX(JButton.CENTER_ALIGNMENT);
    cRegister.addActionListener(this::onRegister);
    cPanel.add(cRegister);

    cPanel.add(Box.createVerticalGlue());
    add(cPanel);
  }

  private void onRegister(ActionEvent event) {
    Router.getInstance().goTo(new Registration());
  }

  private void onEnter(ActionEvent event) {
    // Vê se o e-mail está vazio
    String email = this.cEmail.getText();
    try {
      if (email.length() == 0)
        throw new EmptyFieldException("O campo de email está vazio");
    } 
    catch (EmptyFieldException e) {
      Dialog.showError(e.getMessage());
    }
    // Vê se a senha está vazia
    String password = new String(this.cPassword.getPassword());
    try {
      if (password.length() == 0)
        throw new EmptyFieldException("O campo de senha está vazio");
    } 
    catch (EmptyFieldException e) {
      Dialog.showError(e.getMessage());
    }
    // Tenta logar 
    try {
      User user = Database.read(User.class, email);
      if (user == null) {
        Dialog.showError("Não foi possível entrar. Altere o email ou a senha");
        return;
      }
      if (!user.getPassword().equals(password)) {
        Dialog.showError("Não foi possível entrar. Altere o email ou a senha");
        return;
      }
      if (user instanceof Admin) {
        Router.getInstance().goTo(new HomeAdmin((Admin) user));
      } else if (user instanceof Student) {
        Router.getInstance().goTo(new HomeStudent((Student) user));
      } else if (user instanceof Teacher) {
        Router.getInstance().goTo(new HomeTeacher((Teacher) user));
      } else {
        Dialog.showError("Não foi possível entrar");
      }
    } catch (HibernateException e) {
      Dialog.showError("Erro do banco de dados");
    }
  }

  private void onShowPassword(ActionEvent event) {
    boolean showPassword = cShowPasswordButton.isSelected();
    cPassword.setEchoChar(showPassword ? '\u0000' : '•');
    cShowPasswordButton.setIcon(showPassword ? showIcon : hideIcon);
  }
}
