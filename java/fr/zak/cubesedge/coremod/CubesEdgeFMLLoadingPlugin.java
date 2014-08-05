package fr.zak.cubesedge.coremod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;

@MCVersion(value = "1.7.2")
public class CubesEdgeFMLLoadingPlugin implements cpw.mods.fml.relauncher.IFMLLoadingPlugin {

	public static boolean obfuscation;

	private void detectObfuscation()
    {
        obfuscation = true;
        try
        {
            Method[] methods = Class.forName("net.minecraft.profiler.IPlayerUsage").getDeclaredMethods();
            for(Method m : methods)
            {
            	m.setAccessible(true);
            	if(m.getName().equalsIgnoreCase("isSnooperEnabled"))
            	{
            		obfuscation = false;
            		return;
            	}
            }
        }
        catch (Exception e)
        {
        }
    }
	
	@Override
	public String[] getASMTransformerClass() {
		return new String[]{EntityRendererTransformer.class.getName(), EntityTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		detectObfuscation();
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
