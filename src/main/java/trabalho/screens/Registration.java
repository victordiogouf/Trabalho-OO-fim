package trabalho.screens;

import trabalho.Database;
import trabalho.Dialog;
import trabalho.Router;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import trabalho.components.PasswordField;
import trabalho.components.Title;
import trabalho.entities.AuthToken;
import trabalho.entities.users.Student;
import trabalho.entities.users.Teacher;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

public class Registration extends JFrame {
  private PasswordField cPassword;
  private PasswordField cPasswordConfirm;
  private PasswordField cToken;

  public Registration() {
    super("Registrar");
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

    Title cTitle = new Title("Registrar");
    cPanel.add(cTitle);

    cPanel.add(Box.createRigidArea(new Dimension(0, 30)));

    this.cToken = new PasswordField("Token");
    cPanel.add(this.cToken);

    cPanel.add(Box.createRigidArea(new Dimension(0, 16)));

    this.cPassword = new PasswordField("Senha");
    cPanel.add(this.cPassword);

    cPanel.add(Box.createRigidArea(new Dimension(0, 16)));

    this.cPasswordConfirm = new PasswordField("Confirme a senha");
    cPanel.add(this.cPasswordConfirm);

    cPanel.add(Box.createRigidArea(new Dimension(0, 16)));

    JButton cRegister = new JButton("Registrar");
    cRegister.setAlignmentX(JButton.CENTER_ALIGNMENT);
    cRegister.addActionListener(this::onRegister);
    cPanel.add(cRegister);

    cPanel.add(Box.createRigidArea(new Dimension(0, 16)));

    JButton goHome = new JButton("Voltar");
    goHome.setAlignmentX(JButton.CENTER_ALIGNMENT);
    goHome.addActionListener(this::goBack);
    cPanel.add(goHome);

    cPanel.add(Box.createVerticalGlue());
    add(cPanel);
  }

  private void goBack(ActionEvent event) {
    Router.getInstance().goTo(new Login());
  }

  private void onRegister(ActionEvent event) {
    String token = new String(this.cToken.getPassword());
    if (token.length() == 0) {
      Dialog.showError("O campo de token está vazio");
      return;
    }

    String password = new String(this.cPassword.getPassword());
    if (password.length() == 0) {
      Dialog.showError("O campo de senha está vazio");
      return;
    }
    if (password.length() < 8) {
      Dialog.showError("A senha deve ter pelo menos 8 caracteres");
      return;
    }

    String password_confirm = new String(this.cPasswordConfirm.getPassword());
    if (!password_confirm.equals(password)) {
      Dialog.showError("As senhas não são iguais");
      return;
    }

    try {
      AuthToken dbToken = Database.read(AuthToken.class, token);
      if (dbToken == null) {
        Dialog.showError("Token inválido");
        return;
      }
      if (dbToken.userType() == AuthToken.UserType.teacher) {
        Database.create(new Teacher(dbToken.email(), dbToken.name(), password, dbToken.phoneNumber(), dbToken.cpf()));
      }
      else if (dbToken.userType() == AuthToken.UserType.student) {
        Database.create(new Student(dbToken.email(), dbToken.name(), password, dbToken.phoneNumber(), dbToken.cpf()));
      }
      Database.delete(dbToken);

      Dialog.show("Registrado!");
      Router.getInstance().goTo(new Login());
    }
    catch (Exception e) {
      System.err.println(e.getMessage());
      Dialog.showError("Não foi possível concluir o registro");
    }
  }
}
