package fr.zak.cubesedge.coremod;

import java.util.ArrayList;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class NetHandlerPlayServerTransformer implements IClassTransformer{

	private String methodName;
	private static String className, packetPlayerClassName;
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (transformedName.equals("net.minecraft.network.NetHandlerPlayServer")) {
			System.out.println("Cube\'s Edge Core - Patching class NetHandlerPlayServer...");
			methodName = CubesEdgeFMLLoadingPlugin.obfuscation ? "a" : "processPlayer";
			className = CubesEdgeFMLLoadingPlugin.obfuscation ? "mx" : "net/minecraft/network/NetHandlerPlayServer";
			packetPlayerClassName = CubesEdgeFMLLoadingPlugin.obfuscation ?  "ir" : "net/minecraft/network/play/client/C03PacketPlayer";
			
			ClassReader cr = new ClassReader(basicClass);
			ClassNode cn = new ClassNode(Opcodes.ASM4);
			cr.accept(cn, 0);
			for (Object mnObj : cn.methods) {
				MethodNode mn = (MethodNode)mnObj;
				if (mn.name.equals(methodName) && mn.desc.equals("(L" + packetPlayerClassName + ";)V")) {
					patchMethod(mn);
				}
			}
			ClassWriter cw = new ClassWriter(0);
			cn.accept(cw);

			System.out.println("Cube\'s Edge Core - Patching class NetHandlerPlayServer done.");
			return cw.toByteArray();
		}
		else{
			return basicClass;
		}
	}
	
	private static int returns = 0, aloads = 0, nb = -1;
	private static LabelNode l1 = new LabelNode();
	
	private static void patchMethod(MethodNode mn) {
		System.out.println("\tPatching method processPlayer in NetHandlerPlayServer");
		InsnList newList = new InsnList();
//		
//		Iterator<AbstractInsnNode> it = mn.instructions.iterator();
//		while (it.hasNext()) {
//			AbstractInsnNode insn = it.next();
//			if(insn.getOpcode() == Opcodes.ALOAD){
//				aloads++;
//			}
//			if(aloads == 131){
//				newList.add(new FieldInsnNode(Opcodes.GETSTATIC, "fr/zak/cubesedge/Util", "isSneaking", "Z"));
//				newList.add(new JumpInsnNode(Opcodes.IFNE, l1));
//				newList.add(new InsnNode(Opcodes.RETURN));
//				newList.add(l1);
//			}
//			System.out.println(insn.getOpcode());
//			if (insn.getOpcode() == Opcodes.RETURN) {
//				returns++;
//			}
//			if(returns == 10){
//				returns++;
//			}
//			newList.add(insn);
//		}
		mn.localVariables = new ArrayList<LocalVariableNode>(5);
		
		newList.add(new VarInsnNode(Opcodes.ALOAD, 0));
		newList.add(new VarInsnNode(Opcodes.ALOAD, 1));
		newList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "fr/zak/cubesedge/coremod/Patch", "processPlayerPatch", "(L" + className + ";L" + packetPlayerClassName + ";)V"));
		newList.add(new InsnNode(Opcodes.RETURN));
		mn.instructions = newList;
	}
}
