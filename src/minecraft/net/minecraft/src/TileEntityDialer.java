package net.minecraft.src;

import java.io.*;

public class TileEntityDialer extends TileEntity {	
	private String _currentDisplay = "";
	private Gate _gate;
	
	private boolean Active = false;
	
	public TileEntityDialer(){

	}
	
	public void addedToWorld(int dimension, int x, int y, int z){
		_gate = Gate.createGate(dimension, x, y, z);
	}
	
	public void removedFromWorld(){
		_gate.unregister();
	}

	public void setDisplay(String d){
	
		_currentDisplay = d;
	}
	
	public void Dial(int n){
	
		if (worldObj.isRemote){
		
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			DataOutputStream data = new DataOutputStream(bytes);
			try{
			
				data.writeInt(xCoord);
				data.writeInt(yCoord);
				data.writeInt(zCoord);
				data.writeInt(n);
			}
			catch(IOException e){
			
				e.printStackTrace();
			}
			
			Packet250CustomPayload p = new Packet250CustomPayload();
			p.channel = "endgame.dialer";
			p.data = bytes.toByteArray();
			p.length = p.data.length;
			ModLoader.sendPacket(p);
			return;
		}
		
		if (n == 10){ // *
		
			_currentDisplay = "";
			return;
		}
		
		if (n == 11){ // #
		
			if(_currentDisplay.length() == 5){
				GateDialStatus status = _gate.tryToDial(_currentDisplay);
				if(status == GateDialStatus.NoSuchGate) setDisplay("Invalid Gate Address");
					
			}
			
			// behavior depends on input
			if (_currentDisplay.equals("2")) setDisplay(_gate.getAddress());
			else if (_currentDisplay.equals("3")) setDisplay(_gate.getLastIncoming());
			else if (_currentDisplay.equals("4")) setDisplay(_gate.getLastOutgoing());
			else if (_currentDisplay.equals("5")){ // TODO: make this go away?
			
				// activation test
				setDisplay("");
				int curMeta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
				worldObj.setBlockMetadata(xCoord, yCoord, zCoord, curMeta ^ 1);
				worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
				boolean a = (curMeta & 1) == 0;
				int removeId = (a ? mod_Endgame.Gate.blockID : mod_Endgame.GateActive.blockID);
				int addId = (a ? mod_Endgame.GateActive.blockID : mod_Endgame.Gate.blockID);
				if ((curMeta & 2) != 0){
					for(int i = -2; i <= 2; ++i){
						for(int j = 0; j < 5; ++j){
							if (worldObj.getBlockId(xCoord, yCoord+j, zCoord+i) == removeId ){
								worldObj.setBlockAndMetadataWithNotify(xCoord, yCoord+j, zCoord+i, addId, 0);
							}
						}
					}
				}
				else{
					for(int i = -2; i <= 2; ++i){
						for(int j = 0; j < 5; ++j){
							if (worldObj.getBlockId(xCoord+i, yCoord+j, zCoord) == removeId ){
								worldObj.setBlockAndMetadataWithNotify(xCoord+i, yCoord+j, zCoord, addId, 0);
							}
						}
					}
				}
			}
			return;
		}
		
		_currentDisplay += n;
	}
	
	public String getDisplay() {
		return _currentDisplay;
	}
	
	/**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }
	
	
	
	
	
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		_gate = Gate.find(tag.getString("address"));	
	}
	
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setString("address", _gate.getAddress());
	}
}