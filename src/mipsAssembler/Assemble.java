package mipsAssembler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Assemble {
	public Assemble() {
		// TODO Auto-generated constructor stub
	}
	
	public void assemble(File f) throws Exception{
		InputStream ins = new FileInputStream(f);
		AssembleContext.parseContext(ins);
	}
}
