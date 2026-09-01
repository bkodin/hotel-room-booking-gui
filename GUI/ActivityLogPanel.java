package GUI;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

// The Activity Log. Every action the manager performs is added here as a small
// coloured card holding a picture, a headline, a detail line and the time.
//
// The cards are placed by hand, one under the other, inside a scrolling panel:
// entry number 0 goes at y = 0, entry number 1 at y = 78, and so on.
public class ActivityLogPanel extends JPanel
{
	private static final int ROW = 78;   // height of one entry plus the gap under it

	private JPanel list;
	private JScrollPane scroll;
	private JLabel emptylbl;
	private int count;
	private int width;
	private int height;

	public ActivityLogPanel(int width, int height)
	{
		this.count = 0;

		this.setLayout(null);
		this.setBackground(Color.WHITE);

		list = new JPanel();
		list.setLayout(null);
		list.setBackground(Color.WHITE);

		scroll = new JScrollPane(list);
		scroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.getViewport().setBackground(Color.WHITE);
		scroll.getVerticalScrollBar().setUnitIncrement(20);

		// Shown until the first action happens. It sits on top of the scroll pane
		// and is simply hidden once there is something to show.
		emptylbl = new JLabel("No activity yet", Icons.get("list_pale"), SwingConstants.CENTER);
		emptylbl.setHorizontalTextPosition(SwingConstants.CENTER);
		emptylbl.setVerticalTextPosition(SwingConstants.BOTTOM);
		emptylbl.setIconTextGap(18);
		emptylbl.setFont(new Font("Cambria", Font.BOLD, 17));
		emptylbl.setForeground(new Color(146, 157, 150));
		emptylbl.setOpaque(true);
		emptylbl.setBackground(Color.WHITE);

		this.add(emptylbl);
		this.add(scroll);

		resizeTo(width, height);
	}

	// Called by the main window whenever it is resized or maximised.
	public void resizeTo(int w, int h)
	{
		width = w;
		height = h;

		scroll.setBounds(0, 0, w, h);
		emptylbl.setBounds(0, h / 2 - 70, w, 140);
		layoutEntries();
	}

	// Every entry is built the same way, so its four parts can be moved back into
	// place by walking the list: 0 = picture, 1 = headline, 2 = detail, 3 = time.
	private void layoutEntries()
	{
		for (int i = 0; i < list.getComponentCount(); i++)
		{
			JPanel card = (JPanel) list.getComponent(i);
			card.setBounds(6, i * ROW, width - 30, ROW - 12);
			card.getComponent(1).setBounds(62, 12, width - 210, 22);
			card.getComponent(2).setBounds(62, 34, width - 210, 20);
			card.getComponent(3).setBounds(width - 140, 12, 100, 20);
		}

		list.setPreferredSize(new Dimension(width - 20, count * ROW));
		scroll.validate();
	}

	// Adds one entry. "kind" chooses the colour and the picture, so the rest of
	// the program only has to say what happened, not what it should look like.
	public void log(String kind, String title, String detail)
	{
		Color tint = new Color(238, 244, 251);          // info -- blue

		if (kind.equals("in"))
		{
			tint = new Color(234, 247, 240);            // check in -- green
		}
		else if (kind.equals("update"))
		{
			tint = new Color(253, 247, 234);            // update -- amber
		}
		else if (kind.equals("out"))
		{
			tint = new Color(253, 238, 236);            // check out -- red
		}
		else if (kind.equals("save"))
		{
			tint = new Color(234, 244, 239);            // saved -- dark green
		}
		else if (kind.equals("warn"))
		{
			tint = new Color(254, 245, 233);            // refused -- orange
		}
		else if (kind.equals("view"))
		{
			tint = new Color(242, 244, 243);            // opened a floor -- grey
		}

		RoundedPanel card = new RoundedPanel(tint, 14);

		JLabel iconlbl = new JLabel(Icons.get("badge_" + kind));
		iconlbl.setBounds(16, 16, 34, 34);
		card.add(iconlbl);

		JLabel titlelbl = new JLabel(title);
		titlelbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
		titlelbl.setForeground(new Color(28, 46, 39));
		card.add(titlelbl);

		JLabel detaillbl = new JLabel(detail);
		detaillbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		detaillbl.setForeground(new Color(107, 117, 112));
		card.add(detaillbl);

		JLabel timelbl = new JLabel(new SimpleDateFormat("h:mm a").format(new Date()), SwingConstants.RIGHT);
		timelbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		timelbl.setForeground(new Color(150, 158, 153));
		card.add(timelbl);

		list.add(card);
		count++;
		emptylbl.setVisible(false);

		// Put every entry in its place, then scroll down to the newest one.
		layoutEntries();
		scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
	}

	public void clear()
	{
		list.removeAll();
		count = 0;
		emptylbl.setVisible(true);
		layoutEntries();
		repaint();
	}
}
