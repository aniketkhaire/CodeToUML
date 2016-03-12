package myumlparser;

import japa.parser.ast.type.Type;

public class VariableStore {
	String variableName;
	String className;
	Type variableType;
	int variableModifier;
	
	public VariableStore(String variableName, String className, Type variableType, int variableModifier) {
		super();
		this.variableName = variableName;
		this.className = className;
		this.variableType = variableType;
		this.variableModifier = variableModifier;
	}
	//getters and setters
	public String getVariableName() {
		//remove square braces
		String temp = this.variableName.substring(1,this.variableName.length() - 1);
		//extract first word only
		String[] returnVariableName=temp.split(" ",2);
		return returnVariableName[0];
	}
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Type getVariableType() {
		return variableType;
	}
	public void setVariableType(Type variableType) {
		this.variableType = variableType;
	}
	public String getVariableModifier() {
		switch(variableModifier){
		case 0: return "~";
		case 1: return "+";
		case 2: return "-";
		case 4: return "#";
		}
		return "+";
	}
	public void setVariableModifier(int variableModifier) {
		this.variableModifier = variableModifier;
	}
}
