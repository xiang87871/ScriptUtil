package com.lzx;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;

public class StringJavaFileObject extends SimpleJavaFileObject {

	private CharSequence content;
	 
	protected final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    public StringJavaFileObject(String className, CharSequence content) {
        super(URI.create("string:///" + className.replace('.', '/') + JavaFileObject.Kind.SOURCE.extension), JavaFileObject.Kind.SOURCE);
        this.content = content;
    }
 
    @Override
    public CharSequence getCharContent(
            boolean ignoreEncodingErrors) {
        return content;
    }

    public byte[] getBytes() {
        return bos.toByteArray();
    }
    
	@Override
	public OutputStream openOutputStream() throws IOException {
		return bos;
	}
    
    
}
