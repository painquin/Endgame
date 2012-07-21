package net.minecraft.src;

import net.minecraft.src.forge.*;

public class ItemEndgame extends Item implements ITextureProvider
{
	public int getShiftedIndex()
	{
		return super.shiftedIndex;
	}
	
	public ItemEndgame(int Id)
	{
		super(Id);
		setMaxDamage(0);
		setHasSubtypes(true);
	}
	
	public String getTextureFile()
	{
		return "/Endgame/items.png";
	}
	
	/**
     * Gets an icon index based on an item's damage value
     */
    public int getIconFromDamage(int dmg)
    {
        return dmg;
    }
	
	public String getItemDisplayName(ItemStack par1ItemStack)
    {
		switch(par1ItemStack.getItemDamage())
		{
			case 0: return "Negastone Dust";
			case 1: return "Superconductor Dust";
			case 2: return "Superconductor Ingot";
		}
		return "???";
	}
	
	public String getItemNameIS(ItemStack par1ItemStack)
    {
		switch(par1ItemStack.getItemDamage())
		{
			case 0: return "item.negastone.dust";
			case 1: return "item.superconductor.dust";
			case 2: return "item.superconductor.ingot";
		}
		return "item.???";
    }
}