package fr.zak.cubesedge.ticks;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import fr.zak.cubesedge.Movement;
import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.entity.EntityPlayerCustom;
import fr.zak.cubesedge.event.KeyHandler;
import fr.zak.cubesedge.event.SpeedEvent;
import fr.zak.cubesedge.packet.PacketPlayer;
import fr.zak.cubesedge.renderer.EntityRendererCustom;

public class ClientTickHandler {

	private EntityPlayerCustom playerCustom;

	@SubscribeEvent
	public void playerUpdate(TickEvent.PlayerTickEvent event){
		if(event.phase == TickEvent.Phase.END){
			if(playerCustom == null){
				playerCustom = (EntityPlayerCustom)event.player.getExtendedProperties("Player Custom");
			}
			int heading = MathHelper.floor_double((double)(event.player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
			for(Object o : Util.getMovements()){
				for(Method m : o.getClass().getDeclaredMethods()){
					if(event.side == o.getClass().getAnnotation(Movement.class).value()){
						if(m.getName().equals("control")){
							if(m.getParameterTypes().length == 3){
								try {
									m.invoke(o, playerCustom, event.player, heading);
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									e.printStackTrace();
								}
							}
							else if(m.getParameterTypes().length == 2){
								try {
									m.invoke(o, playerCustom, event.player);
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void tick(TickEvent.RenderTickEvent event) {
		if(event.phase == TickEvent.Phase.START && Minecraft.getMinecraft().theWorld != null){
			playerCustom = (EntityPlayerCustom)Minecraft.getMinecraft().thePlayer.getExtendedProperties("Player Custom");
			for(Object o : Util.getMovements()){
				for(Method m : o.getClass().getDeclaredMethods()){
					if(event.side == o.getClass().getAnnotation(Movement.class).value()){
						if(m.getName().equals("renderTick")){
							try {
								m.invoke(o, playerCustom);
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
}
