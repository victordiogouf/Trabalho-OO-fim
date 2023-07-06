package trabalho.components;

import java.awt.Font;

import javax.swing.JLabel;

public class Title extends JLabel {
  private final Font titleFont = new Font("Arial", Font.BOLD, 32);

  public Title(String text) {
    super(text);
    setAlignmentX(JLabel.CENTER_ALIGNMENT);
    setFont(titleFont);
  }
}