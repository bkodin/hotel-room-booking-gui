package Entity;

public class Room
{
	private int roomNo;
	private String type;
	private double price;
	private String guestName;

	public Room()
	{

	}

	public Room(int roomNo, String type, double price)
	{
		this.roomNo = roomNo;
		this.type = type;
		this.price = price;
		this.guestName = "";
	}

	public void setRoomNo(int roomNo)
	{
		this.roomNo = roomNo;
	}

	public int getRoomNo()
	{
		return roomNo;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getType()
	{
		return type;
	}

	public void setPrice(double price)
	{
		this.price = price;
	}

	public double getPrice()
	{
		return price;
	}

	public void setGuestName(String guestName)
	{
		this.guestName = guestName;
	}

	public String getGuestName()
	{
		return guestName;
	}

	public boolean isOccupied()
	{
		return !guestName.equals("");
	}
}
