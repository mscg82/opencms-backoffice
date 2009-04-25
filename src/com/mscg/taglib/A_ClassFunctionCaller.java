/**
 *
 */
package com.mscg.taglib;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * @author Giuseppe Miscione
 *
 */
public class A_ClassFunctionCaller extends SimpleTagSupport{

	/**
     * The EL varibale in which the result of the method invocation
     * will be put.
     */
    protected String var;

    /**
     * The list of method arguments.
     */
    private List<FunctionCallParameter> methodArguments;

    public A_ClassFunctionCaller(){
    	super();
    	methodArguments = new LinkedList<FunctionCallParameter>();
    }

    /**
	 * Adds an argument to the arguments list.
	 * @param methodArgument The argument that will be added.
	 */
	public void addMethodArgument(FunctionCallParameter methodArgument){
		methodArguments.add(methodArgument);
	}

	/**
	 * Builds an array of {@link Class} objects reflecting the
	 * types of the function arguments.
	 *
	 * @return An array of {@link Class} objects reflecting the
	 * types of the function arguments.
	 * @throws ClassNotFoundException If the parameter type cannot be loaded.
	 */
	protected Class[] buildParametersClassesArray() throws ClassNotFoundException{
		if(methodArguments.size() == 0)
			return null;
		else{
			Class classes[] = new Class[methodArguments.size()];
			int i = 0;
			for(FunctionCallParameter param : methodArguments){
				String type = param.getType();
				classes[i] = resolveType(type);
				i++;
			}
			return classes;
		}
	}

    /**
	 * Builds an array of {@link Object}s containing the values of
	 * the parameters.
	 *
	 * @return An array of {@link Object}s containing the values of
	 * the parameters.
	 */
	protected Object[] buildParametersValuesArray(){
		if(methodArguments.size() == 0)
			return null;
		else{
			Object values[] = new Object[methodArguments.size()];
			int i = 0;
			for(FunctionCallParameter param : methodArguments){
				try {
					values[i] = convertValue(resolveType(param.getType()), param.getValue());
				} catch (ClassNotFoundException e) {
					values[i] = param.getValue();
				}
				i++;
			}
			return values;
		}
	}

	/**
	 * Converts the supplied object value to an instance of the
	 * specified type.
	 *
	 * @param objType The type of the returned object.
	 * @param value The original object value.
	 * @return An instance of the provided type of the specifed object.
	 */
	protected Object convertValue(Class objType, Object value){
		Object ret = null;
		if(value != null){
			String type = objType.getName();
			ret = value;
			if(type.equals(Byte.class.getName()) || type.equals(byte.class.getName())){
				ret = Byte.parseByte((String)value);
			}
			else if(type.equals(Short.class.getName()) || type.equals(short.class.getName())){
				ret = Short.parseShort((String)value);
			}
			else if(type.equals(Integer.class.getName()) || type.equals(int.class.getName())){
				ret = Integer.parseInt((String)value);
			}
			else if(type.equals(Long.class.getName()) || type.equals(long.class.getName())){
				ret = Long.parseLong((String)value);
			}
			else if(type.equals(Float.class.getName()) || type.equals(float.class.getName())){
				ret = Float.parseFloat((String)value);
			}
			else if(type.equals(Double.class.getName()) || type.equals(double.class.getName())){
				ret = Double.parseDouble((String)value);
			}
			else if(type.equals(Boolean.class.getName()) || type.equals(boolean.class.getName())){
				ret = Boolean.parseBoolean((String)value);
			}
			else if(type.equals(Character.class.getName()) || type.equals(char.class.getName())){
				ret = ((String)value).charAt(0);
			}
		}
		return ret;
	}

	/**
	 * @return the methodArguments
	 */
	public List<FunctionCallParameter> getMethodArguments() {
		return methodArguments;
	}

	/**
	 * @return the var
	 */
	public String getVar() {
		return var;
	}

	/**
	 * Tries to resolve a primitive type name.
	 *
	 * @param type The name of the type.
	 * @return A {@link Class} object representing the primitive
	 * type passed as parameter or <code>null</code> if the
	 * provided name isn't a primitive type.
	 */
	protected Class resolvePrimitiveType(String type){
		Class ret = null;
		if(type.equals(byte.class.getName())){
			ret = byte.class;
		}
		else if(type.equals(short.class.getName())){
			ret = short.class;
		}
		else if(type.equals(int.class.getName())){
			ret = int.class;
		}
		else if(type.equals(long.class.getName())){
			ret = long.class;
		}
		else if(type.equals(float.class.getName())){
			ret = long.class;
		}
		else if(type.equals(double.class.getName())){
			ret = double.class;
		}
		else if(type.equals(boolean.class.getName())){
			ret = boolean.class;
		}
		else if(type.equals(char.class.getName())){
			ret = char.class;
		}

		return ret;
	}

	/**
	 * Returns the {@link Class} object corresponding to
	 * the supplied type name.
	 *
	 * @param type The name of the class that will be resolved.
	 * @return A {@link Class} object corresponding to
	 * the supplied type name.
	 * @throws ClassNotFoundException If the class name cannot be resolved.
	 */
	public Class resolveType(String type) throws ClassNotFoundException{
		Class ret = resolvePrimitiveType(type);
		if(ret == null){
			try{
				ret = Class.forName(type);
			} catch(ClassNotFoundException e){
				ret = ClassLoader.getSystemClassLoader().loadClass(type);
			}
		}
		return ret;
	}

	/**
	 * @param methodArguments the methodArguments to set
	 */
	public void setMethodArguments(List<FunctionCallParameter> methodArguments) {
		this.methodArguments = methodArguments;
	}

	/**
	 * @param var the var to set
	 */
	public void setVar(String var) {
		this.var = var;
	}
}
