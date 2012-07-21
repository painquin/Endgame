package net.minecraft.src;

import net.minecraft.src.forge.*;

public class BlockDialer extends BlockContainer implements ITextureProvider
{
	protected BlockDialer(int id)
	{
		super(id, 2, Material.rock);
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
	
	public String getTextureFile()
	{
		return "/Endgame/terrain.png";
	}
	
	/**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int getBlockTextureFromSideAndMetadata(int side, int metadata)
    {
		return side <= 1 ? 6 + (metadata & 3) : 2 + (metadata & 1);
    }
	
	public void onBlockAdded(World w, int x, int y, int z)
	{
		super.onBlockAdded(w, x, y, z);
		
		w.setBlockMetadata(x, y, z, calcMetadata(w, x, y, z));
	}
	
	public void onNeighborBlockChange(World w, int x, int y, int z, int id)
	{
		int currentMeta = w.getBlockMetadata(x, y, z);
		int newMeta = calcMetadata(w, x, y, z);
		if (currentMeta != newMeta)
		{
			w.setBlockMetadata(x, y, z, newMeta);
		}
	}
	
	public boolean isGatePart(int id)
	{
		return id == mod_Endgame.Gate.blockID || id == mod_Endgame.GateActive.blockID;
	}
	
	public int calcMetadata(World w, int x, int y, int z)
	{
		boolean hasAbove = isGatePart(w.getBlockId(x, y+1, z));
		boolean hasBelow = isGatePart(w.getBlockId(x, y-1, z));
		boolean hasEast = isGatePart(w.getBlockId(x+1, y, z));
		boolean hasWest = isGatePart(w.getBlockId(x-1, y, z));
		boolean hasSouth = isGatePart(w.getBlockId(x, y, z+1));
		boolean hasNorth = isGatePart(w.getBlockId(x, y, z-1));

		int active = w.getBlockMetadata(x, y, z) & 1;
		
		if (hasNorth || hasSouth) return 2 | active;
		return active;
	}
	
	/**
     * Returns the block hardness.
     */
    public float getHardness(int metadata)
    {
        return (metadata & 1) == 1 ? -1.0F : this.blockHardness;
    }
	
}