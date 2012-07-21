package net.minecraft.src;

import net.minecraft.src.forge.*;

public class BlockGate extends Block implements ITextureProvider
{
	protected BlockGate(int id)
	{
		super(id, 4, Material.rock);
	}
	
	public String getTextureFile()
	{
		return "/Endgame/terrain.png";
	}
	
	public void onBlockAdded(World w, int x, int y, int z)
	{
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
		return id == mod_Endgame.Gate.blockID || id == mod_Endgame.GateActive.blockID || id == mod_Endgame.Dialer.blockID;
	}
	
	public int calcMetadata(World w, int x, int y, int z)
	{
		
		boolean hasAbove = isGatePart(w.getBlockId(x, y+1, z));
		boolean hasBelow = isGatePart(w.getBlockId(x, y-1, z));
		boolean hasEast = isGatePart(w.getBlockId(x+1, y, z));
		boolean hasWest = isGatePart(w.getBlockId(x-1, y, z));
		boolean hasSouth = isGatePart(w.getBlockId(x, y, z+1));
		boolean hasNorth = isGatePart(w.getBlockId(x, y, z-1));
		
		
		// vertical
		if (hasAbove && hasBelow) return 4;
		
		// horizontal EW
		if (hasEast && hasWest) return 2;
		
		// horizontal NS
		if (hasSouth && hasNorth) return 3;
		
		// Bottom Left
		if (hasAbove && hasEast) return 12;
		if (hasAbove && hasSouth) return 13;
		
		// Bottom Right
		if (hasAbove && hasWest) return 10;
		if (hasAbove && hasNorth) return 11;
		
		// Top Left
		if (hasBelow && hasEast) return 6;
		if (hasBelow && hasSouth) return 7;
		
		// Top Right
		if (hasBelow && hasWest) return 8;
		if (hasBelow && hasNorth) return 9;

		return 0;		
	}
	
	
	/**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int getBlockTextureFromSideAndMetadata(int side, int metadata)
    {
		if (metadata == 0) return 4;
		
		switch(metadata)
		{
			case 0: return 4;
			case 4: return 8;
		}
		
		switch(side)
		{
			case 0: case 1:
				return (metadata & 1) == 0 ? 6 : 8;
			case 2:
				switch(metadata)
				{
					case 2: case 3: return 6;
					case 6: return 12;
					case 7: return 8;
					case 8: return 10;
					case 9: return 8;
					case 10: return 16;
					case 11: return 8;
					case 12: return 14;
					case 13: return 8;
				}
				break;
			case 3:
				switch(metadata)
				{
					case 2: case 3: return 6;
					case 6: return 10;
					case 7: return 8;
					case 8: return 12;
					case 9: return 8;
					case 10: return 14;
					case 11: return 8;
					case 12: return 16;
					case 13: return 8;
				}
				break;
			case 4:
				switch(metadata)
				{
					case 2: case 3: return 6;
					case 6: return 8;
					case 7: return 10;
					case 8: return 8;
					case 9: return 12;
					case 10: return 8;
					case 11: return 14;
					case 12: return 8;
					case 13: return 16;
				}
				break;
			case 5:
				switch(metadata)
				{
					case 2: case 3: return 6;
					case 6: return 8;
					case 7: return 12;
					case 8: return 8;
					case 9: return 10;
					case 10: return 8;
					case 11: return 16;
					case 12: return 8;
					case 13: return 14;
				}
				break;
		}
		
		return 0;
		
		
    }
}