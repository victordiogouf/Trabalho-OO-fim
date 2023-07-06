package trabalho.screens.admin;

import trabalho.Database;
import trabalho.Dialog;
import trabalho.Email;
import trabalho.Router;

import trabalho.entities.AuthToken;
import trabalho.entities.users.Admin;
import trabalho.entities.users.Student;
import trabalho.exceptions.CpfIvalidException;
import trabalho.exceptions.EmailInvalidException;
import trabalho.exceptions.EmptyFieldException;
import trabalho.exceptions.PhoneNumberException;
import trabalho.screens.Create;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JFrame;

public class CreateStudent extends Create {
    private JFrame home;

    public CreateStudent(JFrame home) {
        super("Aluno");
        this.home = home;
    }

    @Override
    protected void onCreate(ActionEvent event) {
        try {
            this.isEmpty();
            this.validateCPF(this.cCpf.getText());
            this.isValidPhone(this.cNPhone.getText());
            this.isValidEmail(this.cEmail.getText());
            try {
            Student student = Database.read(Student.class, this.cEmail.getText());
            if (student != null) {
                Dialog.showError("Um aluno com esse email já foi criado");
                return;
            }
            List<Student> students = Database.readAll(Student.class);
            for (Student s : students) {
                System.out.println(s.getName());
                if (s.getCPF().equals(this.cCpf.getText())) {
                    Dialog.showError("Um aluno com esse email já foi criado");
                    return;
                }
            }

            AuthToken token = new AuthToken(
                    AuthToken.UserType.student,
                    this.cName.getText(),
                    this.cEmail.getText(),
                    this.cNPhone.getText(),
                    this.cCpf.getText());

            Database.create(token);
            Email.send(
                    this.cEmail.getText(),
                    "Token para registro de aluno",
                    "Olá " + this.cName.getText()
                            + ", você foi cadastrado como um aluno na Escola.\nUtilize este token para completar seu registro: "
                            + token.value());

            Dialog.show("Um email com o token gerado foi enviado para " + this.cEmail.getText());
            Router.getInstance().goTo(new CreateStudent(this.home));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            Dialog.showError("Erro na criação do usuário");
        }
        } 
        catch(EmptyFieldException e){
            Dialog.showError(e.getMessage());
        }
        catch (CpfIvalidException e) {
            Dialog.showError(e.getMessage());
        } 
        catch (PhoneNumberException e) {
            Dialog.showError(e.getMessage());
        } 
        catch (EmailInvalidException e) {
            Dialog.showError(e.getMessage());
        } 
        catch (Exception e) {
            Dialog.showError("Um erro inesperado aconteceu");
        }
    }

    protected void goBack(ActionEvent event) {
        Router.getInstance().goTo(new Students(this.home));
    }

    public static void main(String[] args) {
        new CreateStudent(new HomeAdmin(new Admin("admin", "admin")));
    }
}