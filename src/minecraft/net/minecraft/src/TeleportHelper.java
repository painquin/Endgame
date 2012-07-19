package net.minecraft.src;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.ReflectionHelper;
import java.lang.reflect.*;

public class TeleportHelper
{
	public void dimensionalTeleportPlayer(EntityPlayer ent, int dim, double x, double y, double z)
	{
		// ent is ignored on this side.
		Minecraft MC = null;
		try
        {
            Field f = Class.forName("net.minecraft.client.Minecraft").getDeclaredField("theMinecraft");
            f.setAccessible(true);
            MC = (Minecraft)f.get(null);
        }
        catch(Exception e)
		{
			System.out.println(e);
			return;
		}
		
		EntityPlayerSP thePlayer = MC.thePlayer;
		World theWorld = MC.theWorld;
		
		int currentDimension = thePlayer.dimension;
		
		if (currentDimension != dim)
		{
			thePlayer.dimension = dim;
			
			theWorld.setEntityDead(thePlayer);
			thePlayer.isDead = false;
			
			WorldProvider pNew = WorldProvider.getProviderForDimension(dim);
			WorldProvider pOld = WorldProvider.getProviderForDimension(currentDimension);
			
			World var9 = new World(theWorld, pNew);
			
			if (thePlayer.isEntityAlive())
			{
				theWorld.updateEntityWithOptionalForce(thePlayer, false);
			}
			
			MC.changeWorld(var9, "Traversing the wormhole...", thePlayer);
			
			theWorld = MC.theWorld;
			
			thePlayer.worldObj = theWorld;
			System.out.println("Teleported to " + theWorld.worldProvider.worldType);
		}
		
		if (thePlayer.isEntityAlive())
		{
			thePlayer.setLocationAndAngles(x, y, z, thePlayer.rotationYaw, thePlayer.rotationPitch);
			theWorld.updateEntityWithOptionalForce(thePlayer, false);
		}
	}
}