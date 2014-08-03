package fr.zak.cubesedge.coremod;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;

@MCVersion(value = "1.7.2")
public class CubesEdgeFMLLoadingPlugin implements cpw.mods.fml.relauncher.IFMLLoadingPlugin {

	@Override
	public String[] getASMTransformerClass() {
		// TODO Auto-generated method stub
		return new String[]{CubesEdgeTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		// TODO Auto-generated method stub
		return CubesEdgeDummyMod.class.getName();
	}

	@Override
	public String getSetupClass() {
		// TODO Auto-generated method stub
		return Translator.class.getName();
	}

	@Override
	public void injectData(Map<String, Object> data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAccessTransformerClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
