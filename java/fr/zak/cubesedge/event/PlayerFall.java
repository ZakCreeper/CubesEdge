package fr.zak.cubesedge.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.entity.EntityPlayerCustom;

public class PlayerFall {

	@SubscribeEvent
	public void onFall(LivingFallEvent event){
		if(event.entityLiving instanceof EntityPlayer && ((EntityPlayerCustom)event.entityLiving.getExtendedProperties("Player Custom")).isRolling){
			event.distance = 0;
		}
	}
}
