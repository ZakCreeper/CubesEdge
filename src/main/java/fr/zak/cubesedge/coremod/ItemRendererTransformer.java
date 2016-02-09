package fr.zak.cubesedge.coremod;

import java.util.ArrayList;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ItemRendererTransformer implements IClassTransformer {

	private String renderItemMethodName;
	private static String className;

	@Override
	public byte[] transform(String name, String transformedName,
			byte[] basicClass) {
		if (transformedName
				.equals("net.minecraft.client.renderer.ItemRenderer")) {
			System.out
					.println("Cube\'s Edge Core - Patching class ItemRenderer...");
			renderItemMethodName = CubesEdgeLoadingPlugin.obfuscation ? "a"
					: "renderItemInFirstPerson";
			className = CubesEdgeLoadingPlugin.obfuscation ? "bly"
					: "net/minecraft/client/renderer/ItemRenderer";

			ClassReader cr = new ClassReader(basicClass);
			ClassNode cn = new ClassNode(Opcodes.ASM4);
			cr.accept(cn, 0);
			for (Object mnObj : cn.methods) {
				MethodNode mn = (MethodNode) mnObj;
				if (mn.name.equals(renderItemMethodName)
						&& mn.desc.equals("(F)V")) {
					patchRenderHandMethod(mn);
				}
			}
			ClassWriter cw = new ClassWriter(0);
			cn.accept(cw);
			System.out
					.println("Cube\'s Edge Core - Patching class ItemRenderer done.");
			return cw.toByteArray();
		} else {
			return basicClass;
		}
	}

	private static void patchRenderHandMethod(MethodNode mn) {

		System.out.println("\tPatching method renderItemFirstPerson in ItemRenderer");
		
		InsnList newList = new InsnList();
		InsnList newList2 = new InsnList();

		mn.localVariables = new ArrayList<LocalVariableNode>(5);
		
		int loc = 0, loc2 = 0;
		
		AbstractInsnNode insn = null, insn2 = null;
		
		for(int i = 0; i < mn.instructions.size(); i++) {
			insn = mn.instructions.get(i);
			if(insn.getOpcode() == Opcodes.INVOKESTATIC){
				loc2++;
				if(loc2 == 2){
					insn2 = insn;
					loc2++;
				}
			}
			if(insn.getOpcode() == Opcodes.INVOKESPECIAL){
				loc++;
				if(loc == 14){
					mn.instructions.remove(mn.instructions.get(i-4));
					break;
				}
			}
			System.out.println(insn.getOpcode());
		}
		newList.add(new VarInsnNode(Opcodes.ALOAD, 0));
		newList.add(new VarInsnNode(Opcodes.FLOAD, 1));
		newList.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
				"fr/zak/cubesedge/coremod/Patch",
				"renderRightHand", "(Lnet/minecraft/client/entity/EntityPlayerSP;FFL" + className + ";F)V", false));
		
		newList2.add(new VarInsnNode(Opcodes.FLOAD, 1));
		newList2.add(new VarInsnNode(Opcodes.ALOAD, 0));
		newList2.add(new VarInsnNode(Opcodes.FLOAD, 2));
		newList2.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
				"fr/zak/cubesedge/coremod/Patch",
				"renderLeftHand", "(FL" + className + ";F)V", false));

		mn.instructions.insert(insn, newList);
		mn.instructions.remove(insn);
		
		mn.instructions.insert(insn2, newList2);
	}
}
