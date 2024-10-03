package com.slainlight.midastouch;

import com.matthewperiut.accessoryapi.api.AccessoryRegister;
import com.slainlight.midastouch.command.MidasSPC;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class MidasTouchMod implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        if (!FabricLoader.getInstance().isModLoaded("aether") || AccessoryRegister.getNumberOfType("gloves") < 1)
        {
            AccessoryRegister.add("gloves", 0, 2);
        }

        if (FabricLoader.getInstance().isModLoaded("spc"))
        {
            MidasSPC.add();
        }
    }
}
