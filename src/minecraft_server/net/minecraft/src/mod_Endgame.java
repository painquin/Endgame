package net.minecraft.src;

import net.minecraft.src.ic2.api.*;
import net.minecraft.src.forge.*;
import java.util.Random;
import java.lang.reflect.*;

public class mod_Endgame extends NetworkMod
{
	public static Block NegastoneOre;
	public static Block NegastoneOre_Nether;
	
	public static Item NegastoneDust;
	public static Item SuperconductorDust;
	public static Item SuperconductorIngot;
	
	public static BlockGate Gate;
	public static BlockDialer Dialer;
	
	@Override
	public boolean clientSideRequired()
	{
			return true;
	}

	@Override
	public boolean serverSideRequired()
	{
			return false;
	}
		
	public mod_Endgame()
	{
	
	}
	
	public void load()
	{
	
		DisableNetherPortals();
		
		//todo: combine into a single item, use meta info to distinguish
		NegastoneDust = new Item(20000).setItemName("negastone.dust");
		NegastoneDust.iconIndex = ModLoader.addOverride("/gui/items.png", "Endgame/dust.png");
		ModLoader.addName(NegastoneDust, "Negastone Dust");
		
		SuperconductorDust = new Item(20001).setItemName("superconductor.dust");
		SuperconductorDust.iconIndex = ModLoader.addOverride("/gui/items.png", "Endgame/sdust.png");
		ModLoader.addName(SuperconductorDust, "Superconductor Dust");
		
		SuperconductorIngot = new Item(20002).setItemName("superconductor.ingot");
		SuperconductorIngot.iconIndex = ModLoader.addOverride("/gui/items.png", "Endgame/ingot.png");
		ModLoader.addName(SuperconductorIngot, "Superconductor Ingot");
		
		ModLoader.addSmelting(SuperconductorDust.shiftedIndex, new ItemStack(SuperconductorIngot, 1));
		
		
		Gate = new BlockGate(182);
		Gate.setBlockName("endgame.gate")
			.setHardness(3F)
			.setResistance(25f)
			.blockIndexInTexture = ModLoader.addOverride("/terrain.png", "Endgame/gate.png");
		
		ModLoader.addName(Gate, "Gate");
		ModLoader.registerBlock(Gate);
		
		Dialer = new BlockDialer(183);
		
		Dialer.setBlockName("endgame.dialer")
			.setHardness(3F)
			.setResistance(15f)
			.blockIndexInTexture = ModLoader.addOverride("/terrain.png", "Endgame/dialer.png");
		ModLoader.addName(Dialer, "Dialer");
		ModLoader.registerBlock(Dialer);
		
		
		// todo: combine into a single block, use meta info to distinguish
		NegastoneOre = new Block(180, Material.rock)
		{
			/**
			 * Returns the ID of the items to drop on destruction.
			 */
			public int idDropped(int par1, Random par2Random, int par3)
			{
				return NegastoneDust.shiftedIndex;
			}

			/**
			 * Returns the usual quantity dropped by the block plus a bonus of 1 to 'i' (inclusive).
			 */
			public int quantityDroppedWithBonus(int par1, Random par2Random)
			{
				return quantityDropped(par2Random) + par2Random.nextInt(par1 + 1);
			}

			/**
			 * Returns the quantity of items to drop on block destruction.
			 */
			public int quantityDropped(Random par1Random)
			{
				return 1;
			}
		};
		
		NegastoneOre
			.setBlockName("negastone.ore")
			.setHardness(3F)
			.setResistance(15f)
			.blockIndexInTexture = ModLoader.addOverride("/terrain.png", "Endgame/ore.png");
		
		MinecraftForge.setBlockHarvestLevel(NegastoneOre, "pickaxe", 2);
		ModLoader.addName(NegastoneOre, "Negastone Ore");
		ModLoader.registerBlock(NegastoneOre);
		
		NegastoneOre_Nether = new Block(181, Material.rock);
		
		NegastoneOre_Nether
			.setBlockName("negastonenether.ore")
			.setHardness(2F)
			.setResistance(10f)
			.blockIndexInTexture = ModLoader.addOverride("/terrain.png", "Endgame/nore.png");
		
		MinecraftForge.setBlockHarvestLevel(NegastoneOre_Nether, "pickaxe", 1);
		ModLoader.addName(NegastoneOre_Nether, "Negastone Ore");
		ModLoader.registerBlock(NegastoneOre_Nether);
		
	}
	
	public void DisableNetherPortals()
	{
		try{
			Class block = Class.forName("net.minecraft.src.Block");
			Field blockportal = block.getField("portal");
			
			Field modifiersField = Field.class.getDeclaredField("modifiers"); // this is actually reflection ON the Field class.
			modifiersField.setAccessible(true);
			int modifiers = modifiersField.getInt(blockportal);
			modifiers &= ~Modifier.FINAL;
			modifiersField.setInt(blockportal, modifiers);
			blockportal.set(null, new BlockPortal(179, 14)
			{
				@Override
				public boolean tryToCreatePortal(World par1World, int par2, int par3, int par4)
				{
					return false;
				}
			});

		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	
	Perlin p = new Perlin(3);
	
	public void generateSurface(World world, Random rand, int chunkX, int chunkZ)
	{
		int lavaId = Block.lavaStill.blockID;
		
		int maxHeight = world.getHeight();
		
		for(int x = chunkX; x < chunkX+16; ++x)
		{
			for(int z = chunkZ; z < chunkZ+16; ++z)
			{
				for(int y = 0; y < maxHeight; ++y)
				{
					// we only replace smoothstone with negastone
					if (world.getBlockId(x,y,z) != Block.stone.blockID) continue;
					
					// and only under lava
					int under = world.getBlockId(x,y+1,z);
					if (under != Block.lavaStill.blockID && under != Block.lavaMoving.blockID) continue;
						
					// based on this 3D density map
					float v = p.ValueAt(x * 0.312F, y * 0.312F, z * 0.312F) * 0.75F;
					v += p.ValueAt(x * 0.72F, y * 0.72F, z * 0.72F) * 0.25f;
					if (v > 0.6F)
					{
						world.setBlock(x, y, z, NegastoneOre.blockID);
					}
				}
			}
		}
	}
		
	public void generateNether(World world, Random rand, int chunkX, int chunkZ)
	{
		int lavaId = Block.lavaStill.blockID;
		int maxHeight = world.getHeight();
		
		for(int x = chunkX; x < chunkX+16; ++x)
		{
			for(int z = chunkZ; z < chunkZ+16; ++z)
			{
				for(int y = 0; y < maxHeight; ++y)
				{
					if (world.getBlockId(x,y,z) != Block.netherrack.blockID) continue;
					
					// and only under lava
					int under = world.getBlockId(x,y+1,z);
					boolean underLava = (under == Block.lavaStill.blockID || under == Block.lavaMoving.blockID);
					
					// based on this 3D density map
					float v = p.ValueAt(x * 0.312F, y * 0.312F, z * 0.312F) * 0.75F;
					v += p.ValueAt(x * 0.72F, y * 0.72F, z * 0.72F) * 0.25f;
					if (v > (underLava ? 0.4F : 0.8F))
					{
						world.setBlock(x, y, z, NegastoneOre_Nether.blockID);
					}
				}
			}
		}
	}
	
	public void modsLoaded()
	{
		ModLoader.addShapelessRecipe(new ItemStack(SuperconductorDust, 1),
			new Object[] { NegastoneDust, Items.getItem("goldDust").getItem() }
		);

		ModLoader.addRecipe(new ItemStack(Dialer, 1),
			new Object[] {
				"ESE", "SCS", "ESE",
				'E', Items.getItem("energyCrystal"),
				'S', SuperconductorIngot,
				'C', Items.getItem("electronicCircuit")
			}
		);
		
		ModLoader.addRecipe(new ItemStack(Gate, 1),
			new Object[] {
				"ISI", "SCS", "ISI",
				'I', Item.ingotIron,
				'S', SuperconductorIngot,
				'C', Items.getItem("electronicCircuit")
			}
		);
		
		Ic2Recipes.addMaceratorRecipe(new ItemStack(NegastoneOre_Nether), new ItemStack(NegastoneDust, 2));
		
		// 8x charcoal to get a coal dust
		Ic2Recipes.addMaceratorRecipe(new ItemStack(Item.coal, 8, 1), Items.getItem("coalDust"));
		// compress netherrack into netherbricks - 4 resulted in 3 being used
		Ic2Recipes.addCompressorRecipe(new ItemStack(Block.netherrack, 5), new ItemStack(Block.netherBrick, 1));
		
	}
	
	public String getVersion()
	{
		return "0.1";
	}
}