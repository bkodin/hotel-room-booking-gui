package GUI;

import javax.swing.*;
import java.awt.*;

// A JPanel that paints a top-to-bottom color gradient as its background
// instead of a flat color, using Java's built-in 2D drawing tools.
public class GradientPanel extends JPanel
{
	private Color topColor;
	private Color bottomColor;

	public GradientPanel(Color topColor, Color bottomColor)
	{
		this.topColor = topColor;
		this.bottomColor = bottomColor;
		this.setLayout(null);
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		GradientPaint gp = new GradientPaint(0, 0, topColor, 0, getHeight(), bottomColor);
		g2.setPaint(gp);
		g2.fillRect(0, 0, getWidth(), getHeight());
	}
}
