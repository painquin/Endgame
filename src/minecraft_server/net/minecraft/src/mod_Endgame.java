package net.minecraft.src;

import net.minecraft.src.ic2.api.*;
import net.minecraft.src.forge.*;
import java.util.Random;
import java.lang.reflect.*;
import java.io.*;

public class mod_Endgame extends NetworkMod implements IGuiHandler, IConnectionHandler, IPacketHandler
{
	public static mod_Endgame Instance;

	public static BlockEndgameOre ModOre;
	public static BlockGate Gate;
	public static BlockGate GateActive;
	
	public static BlockDialer Dialer;
	
	public static ItemEndgame ModItems;
	
	protected ItemStack copperOre = null;
	protected ItemStack tinOre = null;
	protected ItemStack uraniumOre = null;
	
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
		Instance = this;
	}
	
	public void load()
	{
	
		MinecraftForge.registerConnectionHandler(this);
		
		MinecraftForge.setGuiHandler(this, this);
	
		DisableNetherPortals();
		
		ModItems = new ItemEndgame(20000);
		ModItems.setItemName("endgame.items");
		ModLoader.addName(ModItems, "Mod Item");
		
		//todo: combine into a single item, use meta info to distinguish
		// NegastoneDust = new Item(20000).setItemName("negastone.dust");
		// NegastoneDust.iconIndex = ModLoader.addOverride("/gui/items.png", "Endgame/dust.png");
		// ModLoader.addName(NegastoneDust, "Negastone Dust");
		
		// SuperconductorDust = new Item(20001).setItemName("superconductor.dust");
		// SuperconductorDust.iconIndex = ModLoader.addOverride("/gui/items.png", "Endgame/sdust.png");
		// ModLoader.addName(SuperconductorDust, "Superconductor Dust");
		
		// SuperconductorIngot = new Item(20002).setItemName("superconductor.ingot");
		// SuperconductorIngot.iconIndex = ModLoader.addOverride("/gui/items.png", "Endgame/ingot.png");
		// ModLoader.addName(SuperconductorIngot, "Superconductor Ingot");
		
		// ModLoader.addSmelting(SuperconductorDust.shiftedIndex, new ItemStack(SuperconductorIngot, 1));
		
		
		Gate = new BlockGate(182);
		Gate.setBlockName("endgame.gate")
			.setHardness(3F)
			.setResistance(25f)
			.setResistance(6000000.0F);
		
		ModLoader.addName(Gate, "Gate");
		ModLoader.registerBlock(Gate);
		
		GateActive = new BlockGate(181)
		{
			public int getBlockTextureFromSideAndMetadata(int side, int metadata)
			{
				return super.getBlockTextureFromSideAndMetadata(side, metadata)+1;
			}
		};
		
		GateActive.setBlockName("endgame.gate.active")
			.setBlockUnbreakable()
			.setResistance(6000000.0F);
		
		ModLoader.registerBlock(GateActive);
		
		Dialer = new BlockDialer(183);
		
		Dialer.setBlockName("endgame.dialer")
			.setHardness(3F)
			.setResistance(6000000.0F);
			
		ModLoader.addName(Dialer, "Dialer");
		ModLoader.registerBlock(Dialer);
		
		
		// todo: combine into a single block, use meta info to distinguish
		ModOre = new BlockEndgameOre(180);
		ModOre.setBlockName("endgame.ore");
		ModLoader.registerBlock(ModOre);
		ModLoader.addName(ModOre, "Mod Ore");
		
		MinecraftForge.setBlockHarvestLevel(ModOre, "pickaxe", 2);
		
		ModLoader.registerTileEntity(TileEntityDialer.class, "Dialer");
		
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
	Perlin mAlpha = new Perlin(7); // mountain alpha bonus
	Perlin mBeta = new Perlin(9); // mountain beta bonus
	
	
	public void generateSurface(World world, Random rand, int chunkX, int chunkZ)
	{
		int lavaId = Block.lavaStill.blockID;
		
		int maxHeight = world.getHeight();
		for(int x = chunkX; x < chunkX+16; ++x)
		{
			for(int z = chunkZ; z < chunkZ+16; ++z)
			{
				int skyDepth = 0;
				boolean sky = true;
				
				for(int y = maxHeight - 1; y > 0; --y)
				{
					int blockId = world.getBlockId(x, y, z);
					
					if (sky)
					{
						if (blockId == 0)
						{
							++skyDepth;
						}
						else
						{
							sky = false;
						}
					}
					
					// we only replace smoothstone with valuables
					if (blockId != Block.stone.blockID) continue;
					
					// negastone only under lava
					int under = world.getBlockId(x,y+1,z);
					float v;
					if (under == Block.lavaStill.blockID || under == Block.lavaMoving.blockID)
					{
						// based on this 3D density map
						v = p.ValueAt(x * 0.312F, y * 0.312F, z * 0.312F) * 0.75F;
						v += p.ValueAt(x * 0.72F, y * 0.72F, z * 0.72F) * 0.25f;
						if (v > 0.6F)
						{
							world.setBlockAndMetadata(x, y, z, ModOre.blockID, 0);
							continue;
						}
					}
					
					
					float bonus = (float)maxHeight / skyDepth;
					
					v = mAlpha.ValueAt(x * 0.53F, y * 0.73F, z * 0.53F) * 0.75F;
					v += mBeta.ValueAt(x * 0.72F, y * 0.92F, z * 0.72F) * 0.3F;
					
					if (v * bonus > 1.2F)
					{
						world.setBlock(x, y, z, Block.oreIron.blockID);
						continue;
					}


					if (copperOre != null)
					{
						v = mAlpha.ValueAt(x * 0.53F, y * -0.73F, z * 0.53F) * 0.75F;
						v += mAlpha.ValueAt(x * -0.72F, y * -0.92F, z * -0.72F) * 0.3F;
						
						if (v * bonus > 1.3F)
						{
							world.setBlock(x, y, z, copperOre.itemID);
							continue;
						}
					}
					
					
					if (tinOre != null)
					{
						v = mAlpha.ValueAt(x * -0.53F, y * -0.73F, z * 0.53F) * 0.75F;
						v += mAlpha.ValueAt(x * 0.72F, y * -0.92F, z * -0.72F) * 0.3F;
						
						if (v * bonus > 1.3F)
						{
							world.setBlock(x, y, z, tinOre.itemID);
							continue;
						}
					}
					
					v = mBeta.ValueAt(x * 0.73F, y * 0.93F, z * 0.73F) * 0.75F;
					v += mAlpha.ValueAt(x * 0.92F, y * 1.02F, z * 0.92F) * 0.3F;
					
					if (v * bonus > 1.4F)
					{
						world.setBlock(x, y, z, Block.oreGold.blockID);
						continue;
					}
					
					
					
					
					if (uraniumOre != null)
					{
						v = mAlpha.ValueAt(x * -0.63F, y * -0.73F, z * 0.63F) * 0.75F;
						v += mAlpha.ValueAt(x * 0.82F, y * -0.92F, z * -0.82F) * 0.3F;
						
						if (v * bonus > 1.5F)
						{
							world.setBlock(x, y, z, uraniumOre.itemID);
							continue;
						}
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
						world.setBlockAndMetadata(x, y, z, ModOre.blockID, 1);
					}
				}
			}
		}
	}
	
	public void modsLoaded()
	{
		copperOre = Items.getItem("copperOre");
		tinOre = Items.getItem("tinOre");
		uraniumOre = Items.getItem("uraniumOre");
		
		ModLoader.addShapelessRecipe(new ItemStack(ModItems, 1, 1),
			new ItemStack(ModItems, 1, 0), Items.getItem("goldDust").getItem()
		);

		int energyCrystalId = ((IElectricItem)Items.getItem("energyCrystal").getItem()).getEmptyItemId();
		
		ModLoader.addRecipe(new ItemStack(Dialer, 1),
			"ESE", "SCS", "ESE",
			'E', new ItemStack(energyCrystalId, 1, 26),
			'S', new ItemStack(ModItems, 1, 2),
			'C', Items.getItem("advancedCircuit")
		);
		
		ModLoader.addRecipe(new ItemStack(Gate, 1),
			new Object[] {
				"ISI", "SCS", "ISI",
				'I', Item.ingotIron,
				'S', new ItemStack(ModItems, 1, 2),
				'C', Items.getItem("electronicCircuit")
			}
		);
				
		// 8x charcoal to get a coal dust
		Ic2Recipes.addMaceratorRecipe(new ItemStack(Item.coal, 8, 1), Items.getItem("coalDust"));
		// compress netherrack into netherbricks - 4 resulted in 3 being used
		Ic2Recipes.addCompressorRecipe(new ItemStack(Block.netherrack, 5), new ItemStack(Block.netherBrick, 1));
		
		FurnaceRecipes.smelting().addSmelting(ModItems.shiftedIndex, 1, new ItemStack(ModItems.shiftedIndex, 1, 2));
	}
	
	public String getVersion()
	{
		return "0.1";
	}

	
	public Object getGuiElement(int Id, EntityPlayer player, World world, int x, int y, int z)
	{
		switch(Id)
		{
			case 1: return new ContainerDialer(	player.inventory, (TileEntityDialer)world.getBlockTileEntity(x, y, z));
		}
		return null;
	}
	
	@Override
	public void onConnect(NetworkManager network)
	{
	}

	@Override
	public void onLogin(NetworkManager network, Packet1Login login)
	{
			MessageManager.getInstance().registerChannel(network, this, "endgame.dialer");
	}

	@Override
	public void onDisconnect(NetworkManager network, String message, Object[] args)
	{
	}

	@Override
	public void onPacketData(NetworkManager network, String channel, byte[] data)
	{
		if (channel.equals("endgame.dialer"))
		{
			EntityPlayer player = ((NetServerHandler)network.getNetHandler()).getPlayerEntity();
			
			int x, y, z, n;
			DataInputStream ds = new DataInputStream(new ByteArrayInputStream(data));
			try
			{
				x = ds.readInt();
				y = ds.readInt();
				z = ds.readInt();
				n = ds.readInt();
				
				TileEntity ent = player.worldObj.getBlockTileEntity(x, y, z);
				if (ent == null || !(ent instanceof TileEntityDialer)) return;
				
				((TileEntityDialer)ent).Dial(n);
				
				// String d = ((TileEntityDialer)ent).getDisplay();
				
				// Packet250CustomPayload packet = new Packet250CustomPayload();
				
				// ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                // DataOutputStream dataout = new DataOutputStream(bytes);
                // try
                // {
					// dataout.writeInt(
					// dataout.writeInt(x);
					// dataout.writeInt(y);
					// dataout.writeInt(z);
					// dataout.writeString(d);
                // }
                // catch(IOException e)
                // {
					// e.printStackTrace();
                // }
                // packet.channel = "endgame.dialer";
                // packet.data = bytes.toByteArray();
                // packet.length = packet.data.length;
				
				// ModLoader.getMinecraftServerInstance().configManager.sendPacketToAllPlayers(packet);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}