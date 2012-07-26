package net.minecraft.src;
import java.util.*;

public class Gate {

	private static Map<String,Gate> _gates = new HashMap<String,Gate>();
	//TODO: initialize the nether gate in a static constructor or something;
	
	
	public static Gate find(String address){
		return _gates.get(address);
	}

	public static Gate createGate(int dimension, int x, int y, int z){
		Gate g = new Gate(randomAddress(), dimension, x, y, z);
		_gates.put(g.getAddress(),g);
		return g;
	}

	private Gate(String address, int dimension, int x, int y, int z){
		_address = address;
		_dimension = dimension;
		_chunkCoordinates = new ChunkCoordinates(x,y,z);
		_lastIncoming = "12345";
		_lastOutgoing = address;
	}
	


	private static String randomAddress() {
		Random r = new Random();
		String addr = "";
		do {
			addr = r.nextInt(90000) + 10000 + "";
		}
		while(!_gates.containsKey(addr));
		
		return addr;
	}
	
	private String _address;
	private String _lastIncoming;
	private String _lastOutgoing;
	
	private ChunkCoordinates _chunkCoordinates;
	private int _dimension;
	private GateStatus _gateStatus;
	
	
	public String getAddress(){ return _address;}
	public ChunkCoordinates getChunkCoordinates(){ return _chunkCoordinates;}

	public int getDimension(){ return _dimension;}
	public String getLastIncoming(){ return _lastIncoming;}
	public String getLastOutgoing(){ return _lastOutgoing;}
	public GateStatus getGateStatus(){ return _gateStatus;}
	
	// // determines if the gate has a ring and a dialer in the proper configuration
	// // TODO: once a gate structure is found, cache the positions of the blocks to make future
	// //       checks easier
	// public boolean hasGateStructure(/*World w*/){ return false;}
	
	public void unregister(){
		// TODO: if this gate has an active incoming portal, close it or something
		_gates.remove(_address);
	}
	
	public GateDialStatus tryToDial(String address){
		if(address.equals(_address))
			return GateDialStatus.AddressIsSelf;
		Gate target = find(address);
		if(target == null)
			return GateDialStatus.NoSuchGate;
		return GateDialStatus.Success;
	}
}