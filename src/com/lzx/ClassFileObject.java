package com.lzx;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;

public class ClassFileObject extends SimpleJavaFileObject {


	 
    protected final ByteArrayOutputStream bos =
        new ByteArrayOutputStream();
 
 
    public ClassFileObject(String name, JavaFileObject.Kind kind) {
        super(URI.create("string:///" + name.replace('.', '/')
            + kind.extension), kind);
    }
 
 
    public byte[] getBytes() {
        return bos.toByteArray();
    }
 
    @Override
    public OutputStream openOutputStream() throws IOException {
        return bos;
    }


}
