package net.minecraft.src;


public class TileEntityDialer extends TileEntity 
{	
	private String currentDisplay = "";
	
	public TileEntityDialer()
	{
	}
	
	public void Dial(int n)
	{
		if (n == 10) // *
		{
			currentDisplay = "";
			return;
		}
		
		if (n == 11) // #
		{
			// behavior depends on input
			
			currentDisplay = "99999";
			return;
		}
		
		currentDisplay += n;
	}
	
	public String getDisplay()
	{
		return currentDisplay;
	}
	
}