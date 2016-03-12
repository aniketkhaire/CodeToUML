package myumlparser;

public class DependencyStore {
	String classOne;
	String classTwo;
	String multiplicityOne;
	String multiplicityTwo;
	String typeOne;
	String typeTwo;
	public DependencyStore(String typeOne, String classOne,String typeTwo, String classTwo, String multiplicityOne, String multiplicityTwo) {
		super();
		this.classOne = classOne;
		this.classTwo = classTwo;
		this.typeOne = typeOne;
		this.typeTwo = typeTwo;
		this.multiplicityOne = multiplicityOne;
		this.multiplicityTwo = multiplicityTwo;
	}
	public String getTypeOne() {
		return typeOne;
	}
	public void setTypeOne(String typeOne) {
		this.typeOne = typeOne;
	}
	public String getTypeTwo() {
		return typeTwo;
	}
	public void setTypeTwo(String typeTwo) {
		this.typeTwo = typeTwo;
	}
	public String getClassOne() {
		return classOne;
	}
	public void setClassOne(String classOne) {
		this.classOne = classOne;
	}
	public String getClassTwo() {
		return classTwo;
	}
	public void setClassTwo(String classTwo) {
		this.classTwo = classTwo;
	}
	public String getMultiplicityOne() {
		return multiplicityOne;
	}
	public void setMultiplicityOne(String multiplicityOne) {
		this.multiplicityOne = multiplicityOne;
	}
	public String getMultiplicityTwo() {
		return multiplicityTwo;
	}
	public void setMultiplicityTwo(String multiplicityTwo) {
		this.multiplicityTwo = multiplicityTwo;
	}
	
}
