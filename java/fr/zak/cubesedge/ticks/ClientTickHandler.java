package fr.zak.cubesedge.ticks;

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
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;
import net.minecraftforge.client.ForgeHooksClient;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.event.KeyHandler;
import fr.zak.cubesedge.event.SpeedEvent;
import fr.zak.cubesedge.renderer.EntityRendererCustom;

public class ClientTickHandler {

	Minecraft minecraft = Minecraft.getMinecraft();

	private EntityRenderer renderer, prevRenderer;

	@SubscribeEvent
	public void playerUpdate(TickEvent.PlayerTickEvent event){
		if(event.phase == TickEvent.Phase.END){
			if(Util.isGrabbing && !event.player.capabilities.isCreativeMode){
				PotionEffect potioneffect = event.player.getActivePotionEffect(Potion.jump);
				float f1 = potioneffect != null ? (float)(potioneffect.getAmplifier() + 1) : 0.0F;
				int i = MathHelper.ceiling_float_int(event.player.fallDistance - 3.0F - f1);
				if(i > 0){
					event.player.playSound(i > 4 ? "game.neutral.hurt.fall.big" : "game.neutral.hurt.fall.small", 1.0F, 1.0F);
					damageEntity(EntityLivingBase.class, event.player, DamageSource.fall, (float)i);
				}
				event.player.fallDistance = 0;
			}
			if(event.side == Side.CLIENT){
				sprintAnimation(event.player);
				ralenti();

				int heading = MathHelper.floor_double((double)(event.player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
				if(!event.player.capabilities.isFlying){
					roll(heading, event.player);
					grab(heading, event.player);
					if(event.player instanceof EntityPlayerSP){
						wallJumping(heading, (EntityPlayerSP)event.player);
					}
					jump(heading, event.player);
					sneak(event.player);
				}

				if(event.player.onGround){
					Util.wallJump = false;
					Util.animRight = false;
					Util.animLeft = false;
				}
				if(event.player.capabilities.isFlying){
					Util.animRight = false;
					Util.animLeft = false;
				}
			}
		}
	}

	@SubscribeEvent
	public void tick(TickEvent.RenderTickEvent event) {
		if(event.phase == TickEvent.Phase.START && minecraft.theWorld != null){
			if(Util.isSneaking || Util.isRolling){
				if (renderer == null) {
					renderer = new EntityRendererCustom(minecraft);
				}
				if (minecraft.entityRenderer != renderer) {
					// be sure to store the previous renderer
					prevRenderer = minecraft.entityRenderer;
					minecraft.entityRenderer = renderer;
				}
				forceSetSize(Entity.class, minecraft.thePlayer, 0.6F, 0.6F);
			} else if (prevRenderer != null && minecraft.entityRenderer != prevRenderer && !minecraft.thePlayer.isSneaking()) {
				// reset the renderer
				minecraft.entityRenderer = prevRenderer;
				forceSetSize(Entity.class, minecraft.thePlayer, 0.6F, 1.8F);

			}
		}
	}
	

	private void sneak(EntityPlayer player) {
		if(!player.isSprinting() && Util.wasSprinting){
			if(player.isSneaking() && player.onGround && !Util.isRolling){
				Util.isSneaking = true;
			}
		}
		if(Util.isSneaking && player.isSneaking()){
			if(Util.sneakTime < 15){
				player.motionX *= (0.98F * 0.91F) + 1;
				player.motionZ *= (0.98F * 0.91F) + 1;
				System.out.println("test");
				Util.sneakTime++;
			}
			if(Util.sneakTime == 15){
				Util.isSneaking = false;
				Util.sneakTime = 0;
			}
		}
		if(Util.isSneaking && !player.isSneaking()){
			Util.isSneaking = false;
			Util.sneakTime = 0;
		}
		Util.wasSprinting = player.isSprinting();
	}

	private void sprintAnimation(EntityPlayer player){
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
			if(Util.tickRunningLeft < 0.5F && !Util.backLeft){
				Util.tickRunningLeft += (SpeedEvent.speed - 1) * 0.2;
			}
			if(Util.tickRunningLeft >= 0.5F && !Util.backLeft){
				Util.backLeft = true;
			}
			if(Util.tickRunningLeft > 0 && Util.backLeft){
				Util.tickRunningLeft -= (SpeedEvent.speed - 1) * 0.2;
			}
			if(Util.tickRunningLeft <= 0 && Util.backLeft){
				Util.backLeft = false;
			}
			if(Util.tickRunningRight > 0 && !Util.backRight){
				Util.tickRunningRight -= (SpeedEvent.speed - 1) * 0.2;
			}
			if(Util.tickRunningRight <= 0 && !Util.backRight){
				Util.backRight = true;
			}
			if(Util.tickRunningRight < 0.5F && Util.backRight){
				Util.tickRunningRight += (SpeedEvent.speed - 1) * 0.2;
			}
			if(Util.tickRunningRight >= 0.5F && Util.backRight){
				Util.backRight = false;
			}
		}
	}

	private void ralenti(){
		if(KeyHandler.keyPressedRalenti && !Util.ralenti){
			ObfuscationReflectionHelper.setPrivateValue(Timer.class, ((Timer)ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, minecraft, 15)), 5F, 0);
			Util.ralenti = true;
		}
		if(Util.ralenti){
			Util.temps++;
		}
		if(Util.temps == 25 && Util.ralenti){
			ObfuscationReflectionHelper.setPrivateValue(Timer.class, ((Timer)ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, minecraft, 15)), 20F, 0);
			Util.temps = 0;
			minecraft.gameSettings.mouseSensitivity = KeyHandler.defaultSensitivity;
			KeyHandler.keyPressedRalenti = false;
			Util.ralenti = false;
		}
	}

	private void roll(int heading, EntityPlayer player){
		if(player.fallDistance > 3.0F && player.fallDistance < 15F){
			if (minecraft.gameSettings.keyBindSneak.isPressed() && (player.worldObj.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY) - 3, MathHelper.floor_double(player.posZ)).isNormalCube() || player.worldObj.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY) - 4, MathHelper.floor_double(player.posZ)).isNormalCube())){
				Util.prevRolling = true;
			}
		}
		if(Util.prevRolling && player.onGround){
			Util.isRolling = true;
		}
		if(Util.isRolling){
			KeyBinding.setKeyBindState(minecraft.gameSettings.keyBindForward.getKeyCode(), true);
			KeyBinding.setKeyBindState(minecraft.gameSettings.keyBindLeft.getKeyCode(), false);
			KeyBinding.setKeyBindState(minecraft.gameSettings.keyBindRight.getKeyCode(), false);
			KeyBinding.setKeyBindState(minecraft.gameSettings.keyBindBack.getKeyCode(), false);
			KeyBinding.setKeyBindState(minecraft.gameSettings.keyBindSneak.getKeyCode(), false);
			player.motionZ *= 0.3;
			player.motionX *= 0.3;
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
				KeyBinding.setKeyBindState(minecraft.gameSettings.keyBindForward.getKeyCode(), false);
			}
		}
	}

	private void wallJumping(int heading, EntityPlayerSP player){
		if(player.fallDistance > 0){
			if((minecraft.theWorld.getBlock(MathHelper.floor_double(player.posX) + 1, MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ)).isNormalCube() || minecraft.theWorld.getBlock(MathHelper.floor_double(player.posX) + 1, MathHelper.floor_double(player.posY) - 1, MathHelper.floor_double(player.posZ)).isNormalCube()) && (( heading == 0) || (heading == 2)) && player.moveForward > 0){
				Util.isOnWall = true;
				player.motionZ *= 0.95D;
				player.motionX *= 0.95D;
				player.motionY *= 0.75D;
				if(player.movementInput.jump && !Util.wallJump){
					Util.animRight = false;
					Util.animLeft = false;
					if(heading == 0){
						player.motionZ = 0.7D;
						player.motionX = -0.2D;
					}
					if(heading == 2){
						player.motionZ = -0.7D;
						player.motionX = -0.2D;
					}
					player.motionY = 0.41999998688697815D;
					Util.wallJump = true;
				}
				if(!player.movementInput.jump){
					if(heading == 2){
						Util.animRight = true;
					}
					if(heading == 0){
						Util.animLeft = true;

					}
				}
			}
			else if((minecraft.theWorld.getBlock(MathHelper.floor_double(player.posX) - 1, MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ)).isNormalCube() || minecraft.theWorld.getBlock(MathHelper.floor_double(player.posX) - 1, MathHelper.floor_double(player.posY) - 1, MathHelper.floor_double(player.posZ)).isNormalCube()) && ((heading == 0) || (heading == 2)) && player.moveForward > 0){
				Util.isOnWall = true;
				player.motionZ *= 0.95D;
				player.motionX *= 0.95D;
				player.motionY *= 0.75D;
				if(player.movementInput.jump && !Util.wallJump){
					Util.animRight = false;
					Util.animLeft = false;
					if(heading == 0){
						player.motionZ = 0.7D;
						player.motionX = 0.2D;
					}
					if(heading == 2){
						player.motionZ = -0.7D;
						player.motionX = 0.2D;
					}
					player.motionY = 0.41999998688697815D;
					Util.wallJump = true;
				}
				if(!player.movementInput.jump){
					if(heading == 2){
						Util.animLeft = true;
					}
					if(heading == 0){
						Util.animRight = true;

					}
				}
			}
			else if((minecraft.theWorld.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ) + 1).isNormalCube() || minecraft.theWorld.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY) - 1, MathHelper.floor_double(player.posZ) + 1).isNormalCube()) && ((heading == 3) || (heading == 1)) && player.moveForward > 0){
				Util.isOnWall = true;
				player.motionZ *= 0.95D;
				player.motionX *= 0.95D;
				player.motionY *= 0.75D;
				if(player.movementInput.jump && !Util.wallJump){
					Util.animRight = false;
					Util.animLeft = false;
					if(heading == 3){
						player.motionX = 0.7D;
						player.motionZ = -0.2D;
					}
					if(heading == 1){
						player.motionX = -0.7D;
						player.motionZ = -0.2D;
					}
					player.motionY = 0.41999998688697815D;
					Util.wallJump = true;
				}
				if(!player.movementInput.jump){
					if(heading == 3){
						Util.animRight = true;
					}
					if(heading == 1){
						Util.animLeft = true;
					}
				}
			}
			else if((minecraft.theWorld.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ) - 1).isNormalCube() || minecraft.theWorld.getBlock(MathHelper.floor_double(player.posX) , MathHelper.floor_double(player.posY) - 1, MathHelper.floor_double(player.posZ) - 1).isNormalCube()) && ((heading == 3) || (heading == 1)) && player.moveForward > 0){
				Util.isOnWall = true;
				player.motionZ *= 0.95D;
				player.motionX *= 0.95D;
				player.motionY *= 0.75D;
				if(player.movementInput.jump && !Util.wallJump){
					Util.animRight = false;
					Util.animLeft = false;
					if(heading == 3){
						player.motionX = 0.7D;
						player.motionZ = 0.2D;
					}
					if(heading == 1){
						player.motionX = -0.7D;
						player.motionZ = 0.2D;
					}
					player.motionY = 0.41999998688697815D;
					Util.wallJump = true;
				}
				if(!player.movementInput.jump){
					if(heading == 3){
						Util.animLeft = true;
					}
					if(heading == 1){
						Util.animRight = true;

					}
				}
			}
			else {
				Util.isOnWall = false;
			}
		}
	}

	private void jump(int heading, EntityPlayer player){
		if((minecraft.theWorld.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY) - 1, MathHelper.floor_double(player.posZ) - 1).isNormalCube() && heading == 2) || (minecraft.theWorld.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY) - 1, MathHelper.floor_double(player.posZ) + 1).isNormalCube() && heading == 0) || (minecraft.theWorld.getBlock(MathHelper.floor_double(player.posX) - 1, MathHelper.floor_double(player.posY) - 1, MathHelper.floor_double(player.posZ)).isNormalCube() && heading == 1) || (minecraft.theWorld.getBlock(MathHelper.floor_double(player.posX) + 1, MathHelper.floor_double(player.posY) - 1, MathHelper.floor_double(player.posZ)).isNormalCube() && heading == 3)){
			if((minecraft.theWorld.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ) - 1) == Blocks.air && heading == 2) || (minecraft.theWorld.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ) + 1) == Blocks.air && heading == 0) || (minecraft.theWorld.getBlock(MathHelper.floor_double(player.posX) - 1, MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ)) == Blocks.air && heading == 1) || (minecraft.theWorld.getBlock(MathHelper.floor_double(player.posX) + 1, MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ)) == Blocks.air && heading == 3)){
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
		if((((minecraft.theWorld.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ) - 1).isNormalCube() && heading == 2) || (minecraft.theWorld.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ) + 1).isNormalCube() && heading == 0) || (minecraft.theWorld.getBlock(MathHelper.floor_double(player.posX) - 1, MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ)).isNormalCube() && heading == 1) || (minecraft.theWorld.getBlock(MathHelper.floor_double(player.posX) + 1, MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ)).isNormalCube() && heading == 3)) && ((minecraft.theWorld.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY) + 1, MathHelper.floor_double(player.posZ) - 1) == Blocks.air && heading == 2) || (minecraft.theWorld.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY) + 1, MathHelper.floor_double(player.posZ) + 1) == Blocks.air && heading == 0) || (minecraft.theWorld.getBlock(MathHelper.floor_double(player.posX) - 1, MathHelper.floor_double(player.posY) + 1, MathHelper.floor_double(player.posZ)) == Blocks.air && heading == 1) || (minecraft.theWorld.getBlock(MathHelper.floor_double(player.posX) + 1, MathHelper.floor_double(player.posY) + 1, MathHelper.floor_double(player.posZ)) == Blocks.air && heading == 3)) && (minecraft.theWorld.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY) - 2, MathHelper.floor_double(player.posZ)) == Blocks.air)) && !player.isOnLadder()){
			Util.isGrabbing = true;
			Util.grabbingDirections[heading] = true;
			if(heading == 0){
				Util.grabbingDirections[3] = true;
			}
			else{
				Util.grabbingDirections[heading - 1] = true;
			}

			if(heading == 3){
				Util.grabbingDirections[0] = true;
			}
			else{
				Util.grabbingDirections[heading + 1] = true;
			}
		}
		else{
			Util.isGrabbing = false;
			Util.grabbingDirections[0] = false;
			Util.grabbingDirections[1] = false;
			Util.grabbingDirections[2] = false;
			Util.grabbingDirections[3] = false;
		}
		if(!player.isSneaking() && !(Boolean)ObfuscationReflectionHelper.getPrivateValue(EntityLivingBase.class, (EntityLivingBase)player, 41) && Util.isGrabbing){
			player.motionY = 0.0;
		}
		else if((Boolean)ObfuscationReflectionHelper.getPrivateValue(EntityLivingBase.class, (EntityLivingBase)player, 41) && Util.isGrabbing){
			player.motionY = 0.41999998688697815D;
		}
	}

	private void forceSetSize(Class clz, Entity ent, float width, float height)
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

	private void damageEntity(Class c, EntityPlayer p, DamageSource d, float f){
		try {
			Method m = c.getDeclaredMethod(Util.obfuscation ? "func_70665_d" : "damageEntity", DamageSource.class, float.class);
			m.setAccessible(true);
			m.invoke(p, d, f);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
