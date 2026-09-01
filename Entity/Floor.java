package Entity;

public class Floor
{
	private int floorNo;
	private String floorName;
	private Room rooms[];

	public Floor()
	{

	}

	public Floor(int floorNo, String floorName)
	{
		this.floorNo = floorNo;
		this.floorName = floorName;
		rooms = new Room[12];
	}

	public int getFloorNo()
	{
		return floorNo;
	}

	public String getFloorName()
	{
		return floorName;
	}

	public Room[] getRooms()
	{
		return rooms;
	}

	public Room getRoom(int position)
	{
		return rooms[position];
	}

	// Builds all 12 rooms on this floor: 5 Standard, 5 Deluxe, 2 Presidential.
	// Room number is the floor number followed by the position, e.g. Floor 3, position 2 -> Room 302.
	public void setupRooms()
	{
		for (int i = 0; i < 12; i++)
		{
			int roomNo = (floorNo * 100) + i;

			if (i < 5)
			{
				rooms[i] = new Room(roomNo, "Standard", 100.0);
			}
			else if (i < 10)
			{
				rooms[i] = new Room(roomNo, "Deluxe", 150.0);
			}
			else
			{
				rooms[i] = new Room(roomNo, "Presidential", 400.0);
			}
		}
	}
}
