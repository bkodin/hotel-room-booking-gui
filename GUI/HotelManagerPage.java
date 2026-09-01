package GUI;

import java.lang.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import Entity.*;
import File.*;

public class HotelManagerPage extends JFrame implements ActionListener, MouseListener, FocusListener, ComponentListener
{
	// ---------- the house colours, all kept in one place ----------
	private static final Color DEEP     = new Color(18, 42, 35);
	private static final Color DEEP2    = new Color(34, 70, 58);
	private static final Color GOLD     = new Color(196, 154, 61);
	private static final Color CREAM    = new Color(228, 196, 133);
	private static final Color INK      = new Color(22, 46, 38);
	private static final Color MUTED    = new Color(122, 132, 126);
	private static final Color LINE     = new Color(226, 232, 228);

	private static final Color GREEN    = new Color(20, 74, 58);
	private static final Color GREENUP  = new Color(35, 110, 85);
	private static final Color AMBER    = new Color(169, 125, 47);
	private static final Color AMBERUP  = new Color(200, 155, 65);
	private static final Color BLUE     = new Color(50, 90, 140);
	private static final Color BLUEUP   = new Color(75, 120, 175);
	private static final Color RED      = new Color(140, 45, 40);
	private static final Color REDUP    = new Color(175, 65, 55);
	private static final Color GREY     = new Color(88, 96, 91);
	private static final Color GREYUP   = new Color(118, 128, 122);
	private static final Color TAB      = new Color(238, 241, 239);
	private static final Color TABUP    = new Color(226, 232, 228);
	private static final Color TABINK   = new Color(90, 100, 94);

	private JTextField floorfld, posfld, guestfld;
	private RoundedButton insertbtn, updatebtn, getbtn, deletebtn, showbtn, savebtn, clearbtn;
	private RoundedButton logtab, gridtab;

	private ActivityLogPanel logPanel;
	private RoomGridPanel gridPanel;

	// The parts that have to move or stretch when the window is resized.
	private JPanel panel;
	private RoundedPanel statCard, formCard, rightCard;
	private JPanel tierBox, tierSep, tabSep;

	private JLabel occupancylbl;
	private OccupancyBar occupancybar;

	private Floor floors[];
	private FileIO fileio;
	private int openFloor;

	// Nicer names for the seven floors, shown in the grid header and the log.
	private String levelNames[] = {"", "Garden Level", "Courtyard Level", "Harbour Level",
			"Skyline Level", "Executive Level", "Panorama Level", "Aurelian Crown"};

	public HotelManagerPage()
	{
		super("Hotel Room Booking Management System");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setIconImage(new ImageIcon("images/app_icon.png").getImage());

		// Build 7 floors of 12 rooms, then load any guests saved from last time.
		floors = new Floor[8];
		for (int i = 1; i <= 7; i++)
		{
			floors[i] = new Floor(i, levelNames[i]);
			floors[i].setupRooms();
		}

		fileio = new FileIO();

		// Same idea as the two catches further down (Floor Number, Room Position):
		// try the risky part, and if it fails, do the simple safe thing instead.
		// Here that just means starting with every room empty.
		try
		{
			fileio.loadData(floors);
		}
		catch (IOException ioe)
		{
			// No saved file yet, or it could not be read -- start empty.
		}

		openFloor = 0;

		// The green background fills the whole window. 1156 x 796 is only the
		// STARTING size -- componentResized() below re-arranges everything so the
		// two cards stretch to fill the window when it is resized or maximised.
		panel = new GradientPanel(DEEP, DEEP2);
		panel.setPreferredSize(new Dimension(1156, 796));
		panel.addComponentListener(this);

		buildHeader(panel);
		buildFormCard(panel);
		buildRightCard(panel);

		this.add(panel);
		this.pack();
		this.setMinimumSize(this.getSize());   // never let it be shrunk past the design
		this.setLocationRelativeTo(null);

		showTab("log");
		refreshOccupancy();
		logPanel.log("info", "Welcome to The Aurelian Grand",
				countOccupied() + " of 84 rooms were already booked when the program started");
	}

	// ================= the header strip =================

	private void buildHeader(JPanel panel)
	{
		JLabel logo = new JLabel(Icons.get("logo_64"));
		logo.setBounds(26, 11, 64, 64);
		panel.add(logo);

		JLabel titlelbl = new JLabel("The Aurelian Grand");
		titlelbl.setBounds(104, 14, 600, 36);
		titlelbl.setFont(new Font("Cambria", Font.BOLD, 27));
		titlelbl.setForeground(Color.WHITE);
		panel.add(titlelbl);

		JLabel subtitlelbl = new JLabel("Hotel Room Booking Management System   -   7 Floors   -   12 Rooms per Floor   -   84 Rooms");
		subtitlelbl.setBounds(106, 49, 720, 22);
		subtitlelbl.setFont(new Font("Cambria", Font.ITALIC, 13));
		subtitlelbl.setForeground(CREAM);
		panel.add(subtitlelbl);

		// Live occupancy read-out, refreshed after every check-in and check-out.
		statCard = new RoundedPanel(new Color(26, 58, 48), 16);
		statCard.setBounds(828, 12, 300, 72);
		panel.add(statCard);

		JLabel staticon = new JLabel(Icons.get("bell_gold"));
		staticon.setBounds(18, 12, 20, 20);
		statCard.add(staticon);

		JLabel statheading = new JLabel("TONIGHT'S OCCUPANCY");
		statheading.setBounds(46, 12, 240, 18);
		statheading.setFont(new Font("Segoe UI", Font.BOLD, 11));
		statheading.setForeground(CREAM);
		statCard.add(statheading);

		occupancylbl = new JLabel("0 of 84 rooms occupied");
		occupancylbl.setBounds(18, 32, 264, 22);
		occupancylbl.setFont(new Font("Cambria", Font.BOLD, 16));
		occupancylbl.setForeground(Color.WHITE);
		statCard.add(occupancylbl);

		occupancybar = new OccupancyBar(new Color(16, 42, 34), GOLD);
		occupancybar.setBounds(18, 56, 264, 8);
		statCard.add(occupancybar);
	}

	// ================= left card: the booking form =================

	private void buildFormCard(JPanel panel)
	{
		RoundedPanel card = new RoundedPanel(Color.WHITE, 20);
		card.setBounds(28, 100, 430, 672);
		formCard = card;
		panel.add(card);

		JLabel headicon = new JLabel(Icons.get("key_gold"));
		headicon.setBounds(20, 17, 24, 24);
		card.add(headicon);

		JLabel heading = new JLabel("Reserve a Room");
		heading.setBounds(52, 15, 300, 28);
		heading.setFont(new Font("Cambria", Font.BOLD, 19));
		heading.setForeground(INK);
		card.add(heading);

		card.add(separator(20, 54, 390));

		floorfld = addField(card, "layers_gold", "Floor Number  (1 - 7)", 70);
		posfld   = addField(card, "hash_gold",   "Room Position  (0 - 11)", 140);
		guestfld = addField(card, "user_gold",   "Guest Name", 210);
		guestfld.addActionListener(this);          // pressing Enter checks the guest in

		insertbtn = addButton(card, "Insert (Check In)",  "key_white",    GREEN, GREENUP, 20,  288, 190);
		updatebtn = addButton(card, "Update Guest",       "pencil_white", AMBER, AMBERUP, 220, 288, 190);
		getbtn    = addButton(card, "Get Room Info",      "search_white", BLUE,  BLUEUP,  20,  342, 190);
		deletebtn = addButton(card, "Delete (Check Out)", "exit_white",   RED,   REDUP,   220, 342, 190);
		showbtn   = addButton(card, "View Floor Availability", "grid_white", GREY, GREYUP, 20, 396, 390);

		card.add(separator(20, 456, 390));

		savebtn  = addButton(card, "Save to File", "save_white",  GREEN, GREENUP, 20,  470, 190);
		clearbtn = addButton(card, "Clear Log",    "trash_white", GREY,  GREYUP,  220, 470, 190);

		tierSep = separator(20, 528, 390);
		card.add(tierSep);

		// The three tiers live in their own little box, so the whole strip can be
		// pushed to the bottom of the card with one setBounds when the card grows.
		tierBox = new JPanel();
		tierBox.setLayout(null);
		tierBox.setOpaque(false);
		tierBox.setBounds(20, 542, 390, 130);
		card.add(tierBox);

		JLabel tiericon = new JLabel(Icons.get("crown_gold"));
		tiericon.setBounds(0, 0, 18, 18);
		tierBox.add(tiericon);

		JLabel tierheading = new JLabel("ROOM TIERS");
		tierheading.setBounds(26, -1, 200, 20);
		tierheading.setFont(new Font("Segoe UI", Font.BOLD, 11));
		tierheading.setForeground(MUTED);
		tierBox.add(tierheading);

		addTierRow(tierBox, "Standard",     "$100 / night", "5 per floor", 28);
		addTierRow(tierBox, "Deluxe",       "$150 / night", "5 per floor", 60);
		addTierRow(tierBox, "Presidential", "$400 / night", "2 per floor", 92);
	}

	// A small icon, a caption above, and the text box underneath.
	private JTextField addField(JPanel card, String iconFile, String caption, int y)
	{
		JLabel icon = new JLabel(Icons.get(iconFile));
		icon.setBounds(20, y - 1, 18, 18);
		card.add(icon);

		JLabel lbl = new JLabel(caption);
		lbl.setBounds(46, y - 2, 300, 20);
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lbl.setForeground(new Color(78, 88, 82));
		card.add(lbl);

		JTextField fld = new JTextField();
		fld.setBounds(20, y + 24, 390, 34);
		fld.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		fld.setBackground(new Color(250, 251, 250));
		fld.setForeground(INK);
		fld.setBorder(BorderFactory.createLineBorder(LINE, 1));
		fld.addFocusListener(this);
		card.add(fld);

		return fld;
	}

	private RoundedButton addButton(JPanel card, String text, String iconFile,
			Color normal, Color hover, int x, int y, int w)
	{
		RoundedButton b = new RoundedButton(text);
		b.setBounds(x, y, w, 44);
		b.setColors(normal, hover);
		b.setIcon(Icons.get(iconFile));
		b.addActionListener(this);
		b.addMouseListener(this);
		card.add(b);
		return b;
	}

	// One line of the tier summary that replaced the old Suite Gallery window.
	private void addTierRow(JPanel card, String type, String price, String note, int y)
	{
		RoundedPanel row = new RoundedPanel(new Color(250, 249, 245), 12);
		row.setBounds(0, y, 390, 28);
		card.add(row);

		JLabel icon = new JLabel(Icons.roomType(type, 24));
		icon.setBounds(8, 2, 24, 24);
		row.add(icon);

		JLabel namelbl = new JLabel(type);
		namelbl.setBounds(42, 0, 130, 28);
		namelbl.setFont(new Font("Cambria", Font.BOLD, 14));
		namelbl.setForeground(INK);
		row.add(namelbl);

		JLabel notelbl = new JLabel(note);
		notelbl.setBounds(162, 0, 110, 28);
		notelbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		notelbl.setForeground(MUTED);
		row.add(notelbl);

		JLabel pricelbl = new JLabel(price, SwingConstants.RIGHT);
		pricelbl.setBounds(258, 0, 122, 28);
		pricelbl.setFont(new Font("Cambria", Font.BOLD, 13));
		pricelbl.setForeground(new Color(150, 114, 40));
		row.add(pricelbl);
	}

	private JPanel separator(int x, int y, int w)
	{
		JPanel sep = new JPanel();
		sep.setBounds(x, y, w, 1);
		sep.setBackground(LINE);
		return sep;
	}

	// ================= right card: the two tabs =================

	private void buildRightCard(JPanel panel)
	{
		RoundedPanel card = new RoundedPanel(Color.WHITE, 20);
		card.setBounds(478, 100, 650, 672);
		rightCard = card;
		panel.add(card);

		logtab = addButton(card, "Activity Log", "list_white", GREEN, GREEN, 18, 16, 196);
		logtab.setBounds(18, 16, 196, 42);

		gridtab = addButton(card, "Room Availability", "grid_grey", TAB, TABUP, 222, 16, 220);
		gridtab.setBounds(222, 16, 220, 42);

		tabSep = separator(18, 70, 614);
		card.add(tabSep);

		// Both views sit in exactly the same place. Switching tabs simply hides
		// one and shows the other -- that is what keeps the room grid inside the
		// window instead of opening a second one.
		logPanel = new ActivityLogPanel(614, 574);
		logPanel.setBounds(18, 82, 614, 574);
		card.add(logPanel);

		gridPanel = new RoomGridPanel(614, 574, this);
		gridPanel.setBounds(18, 82, 614, 574);
		card.add(gridPanel);
	}

	private void showTab(String which)
	{
		boolean showLog = which.equals("log");

		logPanel.setVisible(showLog);
		gridPanel.setVisible(showLog == false);

		styleTab(logtab, "list", showLog);
		styleTab(gridtab, "grid", showLog == false);
		repaint();
	}

	// The chosen tab is dark green with a white picture; the other one is grey.
	private void styleTab(RoundedButton tab, String iconName, boolean chosen)
	{
		if (chosen)
		{
			tab.setColors(GREEN, GREEN);
			tab.setForeground(Color.WHITE);
			tab.setIcon(Icons.get(iconName + "_white"));
		}
		else
		{
			tab.setColors(TAB, TABUP);
			tab.setForeground(TABINK);
			tab.setIcon(Icons.get(iconName + "_grey"));
		}
	}

	// ================= reading the three boxes =================
	// All four operations need the same checks, so they are written once here.
	// Each method returns -1 when the value is unusable, after explaining why.

	private int readFloor()
	{
		String text = floorfld.getText().trim();

		if (text.equals(""))
		{
			problem("Please type a Floor Number (1 - 7).");
			return -1;
		}

		try
		{
			int floorNo = Integer.parseInt(text);

			if (floorNo < 1 || floorNo > 7)
			{
				problem("Floor Number must be between 1 and 7.");
				return -1;
			}

			return floorNo;
		}
		catch (NumberFormatException nfe)
		{
			problem("Floor Number must be a number.");
			return -1;
		}
	}

	private int readPosition()
	{
		String text = posfld.getText().trim();

		if (text.equals(""))
		{
			problem("Please type a Room Position (0 - 11).");
			return -1;
		}

		try
		{
			int position = Integer.parseInt(text);

			if (position < 0 || position > 11)
			{
				problem("Room Position must be between 0 and 11.");
				return -1;
			}

			return position;
		}
		catch (NumberFormatException nfe)
		{
			problem("Room Position must be a number.");
			return -1;
		}
	}

	// A refused action both pops up a message and leaves a trail in the log.
	private void problem(String message)
	{
		logPanel.log("warn", "Action cancelled", message);
		showTab("log");
		JOptionPane.showMessageDialog(this, message, "Hotel Room Booking", JOptionPane.WARNING_MESSAGE);
	}

	// ================= what the buttons do =================

	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource() == insertbtn || ae.getSource() == guestfld)
		{
			insertGuest();
		}
		else if (ae.getSource() == updatebtn)
		{
			updateGuest();
		}
		else if (ae.getSource() == getbtn)
		{
			getRoomInfo();
		}
		else if (ae.getSource() == deletebtn)
		{
			deleteGuest();
		}
		else if (ae.getSource() == showbtn)
		{
			showAllRooms();
		}
		else if (ae.getSource() == savebtn)
		{
			try
			{
				fileio.saveData(floors);
				logPanel.log("save", "Saved to file", "Every occupied room was written to File/Occupancy.txt");
				showTab("log");
				JOptionPane.showMessageDialog(this, "Data saved to file.", "Saved", JOptionPane.INFORMATION_MESSAGE);
			}
			catch (IOException ioe)
			{
				problem("Could not save to file.");
			}
		}
		else if (ae.getSource() == clearbtn)
		{
			logPanel.clear();
			showTab("log");
		}
		else if (ae.getSource() == logtab)
		{
			showTab("log");
		}
		else if (ae.getSource() == gridtab)
		{
			showTab("grid");
		}
	}

	// ---------- Insert ----------
	private void insertGuest()
	{
		int floorNo = readFloor();
		if (floorNo == -1) return;

		int position = readPosition();
		if (position == -1) return;

		String name = guestfld.getText().trim();

		if (name.equals(""))
		{
			problem("Please type the Guest Name.");
			return;
		}

		Room r = floors[floorNo].getRoom(position);

		if (r.isOccupied())
		{
			problem("Room " + r.getRoomNo() + " is already taken by " + r.getGuestName() + ".");
			return;
		}

		r.setGuestName(name);
		logPanel.log("in", "Checked in " + name,
				"Room " + r.getRoomNo() + "  -  " + r.getType() + "  -  $" + (int) r.getPrice() + " per night");
		afterChange();
	}

	// ---------- Update ----------
	private void updateGuest()
	{
		int floorNo = readFloor();
		if (floorNo == -1) return;

		int position = readPosition();
		if (position == -1) return;

		String name = guestfld.getText().trim();

		if (name.equals(""))
		{
			problem("Please type the new Guest Name.");
			return;
		}

		Room r = floors[floorNo].getRoom(position);

		if (r.isOccupied() == false)
		{
			problem("Room " + r.getRoomNo() + " is vacant, so there is no guest to rename.");
			return;
		}

		String oldName = r.getGuestName();
		r.setGuestName(name);
		logPanel.log("update", "Renamed guest in Room " + r.getRoomNo(), oldName + "  ->  " + name);
		afterChange();
	}

	// ---------- Get ----------
	private void getRoomInfo()
	{
		int floorNo = readFloor();
		if (floorNo == -1) return;

		int position = readPosition();
		if (position == -1) return;

		Room r = floors[floorNo].getRoom(position);
		String status = "Vacant";

		if (r.isOccupied())
		{
			status = "Occupied by " + r.getGuestName();
		}

		logPanel.log("info", "Room " + r.getRoomNo() + "  -  " + status,
				r.getType() + "  -  $" + (int) r.getPrice() + " per night  -  Floor " + floorNo + ", position " + position);
		showTab("log");
	}

	// ---------- Delete ----------
	private void deleteGuest()
	{
		int floorNo = readFloor();
		if (floorNo == -1) return;

		int position = readPosition();
		if (position == -1) return;

		Room r = floors[floorNo].getRoom(position);

		if (r.isOccupied() == false)
		{
			problem("Room " + r.getRoomNo() + " is already vacant.");
			return;
		}

		String name = r.getGuestName();
		r.setGuestName("");
		logPanel.log("out", "Checked out " + name, "Room " + r.getRoomNo() + " is now vacant and ready to sell");
		afterChange();
	}

	// ---------- Show a whole floor, right here in the window ----------
	private void showAllRooms()
	{
		int floorNo = readFloor();
		if (floorNo == -1) return;

		openFloor = floorNo;
		gridPanel.showFloor(floors[floorNo]);
		showTab("grid");
		logPanel.log("view", "Opened Floor " + floorNo + "  -  " + levelNames[floorNo],
				"All 12 rooms are shown in the Room Availability tab");
	}

	// Called by a room tile when it is clicked, so the grid doubles as a picker.
	public void selectRoom(int floorNo, int position)
	{
		Room r = floors[floorNo].getRoom(position);

		floorfld.setText("" + floorNo);
		posfld.setText("" + position);
		guestfld.setText(r.getGuestName());
		guestfld.requestFocus();
	}

	// ================= keeping the screen up to date =================

	// After anything changes: clear the boxes, update the counter, refresh the
	// open floor and go back to the log so the new entry is visible.
	private void afterChange()
	{
		floorfld.setText("");
		posfld.setText("");
		guestfld.setText("");

		refreshOccupancy();

		if (openFloor >= 1)
		{
			gridPanel.showFloor(floors[openFloor]);
		}

		showTab("log");
	}

	private int countOccupied()
	{
		int occupied = 0;

		for (int f = 1; f <= 7; f++)
		{
			Room rooms[] = floors[f].getRooms();

			for (int p = 0; p < rooms.length; p++)
			{
				if (rooms[p].isOccupied())
				{
					occupied++;
				}
			}
		}

		return occupied;
	}

	private void refreshOccupancy()
	{
		int occupied = countOccupied();
		occupancylbl.setText(occupied + " of 84 rooms occupied");
		occupancybar.setValue(occupied, 84);
	}

	// ================= making everything stretch =================

	// Called by Java every time the window changes size. It works out how much
	// room there is and hands it out: the form keeps its 430 pixel width, and the
	// right hand card takes everything that is left over.
	public void componentResized(ComponentEvent ce)
	{
		int w = panel.getWidth();
		int h = panel.getHeight();

		statCard.setBounds(w - 328, 12, 300, 72);

		int cardH = h - 124;                    // 100 above the cards, 24 below
		formCard.setBounds(28, 100, 430, cardH);
		tierSep.setBounds(20, cardH - 152, 390, 1);
		tierBox.setBounds(20, cardH - 138, 390, 130);

		int rightW = w - 506;                   // 478 from the left, 28 spare on the right
		rightCard.setBounds(478, 100, rightW, cardH);

		int innerW = rightW - 36;
		int innerH = cardH - 98;
		tabSep.setBounds(18, 70, innerW, 1);

		logPanel.setBounds(18, 82, innerW, innerH);
		logPanel.resizeTo(innerW, innerH);

		gridPanel.setBounds(18, 82, innerW, innerH);
		gridPanel.resizeTo(innerW, innerH);
	}

	public void componentMoved(ComponentEvent ce) {}
	public void componentShown(ComponentEvent ce) {}
	public void componentHidden(ComponentEvent ce) {}

	// ================= the other listeners =================

	// The box being typed into gets a gold border so it stands out.
	public void focusGained(FocusEvent fe)
	{
		JTextField f = (JTextField) fe.getSource();
		f.setBorder(BorderFactory.createLineBorder(GOLD, 1));
		f.setBackground(Color.WHITE);
	}

	public void focusLost(FocusEvent fe)
	{
		JTextField f = (JTextField) fe.getSource();
		f.setBorder(BorderFactory.createLineBorder(LINE, 1));
		f.setBackground(new Color(250, 251, 250));
	}

	// Each button already knows its own two colours, so hovering is just a swap.
	public void mouseEntered(MouseEvent me)
	{
		RoundedButton b = (RoundedButton) me.getSource();
		b.setBackground(b.getHoverColor());
		repaint();
	}

	public void mouseExited(MouseEvent me)
	{
		RoundedButton b = (RoundedButton) me.getSource();
		b.setBackground(b.getNormalColor());
		repaint();
	}

	public void mouseClicked(MouseEvent me) {}
	public void mousePressed(MouseEvent me) {}
	public void mouseReleased(MouseEvent me) {}
}
