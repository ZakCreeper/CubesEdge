package fr.zak.cubesedge.coremod;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;

@MCVersion(value = "1.7.2")
public class CubesEdgeFMLLoadingPlugin implements cpw.mods.fml.relauncher.IFMLLoadingPlugin {

	public static boolean obfuscation = false;
	
	@Override
	public String[] getASMTransformerClass() {
		return new String[]{EntityRendererTransformer.class.getName(), EntityTransformer.class.getName(), NetHandlerPlayServerTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		return CubesEdgeDummyMod.class.getName();
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
