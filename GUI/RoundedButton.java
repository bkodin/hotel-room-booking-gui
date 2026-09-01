package GUI;

import javax.swing.*;
import java.awt.*;

// A JButton with rounded corners. It remembers two colours -- its normal one and
// a lighter one for when the mouse is over it -- so HotelManagerPage can just say
// "use your hover colour" instead of naming a colour for every single button.
public class RoundedButton extends JButton
{
	private Color normalColor;
	private Color hoverColor;

	public RoundedButton(String text)
	{
		super(text);
		this.setForeground(Color.WHITE);
		this.setFont(new Font("Cambria", Font.BOLD, 14));
		this.setFocusPainted(false);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		this.setOpaque(false);
		this.setMargin(new Insets(0, 0, 0, 0));
		this.setIconTextGap(9);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	public void setColors(Color normalColor, Color hoverColor)
	{
		this.normalColor = normalColor;
		this.hoverColor = hoverColor;
		this.setBackground(normalColor);
	}

	public Color getNormalColor()
	{
		return normalColor;
	}

	public Color getHoverColor()
	{
		return hoverColor;
	}

	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(getBackground());
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
		super.paintComponent(g);
	}
}
