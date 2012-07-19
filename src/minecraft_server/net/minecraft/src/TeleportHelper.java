package net.minecraft.src;

import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.server.FMLServerHandler;

public class TeleportHelper
{
	public void dimensionalTeleportPlayer(EntityPlayer ent, int dim, double x, double y, double z)
	{
		EntityPlayerMP player = (EntityPlayerMP)ent;
		int currentDimension = player.dimension;
		
		MinecraftServer mcserver = FMLServerHandler.instance().getServer();
		
		if (currentDimension != dim)
		{
			player.dimension = dim;
			
			WorldServer wOld = mcserver.getWorldManager(currentDimension);
			WorldServer wNew = mcserver.getWorldManager(dim);
			
			player.playerNetServerHandler.sendPacket(
				new Packet9Respawn(dim, (byte)player.worldObj.difficultySetting, wNew.getWorldInfo().getTerrainType(), wNew.getHeight(), player.itemInWorldManager.getGameType())
			);
			
			wOld.removePlayer(player);
			player.isDead = false;
			
			mcserver.configManager.joinNewPlayerManager(player);
			player.setLocationAndAngles(x, y, z, player.rotationYaw, player.rotationPitch);
			player.setWorld(wNew);
			player.itemInWorldManager.setWorld(wNew);
			mcserver.configManager.updateTimeAndWeather(player, wNew);
			mcserver.configManager.func_30008_g(player); // ?
			
			FMLServerHandler.instance().announceDimensionChange(player);
		}
		else
		{
			player.setLocationAndAngles(x, y, z, player.rotationYaw, player.rotationPitch);
		}
		
		player.playerNetServerHandler.teleportTo(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
	}
}