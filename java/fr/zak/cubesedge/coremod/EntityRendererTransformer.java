package fr.zak.cubesedge.coremod;

import java.util.Iterator;

import jdk.internal.org.objectweb.asm.Opcodes;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.profiler.IPlayerUsage;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class EntityRendererTransformer implements IClassTransformer{

	private String methodName;
	private static String className;
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (transformedName.equals("net.minecraft.client.renderer.EntityRenderer")){
			System.out.println("Cube\'s Edge Core - Patching class EntityRenderer...");
			methodName = CubesEdgeFMLLoadingPlugin.obfuscation ? "func_78476_b" : "renderHand";
			className = CubesEdgeFMLLoadingPlugin.obfuscation ? "bll" : "net/minecraft/client/renderer/EntityRenderer";

			ClassReader cr = new ClassReader(basicClass);
			ClassNode cn = new ClassNode(Opcodes.ASM4);
			cr.accept(cn, 0);
			for (Object mnObj : cn.methods) {
				MethodNode mn = (MethodNode)mnObj;
				if (mn.name.equals(methodName)) {
					patchMethod(mn);
				}
			}
			ClassWriter cw = new ClassWriter(0);
			cn.accept(cw);
			System.out.println("Cube\'s Edge Core - Patching class EntityRenderer done.");
			return cw.toByteArray();
		}
		else{
			return basicClass;
		}
	}
	
	private static void patchMethod(MethodNode mn) {

		System.out.println("\tPatching method renderHand in EntityRenderer");
		InsnList newList = new InsnList();

		Iterator<AbstractInsnNode> it = mn.instructions.iterator();
		while (it.hasNext()) {
			AbstractInsnNode insn = it.next();
			if (insn.getOpcode() == Opcodes.RETURN) {
				newList.add(new VarInsnNode(Opcodes.FLOAD, 1));
				newList.add(new VarInsnNode(Opcodes.ILOAD, 2));
				newList.add(new VarInsnNode(Opcodes.ALOAD, 0));
				newList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "fr/zak/cubesedge/coremod/Patch", "entityRendererRenderHandPatch"
						, "(FIL" + className + ";)V"));
				newList.add(insn);
			}
		}

		mn.instructions = newList;
	}	
}
