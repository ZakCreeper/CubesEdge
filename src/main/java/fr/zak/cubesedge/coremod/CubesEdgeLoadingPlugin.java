package fr.zak.cubesedge.coremod;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;

@MCVersion("1.7.10")
public class CubesEdgeLoadingPlugin implements
cpw.mods.fml.relauncher.IFMLLoadingPlugin {

	public static boolean obfuscation = false;

	@Override
	public String[] getASMTransformerClass() {
		return new String[] { EntityRendererTransformer.class.getName(),
				EntityTransformer.class.getName(),
				NetHandlerPlayServerTransformer.class.getName(),
				ItemRendererTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {

		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
	
}
