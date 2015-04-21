package com.lzx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

public class ScriptUtil {
	
	private URLClassLoader parentClassLoader;
    private String classpath;
    
	private static ScriptUtil instance;
	
	private static Map<String, Object> scripts = new HashMap<String, Object>();
	

	public static void invoke(String name,String methodName, Class<?>[] parameterTypes,Object[] args) {
		Object object = scripts.get(name);
		try {
			Method method = object.getClass().getMethod(methodName, parameterTypes);
			method.invoke(object, args);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public static void reload(String name,String url) {
		try {
			ScriptUtil.getInstance();
			String code = instance.readCode(url);
			Class<?> codeToClass = instance.codeToClass(name, code);
			
			Object newInstance = codeToClass.newInstance();
			scripts.put(name, newInstance);	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private ScriptUtil() {
		parentClassLoader = (URLClassLoader) this.getClass().getClassLoader();
		this.buildClassPath();
	}
	
	public static ScriptUtil getInstance() {
		if(instance == null) {
			synchronized (ScriptUtil.class) {
				if(instance == null) instance = new ScriptUtil();
			}
			
		}
		return instance;
	}
	private void buildClassPath() {
        this.classpath = null;
        StringBuilder sb = new StringBuilder();
        for (URL url : this.parentClassLoader.getURLs()) {
            String p = url.getFile();
            sb.append(p).append(File.pathSeparator);
        }
        this.classpath = sb.toString();
    }
	/**
	 * 从本地路径中读取字符串代码
	 * @param url 路径名
	 * @return 代码
	 * @throws IOException
	 */
	private String readCode(String url) throws IOException {
		StringBuilder code = new StringBuilder();
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader(url));
		String buf = null;
		while ((buf=reader.readLine())!=null) {
			code.append(buf).append("\n");
		}
		return code.toString();
	}
	
	@SuppressWarnings("resource")
	private Class<?> codeToClass(String name, String code) throws IOException {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<JavaFileObject>();
		ClassFileManager fileManager = new ClassFileManager(compiler.getStandardFileManager(diagnosticCollector, null, null));
		
		boolean result = false;
		try {
			List<JavaFileObject> jfiles = new ArrayList<JavaFileObject>();
	        jfiles.add(new StringJavaFileObject(name, code));
	        
			ArrayList<String> options = new ArrayList<String>();
			options.add("-encoding");
			options.add("utf-8");
			options.add("-classpath");
			options.add(this.classpath);
			
			CompilationTask task = compiler.getTask(null, fileManager, diagnosticCollector, options, null, jfiles);
			
			result = task.call();
		} finally {
			fileManager.close();
		}
		if(result) {
			return new DynamicClassLoader(parentClassLoader).loadClass(fileManager);
		}
		return null;
	}
	
	
}
