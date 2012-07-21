package net.minecraft.src;

import java.util.Random;
import net.minecraft.src.forge.*;

public class BlockEndgameOre extends BlockOre implements ITextureProvider
{
	public BlockEndgameOre(int Id)
	{
		super(Id, 0);
	}

	@Override
	public int idDropped(int par1, Random par2Random, int par3)
	{
		return mod_Endgame.ModItems.getShiftedIndex();
	}
	
	public int quantityDropped(int meta, int fortune, Random random)
    {
    	return (meta == 1 ? 2 : 1) + fortune;
    }
	
	@Override
	protected int damageDropped(int par1)
    {
        return 0; // for now, always return Negastone Ore
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
		return metadata & 1;
	}
}