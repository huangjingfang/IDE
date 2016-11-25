package compiler;

import java.util.Vector;

public class registerAllocation {
	private String []register = { "t0","t1","t2","t3","t4","t5","t6","t7","t8","t9","s0","s1","s2","s3","s4","s5","s6","s7" };
	private boolean []judge;
	
	public registerAllocation()
	{
		judge = new boolean[register.length];
		for(int i = 0 ; i < register.length ; i++)
			judge[i] = false;
	}
	
	public void Allocation(Vector<String> code)
	{
		
	}
}
