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
	
	/**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }
	
}