package trabalho.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;

import javax.swing.JTextField;

public class TextField extends JTextField {
  private String placeholder;

  public TextField(String placeholder) {
    this.placeholder = placeholder;
    setMaximumSize(new Dimension(300, 32));
    setPreferredSize(getMaximumSize());
    setAlignmentX(JTextField.CENTER_ALIGNMENT);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (getText().isEmpty()) {
      g.setColor(Color.GRAY);
      g.drawString(placeholder, getInsets().left, g.getFontMetrics().getMaxAscent() + getInsets().top
          + (getHeight() - (getInsets().top + getInsets().bottom + g.getFontMetrics().getHeight())) / 2);
    }
  }
}