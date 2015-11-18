package de.matthiasmann.tcbotaniaexoflame;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;

@Mod(modid = TCBotaniaExoflame.MODID, name = TCBotaniaExoflame.MODNAME, version = TCBotaniaExoflame.VERSION)
public class TCBotaniaExoflame
{
	public static final String MODID = "TCBotaniaExoflame";
	public static final String MODNAME = "TCBotaniaExoflame";
	public static final String VERSION = "1.0";

	@Instance(MODID)
	public static TCBotaniaExoflame instance = new TCBotaniaExoflame();	
	
}
