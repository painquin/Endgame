package net.minecraft.src;


public class Gate {
	private String _address;
	private int _x;
	private int _y;
	private int _z;
	private GateStatus _gateStatus;
	public String getAddress(){ return _address;}
	
	// determines if the gate has a ring and a dialer in the proper configuration
	public boolean hasGateStructure(){ return false;}
}