package de.matthiasmann.tcbotaniaexoflame.core;

public class ObfSafeName {
    private final String deobf, srg;

    public ObfSafeName(String deobf, String srg) {
        this.deobf = deobf;
        this.srg = srg;
    }
    
    public String getName() {
        return TCBotaniaExoflameCoreLoader.runtimeDeobfEnabled ? srg : deobf;
    }
    
    public boolean matches(String name) {
        return getName().equals(name);
    }
  }
  
