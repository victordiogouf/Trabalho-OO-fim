package trabalho;

import javax.swing.JOptionPane;

public class Dialog 
{
  public static void show(String text) {
    JOptionPane.showMessageDialog(null, text, "Informação", JOptionPane.INFORMATION_MESSAGE);
  }

  public static void showError(String text) {
    JOptionPane.showMessageDialog(null, text, "Erro", JOptionPane.ERROR_MESSAGE);
  }

  public static void showError(Exception e){
    showError(e.getMessage());
  }
}
