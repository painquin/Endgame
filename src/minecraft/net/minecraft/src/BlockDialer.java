package net.minecraft.src;

public class BlockDialer extends BlockContainer
{
	protected BlockDialer(int id)
	{
		super(id, Material.rock);
	}
	
	/**
     * Called upon block activation (left or right click on the block.). The three integers represent x,y,z of the
     * block.
     */
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player)
    {
        if (world.isRemote)
        {
            return true;
        }
        else
        {
			player.openGui(mod_Endgame.Instance, 1, world, x, y, z);
            return true;
        }
    }
	
	/**
     * Returns the TileEntity used by this block.
     */
    public TileEntity getBlockEntity()
    {
		return new TileEntityDialer();
    }
	
	
	
}