package fr.zak.cubesedge;

import com.sun.xml.internal.stream.Entity;

public class Util {
	public static boolean obfuscated = Entity.class.getDeclaredMethods()[3].getName().equals("isUnparsed") ? false : true; 
}
