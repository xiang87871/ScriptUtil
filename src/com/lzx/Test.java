package com.lzx;

public class Test {
	public static void main(String[] args) {
		ScriptUtil.reload("com.lzx.Test1", "src/com/lzx/Test1.java");
		ScriptUtil.invoke("com.lzx.Test1", "xxx", new Class[]{}, new Object[]{});
	}
}
