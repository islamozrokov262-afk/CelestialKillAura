package com.islamozroko.celestialaura;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformmersmc.modmenu.api.ModMenuApi;

public class CelestialAuraModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return AuraConfigScreen::create;
    }
}
