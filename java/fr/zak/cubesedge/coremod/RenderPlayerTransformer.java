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

public class RenderPlayerTransformer implements IClassTransformer{

	private String methodName;

	private boolean obfuscated = IPlayerUsage.class.getDeclaredMethods()[0].getName().equals("addServerStatsToSnooper") ? false : true;
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (transformedName.equals("net.minecraft.client.renderer.entity.RenderPlayer")){
			methodName = obfuscated ? Translator.getMapedMethodName("RenderPlayer", "renderFirstPersonArm") : "renderFirstPersonArm";

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

			return cw.toByteArray();
		}
		else{
			return basicClass;
		}
	}
	
	private static void patchMethod(MethodNode mn) {

		System.out.println("\tPatching method renderFirstPersonArm in RenderPlayer");
		InsnList newList = new InsnList();

		Iterator<AbstractInsnNode> it = mn.instructions.iterator();
		while (it.hasNext()) {
			AbstractInsnNode insn = it.next();
			if (insn.getOpcode() == Opcodes.RETURN) {
				newList.add(new VarInsnNode(Opcodes.ALOAD, 1));
				newList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "fr/zak/cubesedge/coremod/Patch", "renderPlayerRenderFirstPersonArmPatch"
						, "(Lnet/minecraft/entity/player/EntityPlayer;)V"));
				newList.add(insn);
			}
		}

		mn.instructions = newList;
	}	
}
