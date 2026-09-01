package GUI;

import javax.swing.*;
import java.awt.*;

// The slim progress bar in the header that fills up as more rooms are taken.
public class OccupancyBar extends JPanel
{
	private int value;
	private int max;
	private Color track;
	private Color fill;

	public OccupancyBar(Color track, Color fill)
	{
		this.track = track;
		this.fill = fill;
		this.value = 0;
		this.max = 1;
		this.setOpaque(false);
	}

	public void setValue(int value, int max)
	{
		this.value = value;
		this.max = max;
		repaint();
	}

	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int h = getHeight();

		g2.setColor(track);
		g2.fillRoundRect(0, 0, getWidth(), h, h, h);

		if (value > 0 && max > 0)
		{
			int w = (int) ((double) getWidth() * value / max);

			if (w < h)
			{
				w = h;
			}

			g2.setColor(fill);
			g2.fillRoundRect(0, 0, w, h, h, h);
		}
	}
}
