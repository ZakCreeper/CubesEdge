package fr.zak.cubesedge.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiConfig extends GuiScreen {
	/**
	 * A reference to the screen object that created this. Used for navigating
	 * between screens.
	 */
	private GuiScreen parentScreen;
	protected String title = "Movements";
	/** Reference to the GameSettings object. */

	private GuiMovementList movementList;

	public GuiConfig(GuiScreen par1GuiScreen) {
		this.parentScreen = par1GuiScreen;
	}

	/**
	 * Called when the mouse is clicked.
	 */
	protected void mouseClicked(int par1, int par2, int par3) {
		if (par3 != 0 || !this.movementList.func_148179_a(par1, par2, par3)) {
			super.mouseClicked(par1, par2, par3);
		}
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		this.movementList = new GuiMovementList(this, this.mc);
		this.buttonList.add(new GuiButton(200, this.width / 2 - 100,
				this.height - 27, I18n.format("gui.done", new Object[0])));
	}

	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.id == 200) {
			this.mc.displayGuiScreen(this.parentScreen);
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		this.movementList.drawScreen(par1, par2, par3);
		this.drawCenteredString(this.fontRendererObj, this.title,
				this.width / 2, 20, 16777215);

		super.drawScreen(par1, par2, par3);
	}
}