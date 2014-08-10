package fr.zak.cubesedge.coremod;

import java.util.Iterator;

import jdk.internal.org.objectweb.asm.Opcodes;
import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
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
	
	private static int returns = 0;
	
	private static void patchMethod(MethodNode mn) {

		System.out.println("\tPatching method processPlayer in NetHandlerPlayServer");
		InsnList newList = new InsnList();
		
		Iterator<AbstractInsnNode> it = mn.instructions.iterator();
		while (it.hasNext()) {
			AbstractInsnNode insn = it.next();
			newList.add(insn);
			if (insn.getOpcode() == Opcodes.RETURN) {
				returns++;
			}
			if(returns == 10){
//				newList.remove(insn.getPrevious());
//				newList.remove(insn);
			}
		}
		newList.add(new InsnNode(Opcodes.RETURN));
		mn.instructions = newList;
	}
}
