package trabalho.screens.admin;

import trabalho.Dialog;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.w3c.dom.Text;

import com.google.api.services.gmail.Gmail.Users.Drafts.Create;

import trabalho.Router;
import trabalho.components.Title;
import trabalho.entities.users.Admin;
import trabalho.entities.users.Teacher;
import trabalho.screens.Login;

import java.awt.Dimension;
import java.awt.TextField;
import java.awt.event.ActionEvent;

public class HomeAdmin extends JFrame {
    // User Infos - Home
    private Admin user;
    private JButton bDiscplines;
    private JButton bClasses;
    private JButton bTeachers;
    private JButton bStudents;
    private JButton bMatricula;
    private JButton bLogout;

    public HomeAdmin(Admin user) {

        // Set the window title
        super("HomeAdmin");

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

        JLabel wLabel = new JLabel("Bem vindo");
        wLabel.setAlignmentX(CENTER_ALIGNMENT);
        cPanel.add(wLabel);

        cPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        this.bDiscplines = new JButton("Gerenciar Turmas");
        this.bDiscplines.addActionListener(this::toClassesAdmin);
        this.bDiscplines.setAlignmentX(JButton.CENTER_ALIGNMENT);
        cPanel.add(this.bDiscplines);

        cPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        this.bTeachers = new JButton("Professores");
        this.bTeachers.addActionListener(this::toCreateTeacher);
        this.bTeachers.setAlignmentX(CENTER_ALIGNMENT);
        cPanel.add(this.bTeachers);

        cPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        this.bStudents = new JButton("Alunos");
        this.bStudents.addActionListener(this::toStudents);
        this.bStudents.setAlignmentX(CENTER_ALIGNMENT);
        cPanel.add(this.bStudents);

        cPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        this.bLogout = new JButton("Logout");
        this.bLogout.addActionListener(this::logout);
        this.bLogout.setAlignmentX(JButton.CENTER_ALIGNMENT);
        cPanel.add(this.bLogout);

        cPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        cPanel.add(Box.createVerticalGlue());
        add(cPanel);

    }

    private void logout(ActionEvent event) {
        Router.getInstance().goTo(new Login());
    }

    private void toClassesAdmin(ActionEvent event) {
        Router.getInstance().goTo(new TurmasAdmin(this.user));
    }

    private void toCreateTeacher(ActionEvent event) {
        Router.getInstance().goTo(new Teachers(this));
    }

    private void toStudents(ActionEvent event) {
        Router.getInstance().goTo(new Students(this));
    }

}
