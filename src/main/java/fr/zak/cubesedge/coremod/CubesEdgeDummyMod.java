package fr.zak.cubesedge.coremod;

import java.util.Arrays;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

import com.google.common.eventbus.EventBus;

import fr.zak.cubesedge.Util;

public class CubesEdgeDummyMod extends DummyModContainer {

	public CubesEdgeDummyMod() {

		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = "cubesedgecore";
		meta.name = "Cube's Edge Core";
		meta.version = Util.VERSION; // String.format("%d.%d.%d.%d",
										// majorVersion, minorVersion,
										// revisionVersion, buildVersion);
		meta.authorList = Arrays.asList("Zak");
		meta.description = "";
		meta.updateUrl = "";
		meta.screenshots = new String[0];
		meta.logoFile = "";

	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}

}
