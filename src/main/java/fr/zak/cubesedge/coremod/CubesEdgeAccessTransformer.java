package fr.zak.cubesedge.coremod;

import java.io.IOException;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

public class CubesEdgeAccessTransformer extends AccessTransformer {

	public CubesEdgeAccessTransformer() throws IOException {
		super("cubesedge_at.cfg");
	}

}
