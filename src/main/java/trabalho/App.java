package trabalho;

import java.util.List;

import trabalho.entities.users.Admin;
import trabalho.entities.users.Student;
import trabalho.entities.users.Teacher;
import trabalho.screens.Login;

public class App {
  public static void main(String[] args) {
    // Database.create(new Teacher("teacher1", "Adalberto Soares Souza", "@teste123","32987425433", "122.565.400-90"));
    // Database.create(new Teacher("teacher2", "Mãe Lucinda Da Silva", "@teste123","32987425433", "445.274.870-86"));
    // Database.create(new Teacher("teacher3", "Roberto Passos de Almeida Souza", "@teste123","32987425433", "839.919.540-52"));
    // Database.create(new Teacher("teacher4", "Marlucia Da Montana Silveira de Almeida", "@teste123","32987425433", "693.621.130-58"));

    // Database.create(new Admin("admin", "admin"));

    // Database.create(new Student("student1", "Marcelo Reginaldo Soares", "@teste123", "32986457798", "409.694.830-66"));
    // Database.create(new Student("student2", "Scharlene da Santa Barbara", "@teste123", "32986457798", "849.367.720-54"));
    // Database.create(new Student("student3", "Victor Marcôncio de Menezes", "@teste123", "32986457798", "032.238.240-81"));
    // Database.create(new Student("student4", "Arauja Rodriga de Roberta Pinheiro", "@teste123", "32986457798", "256.504.870-06"));


    /*
     * List<Admin> admins = Database.readAll(Admin.class);
     * 
     * for (Admin admin : admins) {
     * System.out.println(admin.getEmail());
     * System.out.println(admin.getPassword());
     * 
     * }
     */

    Router.getInstance().goTo(new Login());
  }
}
