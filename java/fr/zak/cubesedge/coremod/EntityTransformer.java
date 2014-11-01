package fr.zak.cubesedge.coremod;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import fr.zak.cubesedge.CubesEdge;

public class EntityTransformer implements IClassTransformer {

	private String methodSetAnglesName, methodIsEntityInsideOpaqueBlockName;
	private static String className;

	@Override
	public byte[] transform(String name, String transformedName,
			byte[] basicClass) {
		if (name.equals("net.minecraft.profiler.IPlayerUsage")) {
			CubesEdgeLoadingPlugin.obfuscation = false;
		}
		if (name.equals("rk")) {
			CubesEdgeLoadingPlugin.obfuscation = true;
		}
		if (transformedName.equals("net.minecraft.entity.Entity")) {
			System.out.println("Cube\'s Edge Core - Patching class Entity...");
			methodSetAnglesName = CubesEdgeLoadingPlugin.obfuscation ? "c"
					: "setAngles";
			methodIsEntityInsideOpaqueBlockName = CubesEdgeLoadingPlugin.obfuscation ? "aa"
					: "isEntityInsideOpaqueBlock";
			className = CubesEdgeLoadingPlugin.obfuscation ? "sa"
					: "net/minecraft/entity/Entity";

			ClassReader cr = new ClassReader(basicClass);
			ClassNode cn = new ClassNode(Opcodes.ASM4);
			cr.accept(cn, 0);
			for (Object mnObj : cn.methods) {
				MethodNode mn = (MethodNode) mnObj;
				if (mn.name.equals(methodSetAnglesName)
						&& mn.desc.equals("(FF)V")) {
					patchSetAnglesMethod(mn);
				}
				if (mn.name.equals(methodIsEntityInsideOpaqueBlockName)
						&& mn.desc.equals("()Z")) {
					patchIsEntityInsideOpaqueBlockMethod(mn);
				}
			}
			ClassWriter cw = new ClassWriter(0);
			cn.accept(cw);

			System.out
					.println("Cube\'s Edge Core - Patching class Entity done.");
			return cw.toByteArray();
		} else {
			return basicClass;
		}
	}

	private static void patchSetAnglesMethod(MethodNode mn) {

		System.out.println("\tPatching method setAngles in Entity");
		InsnList newList = new InsnList();

		newList.add(new VarInsnNode(Opcodes.FLOAD, 1));
		newList.add(new VarInsnNode(Opcodes.FLOAD, 2));
		newList.add(new VarInsnNode(Opcodes.ALOAD, 0));
		newList.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
				"fr/zak/cubesedge/coremod/Patch", "entitySetAnglesPatch",
				"(FFL" + className + ";)V"));
		newList.add(new InsnNode(Opcodes.RETURN));

		mn.instructions = newList;
	}

	private static void patchIsEntityInsideOpaqueBlockMethod(MethodNode mn) {

		System.out
				.println("\tPatching method isEntityInsideOpaqueBlock in Entity");
		InsnList newList = new InsnList();

		newList.add(new VarInsnNode(Opcodes.ALOAD, 0));
		newList.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
				"fr/zak/cubesedge/coremod/Patch",
				"isEntityInsideOpaqueBlockPatch", "(L" + className + ";)Z"));
		newList.add(new InsnNode(Opcodes.IRETURN));

		mn.instructions = newList;
	}
}
