package fr.zak.cubesedge.gui;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Property;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.IEventListener;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fr.zak.cubesedge.Movement;
import fr.zak.cubesedge.Util;

@SideOnly(Side.CLIENT)
public class GuiMovementList extends GuiListExtended {
	private final GuiConfig config;
	private final Minecraft mc;
	private final GuiListExtended.IGuiListEntry[] listEntry;
	private int lenghtName = 0;

	public GuiMovementList(GuiConfig config, Minecraft mc) {
		super(mc, config.width, config.height, 43, config.height - 32, 20);
		this.config = config;
		this.mc = mc;
		Object[] aObject = ArrayUtils.clone(Util.getMovements());
		this.listEntry = new GuiListExtended.IGuiListEntry[aObject.length];
		int i = 0;
		Object[] aObject1 = aObject;
		int j = aObject.length;

		for (int k = 0; k < j; ++k) {
			Movement movement = (Movement) aObject1[k];

			int l = mc.fontRendererObj.getStringWidth(movement.getName());

			if (l > this.lenghtName) {
				this.lenghtName = l;
			}

			this.listEntry[i++] = new GuiMovementList.MovementEntry(movement);
		}
	}

	protected int getSize() {
		return this.listEntry.length;
	}

	/**
	 * Gets the IGuiListEntry object for the given index
	 */
	public GuiListExtended.IGuiListEntry getListEntry(int slot) {
		return this.listEntry[slot];
	}

	protected int getScrollBarX() {
		return super.getScrollBarX() + 15;
	}

	/**
	 * Gets the width of the list
	 */
	public int getListWidth() {
		return super.getListWidth() + 32;
	}

	@SideOnly(Side.CLIENT)
	public class MovementEntry implements GuiListExtended.IGuiListEntry {
		private final Movement movement;
		private final String movementName;
		private final GuiButton btnDisable;

		private MovementEntry(Movement mov) {
			this.movement = mov;
			this.movementName = mov.getName();
			this.btnDisable = new GuiButton(0, 0, 0, 100, 18, "");
		}

		public void drawEntry(int slotIndex, int x, int y, int listWidth,
				int slotHeight, int mouseX, int mouseY, boolean isSelected){
			GuiMovementList.this.mc.fontRendererObj.drawString(this.movementName,
					x + 105 - GuiMovementList.this.lenghtName,
					y + slotHeight / 2
					- GuiMovementList.this.mc.fontRendererObj.FONT_HEIGHT
					/ 2, 16777215);
			this.btnDisable.xPosition = x + 125;
			this.btnDisable.yPosition = y;
			if (!movement.isMovementDisabled()) {
				this.btnDisable.displayString = "ON";
			} else if (movement.isMovementDisabled()) {
				this.btnDisable.displayString = "OFF";
			}

			this.btnDisable.drawButton(GuiMovementList.this.mc, mouseX,
					mouseY);
		}

		/**
		 * Returns true if the mouse has been pressed on this control.
		 */
		public boolean mousePressed(int p_148278_1_, int p_148278_2_,
				int p_148278_3_, int p_148278_4_, int p_148278_5_,
				int p_148278_6_) {
			if (this.btnDisable.mousePressed(GuiMovementList.this.mc,
					p_148278_2_, p_148278_3_)) {
				if (this.movement.isMovementDisabled()) {
					this.movement.enable();
					Property prop = Util.cfg.get("movements", this.movement.getName(),
							true);
					prop.set(true);
					if (Util.cfg.hasChanged()) {
						Util.cfg.save();
					}
					for (Method m : movement.getClass().getDeclaredMethods()) {
						if (m.isAnnotationPresent(SubscribeEvent.class)) {
							if (m.getParameterTypes()[0].getName().contains(
									"cpw")) {
								FMLCommonHandler.instance().bus()
								.register(movement);
							} else if (m.getParameterTypes()[0].getName()
									.contains("minecraftforge")) {
								MinecraftForge.EVENT_BUS.register(movement);
							}
						}
					}
					for (Field f : movement.getClass().getDeclaredFields()) {
						if (f.getGenericType().toString()
								.contains(KeyBinding.class.getName())) {
							f.setAccessible(true);
							try {
								ClientRegistry
								.registerKeyBinding((KeyBinding) f
										.get(movement));
								boolean flag = true;
								for (Object o : (HashSet) ObfuscationReflectionHelper
										.getPrivateValue(KeyBinding.class,
												(KeyBinding) f.get(movement), 2)) {
									if (o.equals("Cube's Edge")) {
										flag = false;
										break;
									}
								}
								if (flag) {
									((HashSet) ObfuscationReflectionHelper
											.getPrivateValue(KeyBinding.class,
													(KeyBinding) f
													.get(movement), 2))
													.add("Cube's Edge");
								}
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}
							f.setAccessible(false);
						}
					}
				} else {
					this.movement.disable();
					Property prop = Util.cfg.get("movements", this.movement
							.getName(),
							true);
					prop.set(false);
					if (Util.cfg.hasChanged()) {
						Util.cfg.save();
					}
					for (Method m : movement.getClass().getDeclaredMethods()) {
						if (m.isAnnotationPresent(SubscribeEvent.class)) {
							if (m.getParameterTypes()[0].getName().contains(
									"cpw")) {
								if(((ConcurrentHashMap<Object, ArrayList<IEventListener>>)ObfuscationReflectionHelper.getPrivateValue(EventBus.class, FMLCommonHandler.instance().bus(), 1)).containsKey(movement)){
									FMLCommonHandler.instance().bus()
									.unregister(movement);
								}
							} else if (m.getParameterTypes()[0].getName()
									.contains("minecraftforge")) {
								if(((ConcurrentHashMap<Object, ArrayList<IEventListener>>)ObfuscationReflectionHelper.getPrivateValue(EventBus.class, MinecraftForge.EVENT_BUS, 1)).containsKey(movement)){
									MinecraftForge.EVENT_BUS.unregister(movement);
								}
							}
						}
					}
					for (Field f : movement.getClass().getDeclaredFields()) {
						if (f.getGenericType().toString()
								.contains(KeyBinding.class.getName())) {
							f.setAccessible(true);
							try {
								Minecraft.getMinecraft().gameSettings.keyBindings = ArrayUtils
										.removeElement(
												Minecraft.getMinecraft().gameSettings.keyBindings,
												(KeyBinding) f.get(movement));
								for (KeyBinding kb : Minecraft.getMinecraft().gameSettings.keyBindings) {
									if (((String) ObfuscationReflectionHelper
											.getPrivateValue(KeyBinding.class,
													kb, 5))
													.equals("Cube's Edge")) {
										return true;
									}
								}
								((HashSet) ObfuscationReflectionHelper
										.getPrivateValue(KeyBinding.class,
												(KeyBinding) f.get(movement), 2))
												.remove("Cube's Edge");
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}
							f.setAccessible(false);
						}
					}
				}
				return true;
			} else {
				return false;
			}
		}

		/**
		 * Fired when the mouse button is released. Arguments: index, x, y,
		 * mouseEvent, relativeX, relativeY
		 */
		public void mouseReleased(int p_148277_1_, int p_148277_2_,
				int p_148277_3_, int p_148277_4_, int p_148277_5_,
				int p_148277_6_) {
			this.btnDisable.mouseReleased(p_148277_2_, p_148277_3_);
		}

		@Override
		public void setSelected(int p_178011_1_, int p_178011_2_,
				int p_178011_3_) {
			// TODO Auto-generated method stub
			
		}
	}
}