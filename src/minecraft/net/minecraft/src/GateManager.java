/*package net.minecraft.src;
import java.util.*;

public class GateManager {
	
	private static Set<Gate> _gates = new HashSet<Gate>();
	
	private Gate findGateByAddress(String address){
		for(Gate gate : _gates){
			if(gate.getAddress().equals(address)){
				return gate;
			}
		}
		return null;
	}
	
	private void unregisterGate(Gate gate){
		//TODO: gate.ClosePortal();
	}

	
	
	public GateStatus dialGate(String addressFrom, String addressTo){
		Gate gateFrom = findGateByAddress(addressFrom);
		Gate gateTo   = findGateByAddress(addressTo);
		if(gateFrom == null) return GateStatus.NoSuchGate;
		if(!gateFrom.hasGateStructure()){
			unregisterGate(gateFrom);
			return GateStatus.NoRing;
		}
		
		if(gateTo == null) return GateStatus.NoSuchGate;
		if(!gateTo.hasGateStructure()){
			unregisterGate(gateTo);
			return GateStatus.NoSuchGate;
		}
		
		
		//if(!dialerFrom.hasFullGate()) return false;
		return GateStatus.NoSuchGate;
			
		//throw new java.lang.UnsupportedOperationException("not implemented");
	}
	
	
	public boolean contains(int[] t, int o)
	{
		for(int i = 0; i < t.length; ++i)
		{
			if (t[i] == o) return true;
		}
		return false;
	}
	
	public TileEntityDialer createGate(World w, int xBase, int zBase, int range, int... groundIds)
	{
		int x, y, z;
		
		for(y = w.getHeight() - 5; y > 0; --y)
		{
			for(x = xBase-range; x < xBase+range; ++x)
			{
				int count = 0;
				for(z = zBase-range; z < zBase+range; ++z)
				{
					// try to find five flat blocks in a row along z-axis
					if (w.getBlockId(x, y, z) == 0 && contains(groundIds, w.getBlockId(x, y-1, z))) ++count;
					else count = 0;
					if (count == 5)
					{
						for(int i = 0; i < 5; ++i)
						{
							for(int j = 0; j < 5; ++j)
							{
								w.setBlock(x, y + i, z - j,
									i == 0 && j == 2 ? mod_Endgame.Dialer.blockID :
									i == 0 || i == 4 || j == 0 || j == 4 ? mod_Endgame.Gate.blockID : 0
								);
							}
						}
						System.out.println("Spawned gate @ " + x + ", " + y + ", " + z);
						return (TileEntityDialer)w.getBlockTileEntity(x, y, z - 2);
					}
				}
			}
			
			for(z = zBase-range; z < zBase+range; ++z)
			{
				int count = 0;
				for(x = xBase-range; x < xBase+range; ++x)
				{
					if (w.getBlockId(x, y, z) == 0 && contains(groundIds, w.getBlockId(x, y-1, z))) ++count;
					else count = 0;
					if (count == 5)
					{
						for(int i = 0; i < 5; ++i)
						{
							for(int j = 0; j < 5; ++j)
							{
								w.setBlock(x - j, y + i, z,
									i == 0 && j == 2 ? mod_Endgame.Dialer.blockID :
									i == 0 || i == 4 || j == 0 || j == 4 ? mod_Endgame.Gate.blockID : 0
								);
							}
						}
						System.out.println("Spawned gate @ " + x + ", " + y + ", " + z);
						return (TileEntityDialer)w.getBlockTileEntity(x - 2, y, z);
					}
				}
			}
		}
		if (range < 256)
		{
			return createGate(w, xBase, zBase, range * 2, groundIds);
		}
		
		System.out.println("Couldn't find space for a gate near " + xBase + ", " + zBase);
		return null;
	}	
}*/