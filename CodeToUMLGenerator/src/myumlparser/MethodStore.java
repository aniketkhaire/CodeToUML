package myumlparser;

import java.util.ArrayList;
import java.util.List;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.type.Type;

public class MethodStore {
	String methodName;
	String className;
	int methodModifiers;
	List<Parameter> parameterList =new ArrayList<Parameter>();
	Type returnType;
	//List<TypeParameter> parameterTypeList = new ArrayList<TypeParameter>();
	
	public MethodStore(String methodName,String className, int methodModifiers, List<Parameter> parameterList, Type returnType) {
		super();
		this.methodName = methodName;
		this.className = className;
		this.methodModifiers = methodModifiers;
		this.parameterList = parameterList;
		this.returnType = returnType;
	}
	//getters and setters
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodModifiers() {
		switch(methodModifiers){
		case 0: return "~";
		case 1: return "+";
		case 2: return "-";
		case 4: return "#";
		}
		return "+";
	}
	public void setMethodModifiers(int methodModifiers) {
		this.methodModifiers = methodModifiers;
	}
	public String getParameterList() {
		//remove square braces
		if(this.parameterList != null){
		String temp = this.parameterList.toString();
		temp = temp.substring(1,temp.length()-1);
		return temp;
		}
		return "";
	}
	public List<Parameter> getMyParameterList(){
		return parameterList;
	}
	public void setParameterList(List<Parameter> parameterList) {
		this.parameterList = parameterList;
	}
	/*public List<TypeParameter> getParameterTypeList() {
		return parameterTypeList;
	}
	public void setParameterTypeList(List<TypeParameter> parameterTypeList) {
		this.parameterTypeList = parameterTypeList;
	}*/
	public Type getReturnType() {
		return returnType;
	}
	public void setReturnType(Type returnType) {
		this.returnType = returnType;
	}
}
