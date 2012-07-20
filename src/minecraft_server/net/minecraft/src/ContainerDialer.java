package net.minecraft.src;

public class ContainerDialer extends Container
{
	private TileEntityDialer dialer;
	
	public ContainerDialer(InventoryPlayer player, TileEntityDialer par2dialer)
    {
        dialer = par2dialer;
		
	}
	
	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.dialer.isUseableByPlayer(par1EntityPlayer);
    }
}