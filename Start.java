import java.lang.*;
import javax.swing.*;
import Entity.*;
import File.*;
import GUI.*;

public class Start
{
	public static void main(String[] args)
	{
		// Windows is often set to enlarge everything by 125%. When that happens Java
		// stretches the whole window, and stretching a picture is what makes icons
		// look fuzzy. This line asks Java to draw at the real pixel size instead, so
		// every icon is shown exactly as it was saved. It must run before any window
		// is created, which is why it is the first line of the program.
		System.setProperty("sun.java2d.uiScale", "1");

		// Use the computer's own window style, so the text boxes, scroll bars and
		// message dialogs look native instead of dated.
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			// Not a problem -- Java just keeps its default look.
		}

		HotelManagerPage m1 = new HotelManagerPage();
		m1.setVisible(true);
	}
}
