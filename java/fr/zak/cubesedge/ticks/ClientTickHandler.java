package fr.zak.cubesedge.ticks;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import fr.zak.cubesedge.Movement;
import fr.zak.cubesedge.MovementVar;
import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.entity.EntityPlayerCustom;

public class ClientTickHandler {

	private EntityPlayerCustom playerCustom;

	@SubscribeEvent
	public void playerUpdate(TickEvent.PlayerTickEvent event){
		if(event.phase == TickEvent.Phase.END){
			if(playerCustom == null){
				playerCustom = (EntityPlayerCustom)event.player.getExtendedProperties("Cube's Edge Player");
			}
			int heading = MathHelper.floor_double((double)(event.player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
			for(Object o : Util.getMovements()){
				if(!((MovementVar)o).isMovementDisabled()){
					for(Method m : o.getClass().getDeclaredMethods()){
						if(event.side == o.getClass().getAnnotation(Movement.class).side()){
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
	}

	@SubscribeEvent
	public void tick(TickEvent.RenderTickEvent event) {
		if(event.phase == TickEvent.Phase.START && Minecraft.getMinecraft().theWorld != null){
			playerCustom = (EntityPlayerCustom)Minecraft.getMinecraft().thePlayer.getExtendedProperties("Cube's Edge Player");
			for(Object o : Util.getMovements()){
				if(!((MovementVar)o).isMovementDisabled()){
					for(Method m : o.getClass().getDeclaredMethods()){
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
