package fr.zak.cubesedge.coremod;

import java.util.Iterator;

import jdk.internal.org.objectweb.asm.Opcodes;
import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.sun.xml.internal.stream.Entity;

public class CubesEdgeTransformer implements IClassTransformer{

	private String entityClass;

	private String setAnglesMethodName;

	private boolean obfuscated = Entity.class.getDeclaredMethods()[3].getName().equals("isUnparsed") ? false : true;
	@Override
	public byte[] transform(String name, String transformedName,
			byte[] basicClass) {
		if (transformedName.equals("net.minecraft.entity.Entity")) {
			entityClass = obfuscated ? Translator.getMapedClassName("Entity") : "/net/minecraft/entity/Entity";

			setAnglesMethodName = obfuscated ? Translator.getMapedMethodName("Entity", "setAngles") : "setAngles";

			ClassReader cr = new ClassReader(basicClass);
			ClassNode cn = new ClassNode(Opcodes.ASM4);
			cr.accept(cn, 0);
			for (Object mnObj : cn.methods) {
				MethodNode mn = (MethodNode)mnObj;
				if (mn.name.equals(setAnglesMethodName)) {
					processRenderItemMethod(mn);
				}
			}
			ClassWriter cw = new ClassWriter(0);
			cn.accept(cw);

			return cw.toByteArray();
		}
		else{
			return basicClass;
		}
	}
	private void processRenderItemMethod(MethodNode mn) {

		System.out.println("\tPatching method setAngles in Entity");
		InsnList newList = new InsnList();

		Iterator<AbstractInsnNode> it = mn.instructions.iterator();
		while (it.hasNext()) {
			AbstractInsnNode insn = it.next();
			if (insn.getOpcode() == Opcodes.RETURN) {
				newList.add(new VarInsnNode(Opcodes.FLOAD, 1));
				newList.add(new VarInsnNode(Opcodes.FLOAD, 2));
				newList.add(new VarInsnNode(Opcodes.ALOAD, 0));
				newList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "fr/zak/cubesedge/coremod/Patch", "entitySetAnglesPatch"
						, "(FFLnet/minecraft/entity/Entity;)V"));
				newList.add(insn);
			}		
		}

		mn.instructions = newList;
	}

}
