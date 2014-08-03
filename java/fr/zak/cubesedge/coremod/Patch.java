package fr.zak.cubesedge.coremod;

import net.minecraft.entity.Entity;

public class Patch {
	public static void entitySetAnglesPatch(float par1, float par2, Entity entity){
		float f2 = entity.rotationPitch;
        float f3 = entity.rotationYaw;
        entity.rotationYaw = (float)((double)entity.rotationYaw + (double)par1 * 0.15D);
        entity.rotationPitch = (float)((double)entity.rotationPitch - (double)par2 * 0.15D);

        entity.prevRotationPitch += entity.rotationPitch - f2;
        entity.prevRotationYaw += entity.rotationYaw - f3;
	}
}
