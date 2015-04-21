package com.lzx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;

public class ClassFileManager extends ForwardingJavaFileManager<JavaFileManager> {

	private List<ClassFileObject> list = new ArrayList<ClassFileObject>();
	protected ClassFileManager(JavaFileManager fileManager) {
		super(fileManager);
	}
	
	public List<ClassFileObject> getInnerClass() {
		List<ClassFileObject> inners = new ArrayList<ClassFileObject>();
		if(list.size() > 1) {
			inners.addAll(list.subList(0, list.size()-1));
		}
		return inners;
	}
	
	public ClassFileObject getOutterClass() {
		if(list.size() < 1) {
			throw new RuntimeException("没有编译到的类");
		}
		return list.get(list.size()-1);
	}
	
	@Override
	public JavaFileObject getJavaFileForOutput(Location location,
			String className, Kind kind, FileObject sibling) throws IOException {
		ClassFileObject classFileObject = new ClassFileObject(className, kind);
		list.add(classFileObject);
		return classFileObject;
	}
	
	

}
