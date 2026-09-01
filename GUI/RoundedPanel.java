package GUI;

import javax.swing.*;
import java.awt.*;

// A JPanel with rounded corners instead of a plain rectangle. It is used as a
// "card" background all over the program: the big white panels, the log entries,
// the status pills and the room tiles are all RoundedPanels.
public class RoundedPanel extends JPanel
{
	private Color bgColor;
	private int arc;

	public RoundedPanel(Color bgColor, int arc)
	{
		this.bgColor = bgColor;
		this.arc = arc;
		this.setLayout(null);
		this.setOpaque(false);
	}

	// Lets the colour be changed after the panel is built, which the room tiles
	// use to light up when the mouse moves over them.
	public void setColor(Color bgColor)
	{
		this.bgColor = bgColor;
		repaint();
	}

	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(bgColor);
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
		super.paintComponent(g);
	}
}
