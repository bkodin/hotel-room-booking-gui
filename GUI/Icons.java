package GUI;

import javax.swing.*;

// Every icon is an ordinary PNG picture inside the images/icons folder, saved at
// exactly the size it is shown at. This class just builds the file name.
// To change an icon, replace its PNG -- no Java code has to be touched.
public class Icons
{
	// Example: get("key_white") loads images/icons/key_white.png
	public static ImageIcon get(String name)
	{
		return new ImageIcon("images/icons/" + name + ".png");
	}

	// The picture for a room tier: 32 pixels on a room tile, 24 in the tier list.
	public static ImageIcon roomType(String type, int size)
	{
		return get("tier_" + type.toLowerCase() + "_" + size);
	}
}
