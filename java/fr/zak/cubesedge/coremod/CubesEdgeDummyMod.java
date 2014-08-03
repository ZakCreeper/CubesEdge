package fr.zak.cubesedge.coremod;

import java.util.Arrays;

import com.google.common.eventbus.EventBus;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;

public class CubesEdgeDummyMod extends DummyModContainer{

	public CubesEdgeDummyMod() {

		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = "cubesedgecore";
		meta.name = "Cube's Edge Core";
		meta.version = "1.7.2"; //String.format("%d.%d.%d.%d", majorVersion, minorVersion, revisionVersion, buildVersion);
		meta.authorList = Arrays.asList("Zak");
		meta.description = "";
		meta.updateUrl = "";
		meta.screenshots = new String[0];
		meta.logoFile = "";

	}

	@Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
		bus.register(this);
        return true;
    }
	
}
