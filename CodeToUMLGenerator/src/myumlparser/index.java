package myumlparser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.Type;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

public class index {
	public static List<ClassStore> classList =new ArrayList<ClassStore>();
	public static List<VariableStore> variableList =new ArrayList<VariableStore>();
	public static List<MethodStore> methodList =new ArrayList<MethodStore>();
	public static List<interfaceStore> interfaceList =new ArrayList<interfaceStore>();
	public static List<DependencyStore> dependencyList = new ArrayList<DependencyStore>();
	public static List<usesStore> usesList = new ArrayList<usesStore>();
	public static String grammar, className, dependencyGrammar;
	
	public static void main(String[] args) throws ParseException, IOException {
		//this module will be responsible to interact with the user
		String inputPath = args[0];
		String outputPath = args[1];
		
		//fetch all files that end with .java 
    	FilenameFilter filter = new FilenameFilter() {
    			public boolean accept(File dir, String name) {
    	        return name.endsWith(".java");
    	    }    		
    	};
    	
    	//provide path of the source folder
    	//File folder = new File("F://Lectures//202-Paul//Eclipse//myumlparser//src//myumlparser");
    	File folder = new File(inputPath);    	
    	File[] listOfFiles = folder.listFiles(filter);
    	
    	//traverse the folder
    	for (int i = 0; i < listOfFiles.length; i++) {
    	    File file = listOfFiles[i];
    	    //create an object of Compilation Unit
    	    CompilationUnit cu;
            // parse the file
            cu = JavaParser.parse(file);
            //fetch all classes
            new ClassOrInterfaceVisitor().visit(cu, null);                       
    	}
    	
    	//write grammar
    	dependencyGrammar = "";
        grammar = "@startuml\nskinparam classAttributeIconSize 0\n";
        
        generateGrammar();
        
        grammar += dependencyGrammar+ "@enduml\n";
        System.out.println(grammar);
        
        //Save the output as image
        StringBuilder plantUmlSource = new StringBuilder();
        plantUmlSource.append(grammar);
        SourceStringReader reader = new SourceStringReader(plantUmlSource.toString());
        FileOutputStream output = new FileOutputStream(new File(outputPath+"/diagram.png"));
        reader.generateImage(output, new FileFormatOption(FileFormat.PNG, false));    
    }//end of main()
	
	public static void generateGrammar(){
		for(int i=0; i<classList.size(); i++){
			ClassStore tempClass = classList.get(i);
			
			//append the class name
			grammar +=tempClass.getMyType()+tempClass.getClassName()+"{\n";
			
			//append the attributes
			for(int j=0; j<variableList.size();j++){
				VariableStore tempVar = variableList.get(j);
				String tempType;
				boolean isCollection = false;
				
				//System.out.println(tempVar.getClassName()+" == "+tempClass.getClassName());
				if(tempVar.getClassName() == tempClass.getClassName()){
					//agar variable is of same class
					
					//check whether the variable is a collection or not
					if(tempVar.getVariableType().toString().contains("<")){			
						//if Collection, extract class name
						int startIndex,endIndex;
						startIndex = tempVar.getVariableType().toString().indexOf("<");
						endIndex = tempVar.getVariableType().toString().indexOf(">");
						tempType = tempVar.getVariableType().toString().substring(startIndex+1, endIndex);
						isCollection = true;
					}
					else if(tempVar.getVariableType().toString().contains("[")){			
						//if Array, extract class name
						int startIndex,endIndex;
						startIndex = tempVar.getVariableType().toString().indexOf("[");
						endIndex = tempVar.getVariableType().toString().indexOf("]");
						tempType = tempVar.getVariableType().toString().substring(0, startIndex);
						isCollection = true;
					}
					else{
						tempType = tempVar.getVariableType().toString();
					}
					
					//check for getter/setters
					
					//check whether the method is getter or setter
					boolean isGetterSetter = false;
					
					for(int jo=0; jo<methodList.size();jo++){
						
						//extract getMethodName and setMethodName
						MethodStore tempMethod = methodList.get(jo);
						
						if(tempMethod.getClassName() == tempClass.getClassName()){
							
							//replacing the first character by uppercase 
							String tempStr = tempVar.getVariableName().substring(0,1).toUpperCase() + tempVar.getVariableName().substring(1).toLowerCase();
							String getMethod, setMethod;
							//create string  "get + variable_name" and "set + variable_name"
							getMethod = "get"+tempStr;
							setMethod = "set"+tempStr;
							//System.out.println(tempMethod.getMethodName()+" ==== "+getMethod);
							if(tempMethod.getMethodName().equals(setMethod) || tempMethod.getMethodName().equals(getMethod)){
								//System.out.println(tempMethod.getMethodName()+" == "+getMethod);
								tempVar.setVariableModifier(1);
								variableList.set(j, tempVar);
							}
						}
					}				
					
					//check for dependency
					boolean dependencyPresent = false;
					for(int myItr=0;myItr <classList.size();myItr++)
					{
						if(tempType.equals(classList.get(myItr).getClassName()))
						{
							dependencyPresent = true;
							//check whether the pair already exists in dependencyList
							boolean present =false;
							for(int y=0;y<dependencyList.size();y++)
							{
								if((tempType.equals(dependencyList.get(y).getClassOne()) && tempClass.getClassName().equals(dependencyList.get(y).getClassTwo())) ||(tempClass.getClassName().equals(dependencyList.get(y).getClassOne()) && tempType.equals(dependencyList.get(y).getClassTwo())))
								{
									present = true;
									if(tempClass.getClassName().equals(dependencyList.get(y).getClassOne())){
										if(dependencyList.get(y).getMultiplicityTwo() =="")
										{
											if(isCollection)
												dependencyList.get(y).setMultiplicityTwo("*");
											else
												dependencyList.get(y).setMultiplicityTwo("1");
										}
										else
										{
											dependencyList.get(y).setMultiplicityTwo("*");
										}
									}else{
										if(dependencyList.get(y).getMultiplicityOne() =="")
										{
											if(isCollection)
												dependencyList.get(y).setMultiplicityOne("*");
											else
												dependencyList.get(y).setMultiplicityOne("1");
										}
										else
										{
											dependencyList.get(y).setMultiplicityOne("*");
										}
									}
								}
							}
							if(! present)
							{
								if(isCollection){
									dependencyList.add(new DependencyStore(tempClass.getMyType(), tempClass.getClassName(), classList.get(myItr).getMyType(), tempType, "", "*"));
									//System.out.println("Added: "+tempClass.getClassName()+" : "+ tempType+ ": 1"+ ": *");
								}
								else{
									dependencyList.add(new DependencyStore(tempClass.getMyType(), tempClass.getClassName(), classList.get(myItr).getMyType(), tempType, "", "1"));
									//System.out.println("Added: "+tempClass.getClassName()+" : "+ tempType+ ": 1"+ ": 1");
								}
							}							
						}
					}
					if(! dependencyPresent)
					{
						if((tempVar.getVariableModifier() == "+") || (tempVar.getVariableModifier() == "-"))
							grammar+=tempVar.getVariableModifier()+" "+tempVar.getVariableName()+" : "+tempVar.getVariableType()+"\n";
					}
				}
			}
			
			//append the methods
			for(int k=0; k<methodList.size();k++){
				MethodStore tempMethod = methodList.get(k);
				
				if(tempMethod.getClassName() == tempClass.getClassName())
				{					
					//check for uses attribute
					//System.out.println(tempMethod.getMyParameterList());
					if(tempMethod.getMyParameterList() != null){
					for(int h=0;h<tempMethod.getMyParameterList().size();h++)
					{
						for(int s=0;s<classList.size();s++)
						{
							if(classList.get(s).getClassName().equals(tempMethod.getMyParameterList().get(h).getType().toString()))
							{
								//System.out.println(tempClass.getClassName()+" != "+classList.get(s).getClassName());
								//System.out.println(tempClass.getMyType()+" != "+classList.get(s).getMyType());
								if((tempClass.getMyType() == "interface " && classList.get(s).getMyType() == "interface ") || (tempClass.getMyType() == "class " && classList.get(s).getMyType() == "class ")){
									//System.out.println(tempClass.getClassName()+" == "+classList.get(s).getClassName());
								}else
									usesList.add(new usesStore(tempClass.getClassName(), classList.get(s).getClassName()));
							}
						}
					}					
					}
					//check whether the method is getter or setter
					boolean isGetterSetter = false;
					
					for(int j=0; j<variableList.size();j++){
						
						//extract getMethodName and setMethodName
						VariableStore tempVar = variableList.get(j);
						
						if(tempVar.getClassName() == tempClass.getClassName()){
							
							//replacing the first character by uppercase 
							String tempStr = tempVar.getVariableName().substring(0,1).toUpperCase() + tempVar.getVariableName().substring(1).toLowerCase();
							String getMethod, setMethod;
							//create string  "get + variable_name" and "set + variable_name"
							getMethod = "get"+tempStr;
							setMethod = "set"+tempStr;
							//System.out.println(setMethod);
							if(tempMethod.getMethodName().equals(setMethod) || tempMethod.getMethodName().equals(getMethod)){
								isGetterSetter = true;
								break;
							}
						}
					}
					if(! isGetterSetter){
						if(tempMethod.getMethodModifiers() == "+"){
							if(tempMethod.getReturnType() !=null)
								grammar+=tempMethod.getMethodModifiers()+" "+tempMethod.getMethodName()+"("+tempMethod.getParameterList()+") : "+tempMethod.getReturnType()+"\n";
							else
								grammar+=tempMethod.getMethodModifiers()+" "+tempMethod.getMethodName()+"("+tempMethod.getParameterList()+")\n";
						}
					}
				}
			}
			grammar +="}\n";
			
			//append extended classes
			//System.out.println("Extends List : "+tempClass.getExtendsList());
			if (tempClass.getExtendsList() != null){
				for(int itr=0; itr< tempClass.getExtendsList().size();itr++){
					grammar += "class "+tempClass.getClassName()+" extends "+tempClass.getExtendsList().get(itr)+"\n";
				}
			}
			
			//append implemented classes
			if (tempClass.getImplementsList() != null){
				for(int itr=0; itr< tempClass.getImplementsList().size();itr++){
					//grammar += "class "+tempClass.getClassName()+" implements "+tempClass.getImplementsList().get(itr)+"\n";
					grammar += "class "+tempClass.getClassName()+" implements "+tempClass.getImplementsList().get(itr)+"\n";
				}
			}
		}
		
		//print association
		for(int z=0;z<dependencyList.size();z++)
		{
			if(dependencyList.get(z).getMultiplicityOne() == "")
			{
				dependencyGrammar += dependencyList.get(z).getTypeOne()+dependencyList.get(z).getClassOne()+" -- \""+dependencyList.get(z).getMultiplicityTwo()+"\" "+dependencyList.get(z).getTypeTwo()+dependencyList.get(z).getClassTwo()+"\n";
			}else if(dependencyList.get(z).getMultiplicityTwo() == "")
			{
				dependencyGrammar += dependencyList.get(z).getTypeOne()+dependencyList.get(z).getClassOne()+" \""+dependencyList.get(z).getMultiplicityOne()+"\" -- "+dependencyList.get(z).getTypeTwo()+dependencyList.get(z).getClassTwo()+"\n";
			}
			else
				dependencyGrammar += dependencyList.get(z).getTypeOne()+dependencyList.get(z).getClassOne()+" \""+dependencyList.get(z).getMultiplicityOne()+"\" -- \""+dependencyList.get(z).getMultiplicityTwo()+"\" "+dependencyList.get(z).getTypeTwo()+dependencyList.get(z).getClassTwo()+"\n";
		}
		
		//print uses relationship
		//System.out.println(usesList.size());
		for(int z=0;z<usesList.size();z++)
		{
			/*boolean present = false;
			for(int a=0;a<dependencyList.size();a++)
			{
				if((usesList.get(z).getClassOne().equals(dependencyList.get(a).getClassOne()) &&
						usesList.get(z).getClassTwo().equals(dependencyList.get(a).getClassTwo())) || 
						(usesList.get(z).getClassOne().equals(dependencyList.get(a).getClassTwo()) &&
								usesList.get(z).getClassTwo().equals(dependencyList.get(a).getClassOne())))
				{
					present = true;
				}
			}*/
			for(int a=0;a<usesList.size();a++)
			{
				if((usesList.get(z).getClassOne().equals(usesList.get(a).getClassOne()) &&
						usesList.get(z).getClassTwo().equals(usesList.get(a).getClassTwo())))
				{
					if(z != a)
					{
						usesList.remove(a);
					}
				}
			}
			//if(!present)
				dependencyGrammar += usesList.get(z).getClassOne()+" \"uses\" ..> "+usesList.get(z).getClassTwo()+"\n";
		}
	}
		
    private static class ClassOrInterfaceVisitor extends VoidVisitorAdapter {
        @Override
        public void visit(ClassOrInterfaceDeclaration cn, Object arg) {
        	String type;
            if(cn.isInterface()){
            	interfaceList.add(new interfaceStore(cn.getName()));
            	classList.add(new ClassStore("interface ", cn.getName(),null ,null));
            	type = "interface";
            }else{
            	classList.add(new ClassStore("class ", cn.getName(), cn.getExtends(), cn.getImplements()));
            	type = "class";
            }
            //System.out.println("Class "+tempClass.getClassName() + " extends " + tempClass.getExtendsList() +" implements  "+ tempClass.getImplementsList()+ "{");
            className = cn.getName();
            type += cn.getMembers();
            type = Pattern.compile("(?<=\\{).*?(?=\\})", Pattern.DOTALL).matcher(type).replaceAll("");
            type = Pattern.compile("(?<=\\{).*?(?=\\};)", Pattern.DOTALL).matcher(type).replaceAll("");
            
            //System.out.println("Type------------\n"+type);
            new VarVisitor().visit(cn, null);
            new MethodVisitor().visit(cn, type);
            new ConstructorVisitor().visit(cn,null);
            //System.out.println("}\n");
        }//end of visit    	
    }//end of ClassOrInterfaceVisitor
    
    private static class MethodVisitor extends VoidVisitorAdapter {
    	@Override
        public void visit(MethodDeclaration mn, Object arg) {
    			String body = arg.toString();
    			if(body.contains(mn.getName()))
    			{
    			//System.out.println(arg);    				
    						if(body.contains("interface"))
    							methodList.add(new MethodStore(mn.getName(), className, 1, mn.getParameters(), mn.getType()));
    						else
    							methodList.add(new MethodStore(mn.getName(), className, mn.getModifiers(), mn.getParameters(), mn.getType()));
    						//System.out.println("\n\n\n"+mn.getName()+" : "+mn.getModifiers()+":"+mn.getType()+":"+mn.getParameters()+"------This is method\n\n\n");
    			}
    	}//end of visit     	
    }//end of MethodVisitor
    
    private static class ConstructorVisitor extends VoidVisitorAdapter {
    	@Override
        public void visit(ConstructorDeclaration mn, Object arg) {
    		methodList.add(new MethodStore(mn.getName(), className, mn.getModifiers(), mn.getParameters(), null));
    		//System.out.println(mn.getName()+" : "+mn.getModifiers()+":"+mn.getParameters()+"------This is constructor");
        }//end of visit     	
    }//end of MethodVisitor
    
    private static class VarVisitor extends VoidVisitorAdapter {
    	@Override
        public void visit(FieldDeclaration vn, Object arg) {
        	variableList.add(new VariableStore(vn.getVariables().toString(), className, vn.getType(), vn.getModifiers()));
        }//end of visit  
    }//end of VarVisitor    
} 