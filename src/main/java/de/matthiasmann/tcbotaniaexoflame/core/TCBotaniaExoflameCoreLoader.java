package de.matthiasmann.tcbotaniaexoflame.core;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.Name(TCBotaniaExoflameCoreLoader.NAME)
public class TCBotaniaExoflameCoreLoader implements IFMLLoadingPlugin {
    public static final String NAME = "TCBotaniaExoflame";
    
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{TCBotaniaExoflameCoreTransformer.class.getName()};
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

