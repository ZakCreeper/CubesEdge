package fr.zak.cubesedge.ticks;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.event.KeyHandler;
import fr.zak.cubesedge.event.SpeedEvent;

public class ClientTickHandler {

	int temps = 0;
	boolean ralenti = false;

	public static boolean jump = false, animRight = false, animLeft = false;

	Minecraft minecraft = Minecraft.getMinecraft();

	private EntityRenderer renderer, prevRenderer;
	boolean backLeft = false, backRight = false;

	@SubscribeEvent
	public void playerUpdate(TickEvent.PlayerTickEvent event){
		if(event.phase == TickEvent.Phase.END){
			if(event.side == Side.CLIENT){
				sprintAnimation(event.player);
				ralenti();
				int heading = MathHelper.floor_double((double)(event.player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
				if(!event.player.capabilities.isFlying){
					roll(heading, event.player);
				}
				grab(heading, event.player);
				wallJumping(heading, (EntityPlayerSP)event.player);
				jump(heading, event.player);

				if(event.player.onGround){
					jump = false;
					animRight = false;
					animLeft = false;
				}
				if(event.player.capabilities.isFlying){
					animRight = false;
					animLeft = false;
				}
			}
		}
	}

	//	@SubscribeEvent
	//	public void tick(TickEvent.RenderTickEvent event) {
	//		if(event.phase == TickEvent.Phase.START && minecraft.theWorld != null){
	//			//						if(!Util.b){
	//			//							if (renderer == null) {
	//			//								renderer = new EntityRendererCustom(minecraft);
	//			//							}
	//			//							if (minecraft.entityRenderer != renderer) {
	//			//								// be sure to store the previous renderer
	//			//								prevRenderer = minecraft.entityRenderer;
	//			//								minecraft.entityRenderer = renderer;
	//			//							}
	//			//							forceSetSize(Entity.class, player, 0.6F, 0.6F);
	//			//						} else if (prevRenderer != null && minecraft.entityRenderer != prevRenderer) {
	//			//							// reset the renderer
	//			//							minecraft.entityRenderer = prevRenderer;
	//			//						}
	//		}
	//		if(event.phase == TickEvent.Phase.END && minecraft.theWorld != null){
	//			
	//		}
	//	}

	public void sprintAnimation(EntityPlayer player){
		if(player.isSprinting()){
			if(Util.tickRunningRight < 0.5F && !Util.beginingRunning){
				Util.tickRunningRight += (SpeedEvent.speed - 1) * 0.05;
			}
			if(Util.tickRunningRight >= 0.5F && !Util.beginingRunning){
				Util.beginingRunning = true;
			}
			if(Util.beginingRunning){
				Util.animRunnig = true;
			}
		}
		else{
			Util.animRunnig = false;
			Util.beginingRunning = false;
			Util.tickRunningLeft = 0;
			Util.tickRunningRight = 0;
		}
		if(Util.animRunnig){
			if(Util.tickRunningLeft < 0.5F && !backLeft){
				Util.tickRunningLeft += (SpeedEvent.speed - 1) * 0.2;
			}
			if(Util.tickRunningLeft >= 0.5F && !backLeft){
				backLeft = true;
			}
			if(Util.tickRunningLeft > 0 && backLeft){
				Util.tickRunningLeft -= (SpeedEvent.speed - 1) * 0.2;
			}
			if(Util.tickRunningLeft <= 0 && backLeft){
				backLeft = false;
			}
			if(Util.tickRunningRight > 0 && !backRight){
				Util.tickRunningRight -= (SpeedEvent.speed - 1) * 0.2;
			}
			if(Util.tickRunningRight <= 0 && !backRight){
				backRight = true;
			}
			if(Util.tickRunningRight < 0.5F && backRight){
				Util.tickRunningRight += (SpeedEvent.speed - 1) * 0.2;
			}
			if(Util.tickRunningRight >= 0.5F && backRight){
				backRight = false;
			}
		}
	}

	private void ralenti(){
		if(KeyHandler.keyPressedRalenti && !ralenti){
			ObfuscationReflectionHelper.setPrivateValue(Timer.class, ((Timer)ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), 15)), 5F, 0);
			ralenti = true;
		}
		if(ralenti){
			temps++;
			System.out.println(temps);
		}
		if(temps == 25 && ralenti){
			ObfuscationReflectionHelper.setPrivateValue(Timer.class, ((Timer)ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), 15)), 20F, 0);
			temps = 0;
			Minecraft.getMinecraft().gameSettings.mouseSensitivity = KeyHandler.defaultSensitivity;
			KeyHandler.keyPressedRalenti = false;
			ralenti = false;
		}
	}

	private void roll(int heading, EntityPlayer player){
		if(player.fallDistance > 4.5F && player.fallDistance < 15F){
			if (minecraft.gameSettings.keyBindSneak.isPressed()){
				Util.prevRolling = true;
			}
		}
		if(Util.prevRolling && player.onGround){
			Util.isRolling = true;
		}
		if(Util.isRolling){
			if(heading == 0){
				player.motionZ = 0.1;
				player.motionX = 0;
			}
			if(heading == 1){
				player.motionX = -0.1;
				player.motionZ = 0;
			}
			if(heading == 2){
				player.motionZ = -0.1;
				player.motionX = 0;
			}
			if(heading == 3){
				player.motionX = 0.1;
				player.motionZ = 0;
			}
			if(Util.rollingTime < 27){
				float f2 = player.rotationPitch;
				player.rotationPitch = (float)((double)player.rotationPitch + 10);
				player.prevRotationPitch += player.rotationPitch - f2;
				Util.rollingTime++;
			}
			if(Util.rollingTime == 27){
				player.rotationPitch = 0F;
				Util.rollingTime = 0;
				Util.prevRolling = false;
				Util.isRolling = false;
			}
		}
	}

	private void wallJumping(int heading, EntityPlayerSP player){
		if(player.fallDistance > 0){
			if((minecraft.theWorld.getBlock((int)MathHelper.floor_double(player.posX) + 1, (int)MathHelper.floor_double(player.posY), (int)MathHelper.floor_double(player.posZ)) != Blocks.air || minecraft.theWorld.getBlock((int)MathHelper.floor_double(player.posX) + 1, (int)MathHelper.floor_double(player.posY) - 1, (int)MathHelper.floor_double(player.posZ)) != Blocks.air) && (( heading == 0) || (heading == 2)) && player.moveForward > 0){
				player.motionZ *= 0.95D;
				player.motionX *= 0.95D;
				player.motionY *= 0.75D;
				if(player.movementInput.jump && !jump){
					animRight = false;
					animLeft = false;
					if(heading == 0){
						player.motionZ = 0.7D;
						player.motionX = -0.2D;
					}
					if(heading == 2){
						player.motionZ = -0.7D;
						player.motionX = -0.2D;
					}
					player.motionY = 0.41999998688697815D;
					jump = true;
				}
				if(!player.movementInput.jump){
					if(heading == 2){
						animRight = true;
					}
					if(heading == 0){
						animLeft = true;

					}
				}
			}
			if((minecraft.theWorld.getBlock((int)MathHelper.floor_double(player.posX) - 1, (int)MathHelper.floor_double(player.posY), (int)MathHelper.floor_double(player.posZ)) != Blocks.air || minecraft.theWorld.getBlock((int)MathHelper.floor_double(player.posX) - 1, (int)MathHelper.floor_double(player.posY) - 1, (int)MathHelper.floor_double(player.posZ)) != Blocks.air) && ((heading == 0) || (heading == 2)) && player.moveForward > 0){
				player.motionZ *= 0.95D;
				player.motionX *= 0.95D;
				player.motionY *= 0.75D;
				if(player.movementInput.jump && !jump){
					animRight = false;
					animLeft = false;
					if(heading == 0){
						player.motionZ = 0.7D;
						player.motionX = 0.2D;
					}
					if(heading == 2){
						player.motionZ = -0.7D;
						player.motionX = 0.2D;
					}
					player.motionY = 0.41999998688697815D;
					jump = true;
				}
				if(!player.movementInput.jump){
					if(heading == 2){
						animLeft = true;
					}
					if(heading == 0){
						animRight = true;

					}
				}
			}
			if((minecraft.theWorld.getBlock((int)MathHelper.floor_double(player.posX), (int)MathHelper.floor_double(player.posY), (int)MathHelper.floor_double(player.posZ) + 1) != Blocks.air || minecraft.theWorld.getBlock((int)MathHelper.floor_double(player.posX), (int)MathHelper.floor_double(player.posY) - 1, (int)MathHelper.floor_double(player.posZ) + 1) != Blocks.air) && ((heading == 3) || (heading == 1)) && player.moveForward > 0){
				player.motionZ *= 0.95D;
				player.motionX *= 0.95D;
				player.motionY *= 0.75D;
				if(player.movementInput.jump && !jump){
					animRight = false;
					animLeft = false;
					if(heading == 3){
						player.motionX = 0.7D;
						player.motionZ = -0.2D;
					}
					if(heading == 1){
						player.motionX = -0.7D;
						player.motionZ = -0.2D;
					}
					player.motionY = 0.41999998688697815D;
					jump = true;
				}
				if(!player.movementInput.jump){
					if(heading == 3){
						animRight = true;
					}
					if(heading == 1){
						animLeft = true;
					}
				}
			}
			if((minecraft.theWorld.getBlock((int)MathHelper.floor_double(player.posX), (int)MathHelper.floor_double(player.posY), (int)MathHelper.floor_double(player.posZ) - 1) != Blocks.air || minecraft.theWorld.getBlock((int)MathHelper.floor_double(player.posX) , (int)MathHelper.floor_double(player.posY) - 1, (int)MathHelper.floor_double(player.posZ) - 1) != Blocks.air) && ((heading == 3) || (heading == 1)) && player.moveForward > 0){
				player.motionZ *= 0.95D;
				player.motionX *= 0.95D;
				player.motionY *= 0.75D;
				if(player.movementInput.jump && !jump){
					animRight = false;
					animLeft = false;
					if(heading == 3){
						player.motionX = 0.7D;
						player.motionZ = 0.2D;
					}
					if(heading == 1){
						player.motionX = -0.7D;
						player.motionZ = 0.2D;
					}
					player.motionY = 0.41999998688697815D;
					jump = true;
				}
				if(!player.movementInput.jump){
					if(heading == 3){
						animLeft = true;
					}
					if(heading == 1){
						animRight = true;

					}
				}
			}
		}
	}

	private void jump(int heading, EntityPlayer player){
		if((minecraft.theWorld.getBlock((int)MathHelper.floor_double(player.posX), (int)MathHelper.floor_double(player.posY) - 1, (int)MathHelper.floor_double(player.posZ) - 1) != Blocks.air && heading == 2) || (minecraft.theWorld.getBlock((int)MathHelper.floor_double(player.posX), (int)MathHelper.floor_double(player.posY) - 1, (int)MathHelper.floor_double(player.posZ) + 1) != Blocks.air && heading == 0) || (minecraft.theWorld.getBlock((int)MathHelper.floor_double(player.posX) - 1, (int)MathHelper.floor_double(player.posY) - 1, (int)MathHelper.floor_double(player.posZ)) != Blocks.air && heading == 1) || (minecraft.theWorld.getBlock((int)MathHelper.floor_double(player.posX) + 1, (int)MathHelper.floor_double(player.posY) - 1, (int)MathHelper.floor_double(player.posZ)) != Blocks.air && heading == 3)){
			if((minecraft.theWorld.getBlock((int)MathHelper.floor_double(player.posX), (int)MathHelper.floor_double(player.posY), (int)MathHelper.floor_double(player.posZ) - 1) == Blocks.air && heading == 2) || (minecraft.theWorld.getBlock((int)MathHelper.floor_double(player.posX), (int)MathHelper.floor_double(player.posY), (int)MathHelper.floor_double(player.posZ) + 1) == Blocks.air && heading == 0) || (minecraft.theWorld.getBlock((int)MathHelper.floor_double(player.posX) - 1, (int)MathHelper.floor_double(player.posY), (int)MathHelper.floor_double(player.posZ)) == Blocks.air && heading == 1) || (minecraft.theWorld.getBlock((int)MathHelper.floor_double(player.posX) + 1, (int)MathHelper.floor_double(player.posY), (int)MathHelper.floor_double(player.posZ)) == Blocks.air && heading == 3)){
				if(ObfuscationReflectionHelper.getPrivateValue(EntityLivingBase.class, (EntityLivingBase)player, 41)){
					player.motionY = 0.41999998688697815D;
					Util.isGrabbing = true;
				}
				else{
					Util.isGrabbing = false;
				}
			}
		}
	}

	private void grab(int heading, EntityPlayer player){
		if((minecraft.theWorld.getBlock((int)MathHelper.floor_double(player.posX), (int)MathHelper.floor_double(player.posY), (int)MathHelper.floor_double(player.posZ) - 1) != Blocks.air && heading == 2) || (minecraft.theWorld.getBlock((int)MathHelper.floor_double(player.posX), (int)MathHelper.floor_double(player.posY), (int)MathHelper.floor_double(player.posZ) + 1) != Blocks.air && heading == 0) || (minecraft.theWorld.getBlock((int)MathHelper.floor_double(player.posX) - 1, (int)MathHelper.floor_double(player.posY), (int)MathHelper.floor_double(player.posZ)) != Blocks.air && heading == 1) || (minecraft.theWorld.getBlock((int)MathHelper.floor_double(player.posX) + 1, (int)MathHelper.floor_double(player.posY), (int)MathHelper.floor_double(player.posZ)) != Blocks.air && heading == 3)){
			if((minecraft.theWorld.getBlock((int)MathHelper.floor_double(player.posX), (int)MathHelper.floor_double(player.posY) + 1, (int)MathHelper.floor_double(player.posZ) - 1) == Blocks.air && heading == 2) || (minecraft.theWorld.getBlock((int)MathHelper.floor_double(player.posX), (int)MathHelper.floor_double(player.posY) + 1, (int)MathHelper.floor_double(player.posZ) + 1) == Blocks.air && heading == 0) || (minecraft.theWorld.getBlock((int)MathHelper.floor_double(player.posX) - 1, (int)MathHelper.floor_double(player.posY) + 1, (int)MathHelper.floor_double(player.posZ)) == Blocks.air && heading == 1) || (minecraft.theWorld.getBlock((int)MathHelper.floor_double(player.posX) + 1, (int)MathHelper.floor_double(player.posY) + 1, (int)MathHelper.floor_double(player.posZ)) == Blocks.air && heading == 3)){
				if(minecraft.theWorld.getBlock((int)MathHelper.floor_double(player.posX), (int)MathHelper.floor_double(player.posY) - 2, (int)MathHelper.floor_double(player.posZ)) == Blocks.air){
					if(!player.isSneaking() && !(Boolean)ObfuscationReflectionHelper.getPrivateValue(EntityLivingBase.class, (EntityLivingBase)player, 41)){
						player.motionY = 0.0;
					}
					else if(ObfuscationReflectionHelper.getPrivateValue(EntityLivingBase.class, (EntityLivingBase)player, 41)){
						player.motionY = 0.41999998688697815D;
					}
				}
			}
		}
	}

	public static void forceSetSize(Class clz, Entity ent, float width, float height)
	{
		try
		{
			Method m = clz.getDeclaredMethod(Util.obfuscation ? "func_70105_a" : "setSize", float.class, float.class);
			m.setAccessible(true);
			m.invoke(ent, width, height);
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e) 
		{
			e.printStackTrace();
		} 
		catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		} catch (SecurityException e) 
		{
			e.printStackTrace();
		} 
	}
}
