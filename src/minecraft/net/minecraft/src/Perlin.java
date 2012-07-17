package net.minecraft.src;

import java.util.*;

public class Perlin
{
	public final int Size = 12;
	
	protected float[] values;
	
	public Perlin(long seed)
	{
		values = new float[Size*Size*Size];
		Random r = new Random(seed);
		for(int x = 0; x < Size; ++x)
		{
			for(int y = 0; y < Size; ++y)
			{
				for(int z = 0; z < Size; ++z)
				{
					values[x + Size * y + Size*Size * z] = r.nextFloat();
				}
			}
		}
	}
	// todo: there's got to be a better way
	int safe(int i)
	{
		while(i < 0)
		{
			i += Size;
		}
		return i % Size;
	}
	
	public float ValueAt(int x, int y, int z)
	{
		return values[ safe(x) + Size*safe(y) + Size*Size*safe(z) ];
	}
	
	public float lerp(float a, float b, float t)
	{
		return a + t * (b - a);
	}
	
	public float ValueAt(float x, float y, float z)
	{
		int x1, x2, y1, y2, z1, z2;
		float tx, ty, tz;
		
		x1 = (int)Math.floor(x);
		x2 = (int)Math.ceil(x) + 1;
		tx = x - x1;
		
		y1 = (int)Math.floor(y);
		y2 = (int)Math.ceil(y);
		ty = y - y1;
		
		z1 = (int)Math.floor(z);
		z2 = (int)Math.ceil(z);
		tz = z - z1;
				
		return lerp(
			lerp(
				lerp(ValueAt(x1, y1, z1), ValueAt(x2, y1, z1), tx),
				lerp(ValueAt(x1, y1, z2), ValueAt(x2, y1, z2), tx),
				tz
			),
			lerp(
				lerp(ValueAt(x1, y2, z1), ValueAt(x2, y2, z1), tx),
				lerp(ValueAt(x1, y2, z2), ValueAt(x2, y2, z2), tx),
				tz
			),
			ty
		);
		
	}
}