package net.minecraft.src;

import java.io.*;

public class TileEntityDialer extends TileEntity 
{	
	private String currentDisplay = "";
	private String Address = "";
	private String LastOutgoing = "";
	private String LastIncoming = "";
	
	private boolean Active = false;
	
	public TileEntityDialer()
	{
		Address = "12345";
	}
	
	public void setAddress(String address)
	{
		Address = address;
	}
	public String getAddress()
	{
		return Address;
	}
	
	public void setDisplay(String d)
	{
		currentDisplay = d;
	}
	
	public void Dial(int n)
	{
		if (worldObj.isRemote)
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			DataOutputStream data = new DataOutputStream(bytes);
			try
			{
				data.writeInt(xCoord);
				data.writeInt(yCoord);
				data.writeInt(zCoord);
				data.writeInt(n);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			
			Packet250CustomPayload p = new Packet250CustomPayload();
			p.channel = "endgame.dialer";
			p.data = bytes.toByteArray();
			p.length = p.data.length;
			ModLoader.sendPacket(p);
			return;
		}
		
		if (n == 10) // *
		{
			currentDisplay = "";
			return;
		}
		
		if (n == 11) // #
		{
			// behavior depends on input
			if (currentDisplay.equals("2")) setDisplay(Address);
			else if (currentDisplay.equals("3")) setDisplay(LastIncoming);
			else if (currentDisplay.equals("4")) setDisplay(LastOutgoing);
			else if (currentDisplay.equals("5"))
			{
				// activation test
				setDisplay("");
				int curMeta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
				worldObj.setBlockMetadata(xCoord, yCoord, zCoord, curMeta ^ 1);
				worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
				boolean a = (curMeta & 1) == 0;
				int removeId = (a ? mod_Endgame.Gate.blockID : mod_Endgame.GateActive.blockID);
				int addId = (a ? mod_Endgame.GateActive.blockID : mod_Endgame.Gate.blockID);
				if ((curMeta & 2) != 0)
				{
					for(int i = -2; i <= 2; ++i)
					{
						for(int j = 0; j < 5; ++j)
						{
							if (worldObj.getBlockId(xCoord, yCoord+j, zCoord+i) == removeId )
							{
								worldObj.setBlockAndMetadataWithNotify(xCoord, yCoord+j, zCoord+i, addId, 0);
							}
						}
					}
				}
				else
				{
					for(int i = -2; i <= 2; ++i)
					{
						for(int j = 0; j < 5; ++j)
						{
							if (worldObj.getBlockId(xCoord+i, yCoord+j, zCoord) == removeId )
							{
								worldObj.setBlockAndMetadataWithNotify(xCoord+i, yCoord+j, zCoord, addId, 0);
							}
						}
					}
				}
			}
			return;
		}
		
		currentDisplay += n;
	}
	
	public String getDisplay()
	{
		return currentDisplay;
	}
	
	/**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }
	
}