package GUI;

import javax.swing.*;
import java.awt.*;
import Entity.*;

// The Room Availability view. This used to be a separate pop-up window; it is now
// a panel that lives inside the main frame beside the Activity Log, so opening a
// floor never takes the manager away from the booking form.
//
// It is resizable: resizeTo() is called whenever the window changes size, and the
// twelve tiles are simply rebuilt at the new tile size.
public class RoomGridPanel extends JPanel
{
	private JLabel titlelbl;
	private JLabel countlbl;
	private JLabel vacantkey;
	private JLabel occupiedkey;
	private JPanel board;
	private JLabel emptylbl;

	private HotelManagerPage owner;
	private Floor current;
	private int width;
	private int height;

	public RoomGridPanel(int width, int height, HotelManagerPage owner)
	{
		this.owner = owner;
		this.width = width;
		this.height = height;

		this.setLayout(null);
		this.setBackground(Color.WHITE);

		titlelbl = new JLabel("Room Availability");
		titlelbl.setFont(new Font("Cambria", Font.BOLD, 18));
		titlelbl.setForeground(new Color(18, 42, 35));
		this.add(titlelbl);

		countlbl = new JLabel("Choose a floor to see every room at a glance.");
		countlbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		countlbl.setForeground(new Color(126, 136, 130));
		this.add(countlbl);

		vacantkey = legend("Vacant", "dot_green");
		occupiedkey = legend("Occupied", "dot_red");
		this.add(vacantkey);
		this.add(occupiedkey);

		// The twelve tiles are placed inside this panel.
		board = new JPanel();
		board.setLayout(null);
		board.setOpaque(false);
		this.add(board);

		emptylbl = new JLabel("No floor open", Icons.get("grid_pale"), SwingConstants.CENTER);
		emptylbl.setHorizontalTextPosition(SwingConstants.CENTER);
		emptylbl.setVerticalTextPosition(SwingConstants.BOTTOM);
		emptylbl.setIconTextGap(18);
		emptylbl.setFont(new Font("Cambria", Font.BOLD, 17));
		emptylbl.setForeground(new Color(146, 157, 150));
		this.add(emptylbl, 0);

		resizeTo(width, height);
	}

	// One "green dot + word" key shown at the top right.
	private JLabel legend(String text, String dot)
	{
		JLabel lbl = new JLabel(text, Icons.get(dot), SwingConstants.RIGHT);
		lbl.setIconTextGap(7);
		lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lbl.setForeground(new Color(110, 120, 114));
		return lbl;
	}

	// Called by the main window whenever it is resized or maximised.
	public void resizeTo(int w, int h)
	{
		width = w;
		height = h;

		titlelbl.setBounds(0, 0, 400, 26);
		countlbl.setBounds(0, 26, 460, 18);
		vacantkey.setBounds(w - 200, 4, 100, 20);
		occupiedkey.setBounds(w - 100, 4, 100, 20);
		board.setBounds(0, 58, w, h - 58);
		emptylbl.setBounds(0, 58, w, h - 120);

		if (current != null)
		{
			showFloor(current);
		}
	}

	// Rebuilds the twelve tiles for whichever floor was asked for. It is called
	// again after every check-in, check-out and resize, so the grid is never out
	// of date and always fills the space it has been given.
	public void showFloor(Floor floor)
	{
		current = floor;
		board.removeAll();

		// Three tiles across and four rows down, sharing out whatever space there is.
		int gap = 13;
		int tileW = (width - 2 * gap) / 3;
		int tileH = ((height - 58) - 3 * gap) / 4;

		Room rooms[] = floor.getRooms();
		int vacant = 0;
		int occupied = 0;
		double booked = 0.0;

		for (int i = 0; i < rooms.length; i++)
		{
			if (rooms[i].isOccupied())
			{
				occupied++;
				booked = booked + rooms[i].getPrice();
			}
			else
			{
				vacant++;
			}

			RoomTile tile = new RoomTile(rooms[i], floor.getFloorNo(), i, tileW, tileH, owner);
			tile.setBounds((i % 3) * (tileW + gap), (i / 3) * (tileH + gap), tileW, tileH);
			board.add(tile);
		}

		titlelbl.setText("Floor " + floor.getFloorNo() + "  -  " + floor.getFloorName());
		countlbl.setText(vacant + " vacant  -  " + occupied + " occupied  -  $" + (int) booked + " booked per night");

		emptylbl.setVisible(false);
		board.revalidate();
		board.repaint();
	}
}
