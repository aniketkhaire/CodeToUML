package myumlparser;

import java.util.ArrayList;
import java.util.List;

import japa.parser.ast.type.ClassOrInterfaceType;

public class ClassStore {
	String className;
	List<ClassOrInterfaceType> extendsList =new ArrayList<ClassOrInterfaceType>();
	List<ClassOrInterfaceType> implementsList = new ArrayList<ClassOrInterfaceType>();
	String myType;
	
	public ClassStore(String myType, String className, List<ClassOrInterfaceType> extendsList,	List<ClassOrInterfaceType> implementsList) {
		super();
		this.className = className;
		this.myType = myType;
		this.extendsList = extendsList;
		this.implementsList = implementsList;
	}
	//getters and setters
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMyType() {
		return myType;
	}
	public void setMyType(String myType) {
		this.myType = myType;
	}
	public List<ClassOrInterfaceType> getExtendsList() {
		return extendsList;
	}
	public void setExtendsList(List<ClassOrInterfaceType> extendsList) {
		this.extendsList = extendsList;
	}
	public List<ClassOrInterfaceType> getImplementsList() {
		return implementsList;
	}
	public void setImplementsList(List<ClassOrInterfaceType> implementsList) {
		this.implementsList = implementsList;
	}
}
