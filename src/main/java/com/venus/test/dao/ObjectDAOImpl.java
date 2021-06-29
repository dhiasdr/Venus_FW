package com.venus.test.dao;

public class ObjectDAOImpl implements IObjectDAO {
	// IClassA classA;
	private int a;
	private ClassC classC;

	@Override
	public void work() {
		System.out.println("work "+a + " //ClassC: "+classC.getB());
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public ClassC getClassC() {
		return classC;
	}

	public void setClassC(ClassC classC) {
		this.classC = classC;
	}

	@Override
    public String aspectTest() {
    	System.out.println("for testing aspect!");
    	throw new NullPointerException("a not found");
    	//return "// Aspect Test method //";
    }

	/*
	 * public void setClassA(IClassA classA) { this.classA = classA; } public
	 * IClassA getClassA() { return classA; }
	 */

}
