package File;

import java.io.*;
import java.util.*;
import Entity.*;

public class FileIO
{
	private File myfile;
	private FileWriter fwriter;
	private Scanner sc;

	// Writes one line per occupied room: floorNo;position;guestName
	// Vacant rooms are not written -- no line for a room simply means it is empty.
	//
	// "throws IOException" just tells Java "if writing to the disk ever fails,
	// don't handle it here -- pass it up to whoever called this method."
	// The one place that calls this (the Save button) is what decides what to
	// do about it, so the file-handling code itself can stay plain and simple.
	public void saveData(Floor floors[]) throws IOException
	{
		myfile = new File("./File/Occupancy.txt");
		fwriter = new FileWriter(myfile);

		for (int f = 0; f < floors.length; f++)
		{
			if (floors[f] != null)
			{
				Room rooms[] = floors[f].getRooms();

				for (int p = 0; p < rooms.length; p++)
				{
					if (rooms[p] != null && rooms[p].isOccupied())
					{
						fwriter.write(floors[f].getFloorNo() + ";" + p + ";" + rooms[p].getGuestName() + "\n");
					}
				}
			}
		}

		fwriter.close();
	}

	// Reads Occupancy.txt (if it exists) and re-applies every saved guest onto a freshly built floor list.
	public void loadData(Floor floors[]) throws IOException
	{
		myfile = new File("./File/Occupancy.txt");

		if (myfile.exists())
		{
			sc = new Scanner(myfile);

			while (sc.hasNextLine())
			{
				String line = sc.nextLine();
				String value[] = line.split(";");

				int floorNo = Integer.parseInt(value[0]);
				int position = Integer.parseInt(value[1]);
				String guestName = value[2];

				if (floorNo >= 0 && floorNo < floors.length && floors[floorNo] != null)
				{
					Room r = floors[floorNo].getRoom(position);
					if (r != null)
					{
						r.setGuestName(guestName);
					}
				}
			}

			sc.close();
		}
	}
}
