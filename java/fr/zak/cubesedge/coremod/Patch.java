package fr.zak.cubesedge.coremod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.lwjgl.opengl.GL11;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import fr.zak.cubesedge.Util;

public class Patch {

	public static void entitySetAnglesPatch(float par1, float par2, Entity entity){
		float f2 = entity.rotationPitch;
		float f3 = entity.rotationYaw;
		if(!Util.isRolling){
			entity.rotationYaw = (float)((double)entity.rotationYaw + (double)par1 * 0.15D);
			entity.rotationPitch = (float)((double)entity.rotationPitch - (double)par2 * 0.15D);

			if (entity.rotationPitch < -90.0F)
			{
				entity.rotationPitch = -90.0F;
			}

			if (entity.rotationPitch > 90.0F)
			{
				entity.rotationPitch = 90.0F;
			}

			entity.prevRotationPitch += entity.rotationPitch - f2;
			entity.prevRotationYaw += entity.rotationYaw - f3;
		}
	}
	
	public static void renderPlayerRenderFirstPersonArmPatch(EntityPlayer player){
		
		System.out.println("Nyyyyyyyyaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
	}
}
