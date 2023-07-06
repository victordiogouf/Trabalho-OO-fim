package trabalho.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JPasswordField;

public class PasswordField extends JPasswordField implements FocusListener 
{
  private String placeholder;

  public PasswordField(String placeholder) {
    this.placeholder = placeholder;
    setMaximumSize  (new Dimension(300, 32));
    setPreferredSize(getMaximumSize());
    setAlignmentX(JPasswordField.CENTER_ALIGNMENT);
    addFocusListener(this);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (super.getPassword().length == 0 && !isFocusOwner()) {
      java.awt.FontMetrics metrics = g.getFontMetrics();
      g.setColor(Color.GRAY);
      int y = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
      g.drawString(placeholder, getInsets().left, y);
    }
  }

  public void focusGained(FocusEvent e) {
    repaint();
  }

  public void focusLost(FocusEvent e) {
    repaint();
  }

}
