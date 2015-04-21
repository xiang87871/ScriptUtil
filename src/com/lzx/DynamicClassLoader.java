package com.lzx;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class DynamicClassLoader extends URLClassLoader {

	public DynamicClassLoader(ClassLoader parent) {
		super(new URL[0], parent);
	}

	public Class<?> loadClass(ClassFileManager fileManager) {
    	
    	List<ClassFileObject> innerClass = fileManager.getInnerClass();
    	for (ClassFileObject jco : innerClass) {
    		byte[] classData = jco.getBytes();
    		String name = jco.getName();
    		name = name.replaceAll("/", ".").substring(1,name.indexOf(".class"));
            this.defineClass(name, classData, 0, classData.length);
		}
    	ClassFileObject jco = fileManager.getOutterClass();
    	byte[] classData = jco.getBytes();
    	String name = jco.getName();
    	name = name.replaceAll("/", ".").substring(1,name.indexOf(".class"));
    	return this.defineClass(name, classData, 0, classData.length);
    }
}
