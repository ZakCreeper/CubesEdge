package fr.zak.cubesedge.coremod;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class EntityRendererTransformer implements IClassTransformer {

	private String renderHandMethodName;
	private static String className;

	@Override
	public byte[] transform(String name, String transformedName,
			byte[] basicClass) {
		if (transformedName
				.equals("net.minecraft.client.renderer.EntityRenderer")) {
			System.out
			.println("Cube\'s Edge Core - Patching class EntityRenderer...");
			renderHandMethodName = CubesEdgeLoadingPlugin.obfuscation ? "b"
					: "renderHand";
			className = CubesEdgeLoadingPlugin.obfuscation ? "blt"
					: "net/minecraft/client/renderer/EntityRenderer";

			ClassReader cr = new ClassReader(basicClass);
			ClassNode cn = new ClassNode(Opcodes.ASM4);
			cr.accept(cn, 0);
			for (Object mnObj : cn.methods) {
				MethodNode mn = (MethodNode) mnObj;
				if (mn.name.equals(renderHandMethodName)
						&& mn.desc.equals("(FI)V")) {
					patchRenderHandMethod(mn);
				}
			}
			ClassWriter cw = new ClassWriter(0);
			cn.accept(cw);
			System.out.println("Cube\'s Edge Core - Patching class EntityRenderer done.");
			byte[] p = cw.toByteArray();
			try
			{
				FileOutputStream out = new FileOutputStream("EntityRenderer.class");
				out.write(p);
				out.close();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
			return p;
		} else {
			return basicClass;
		}
	}

	private static void patchRenderHandMethod(MethodNode mn) {

		System.out.println("\tPatching method renderHand in EntityRenderer");
		InsnList newList = new InsnList();

		mn.localVariables = new ArrayList<LocalVariableNode>(5);
		int loc = 0;
		AbstractInsnNode insn = null;
		for(int i = 0; i < mn.instructions.size(); i++) {
			insn = mn.instructions.get(i);
			if(insn.getOpcode() == Opcodes.INVOKESTATIC){
				loc++;
				if(loc == 9){
					break;
				}
			}
		}

		newList.add(new VarInsnNode(Opcodes.ALOAD, 0));
		newList.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
				"fr/zak/cubesedge/coremod/Patch",
				"entityRendererRenderHandPatch", "(L" + className + ";)V", false));

		System.out.println(insn);
		mn.instructions.insert(insn, newList);
		mn.instructions.remove(insn);
	}
}
