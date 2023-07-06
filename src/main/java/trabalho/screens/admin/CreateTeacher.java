package trabalho.screens.admin;

import trabalho.Database;
import trabalho.Dialog;
import trabalho.Email;
import trabalho.Router;

import trabalho.entities.AuthToken;
import trabalho.entities.users.Admin;
import trabalho.entities.users.Teacher;
import trabalho.exceptions.CpfIvalidException;
import trabalho.exceptions.EmailInvalidException;
import trabalho.exceptions.EmptyFieldException;
import trabalho.exceptions.PhoneNumberException;
import trabalho.screens.Create;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JFrame;

public class CreateTeacher extends Create {
    private JFrame home;

    public CreateTeacher(JFrame home) {
        super("Professor");
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
                Teacher teacher = Database.read(Teacher.class, this.cEmail.getText());
                if (teacher != null) {
                    Dialog.showError("Um professor com esse email já foi criado");
                    return;
                }
                List<Teacher> teachers = Database.readAll(Teacher.class);
                for (Teacher t : teachers) {
                    System.out.println(t.getName());
                    if (t.getCPF().equals(this.cCpf.getText())) {
                        Dialog.showError("Um professor com esse email já foi criado");
                        return;
                    }
                }

                AuthToken token = new AuthToken(
                        AuthToken.UserType.teacher,
                        this.cName.getText(),
                        this.cEmail.getText(),
                        this.cNPhone.getText(),
                        this.cCpf.getText());

                Database.create(token);
                Email.send(
                        this.cEmail.getText(),
                        "Token para registro de professor",
                        "Olá " + this.cName.getText()
                                + ", você foi cadastrado como um professor na Escola.\nUtilize este token para completar seu registro: "
                                + token.value());

                Dialog.show("Um email com o token gerado foi enviado para " + this.cEmail.getText());
                Router.getInstance().goTo(new CreateTeacher(this.home));
            } 
            catch (Exception e) {
                System.err.println(e.getMessage());
                Dialog.showError("Erro na criação do usuário");
            }
        } 
        catch (EmptyFieldException e) {
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
        Router.getInstance().goTo(new Teachers(this.home));
    }

    public static void main(String[] args) {
        new CreateTeacher(new HomeAdmin(new Admin("admin", "admin")));
    }
}