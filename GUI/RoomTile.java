package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Entity.*;

// One room drawn as a card: the tier picture, the room number, the price and a
// coloured status pill. It extends RoundedPanel, so the rounded card is already
// drawn for it and this class only fills in the contents.
//
// The tile is told how wide and tall it should be, so the twelve tiles can grow
// with the window instead of staying one fixed size.
//
// Clicking a tile loads that room into the booking form on the left, so the grid
// is a way of PICKING a room, not just a picture of one.
public class RoomTile extends RoundedPanel implements MouseListener
{
	private Color base;
	private Color hoverColor;

	private HotelManagerPage owner;
	private int floorNo;
	private int position;

	public RoomTile(Room r, int floorNo, int position, int w, int h, HotelManagerPage owner)
	{
		super(new Color(243, 251, 246), 16);

		this.owner = owner;
		this.floorNo = floorNo;
		this.position = position;

		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.addMouseListener(this);

		Color pillColor;
		Color textColor;
		String pillIcon;
		String pillText;

		if (r.isOccupied())
		{
			base = new Color(253, 243, 242);
			hoverColor = new Color(250, 233, 231);
			pillColor = new Color(250, 227, 224);
			textColor = new Color(158, 50, 42);
			pillIcon = "user_red";
			pillText = r.getGuestName();
		}
		else
		{
			base = new Color(243, 251, 246);
			hoverColor = new Color(230, 246, 237);
			pillColor = new Color(222, 242, 230);
			textColor = new Color(22, 106, 72);
			pillIcon = "check_green";
			pillText = "Vacant";
		}

		this.setColor(base);

		JLabel typeicon = new JLabel(Icons.roomType(r.getType(), 32));
		typeicon.setBounds(14, 12, 32, 32);
		this.add(typeicon);

		JLabel roomlbl = new JLabel("Room " + r.getRoomNo());
		roomlbl.setBounds(54, 11, w - 70, 22);
		roomlbl.setFont(new Font("Cambria", Font.BOLD, 16));
		roomlbl.setForeground(new Color(22, 46, 38));
		this.add(roomlbl);

		JLabel typelbl = new JLabel(r.getType());
		typelbl.setBounds(54, 32, w - 128, 16);
		typelbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		typelbl.setForeground(new Color(132, 140, 135));
		this.add(typelbl);

		JLabel pricelbl = new JLabel("$" + (int) r.getPrice(), SwingConstants.RIGHT);
		pricelbl.setBounds(w - 68, 32, 54, 16);
		pricelbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
		pricelbl.setForeground(new Color(150, 114, 40));
		this.add(pricelbl);

		RoundedPanel pill = new RoundedPanel(pillColor, 16);
		pill.setBounds(13, h - 54, w - 26, 32);
		this.add(pill);

		JLabel pilllbl = new JLabel(pillText, Icons.get(pillIcon), SwingConstants.LEFT);
		pilllbl.setBounds(10, 0, w - 46, 32);
		pilllbl.setIconTextGap(6);
		pilllbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
		pilllbl.setForeground(textColor);
		pill.add(pilllbl);
	}

	public void mouseClicked(MouseEvent me)
	{
		owner.selectRoom(floorNo, position);
	}

	public void mouseEntered(MouseEvent me)
	{
		setColor(hoverColor);
	}

	public void mouseExited(MouseEvent me)
	{
		setColor(base);
	}

	public void mousePressed(MouseEvent me) {}
	public void mouseReleased(MouseEvent me) {}
}
