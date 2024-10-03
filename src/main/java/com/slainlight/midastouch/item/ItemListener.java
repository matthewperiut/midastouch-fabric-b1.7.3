package com.slainlight.midastouch.item;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;

public class ItemListener
{

    public static ItemBase MIDAS_GLOVE;

    @Entrypoint.Namespace
    public static final Namespace MOD_ID = Null.get();

    @EventListener
    public void registerItems(ItemRegistryEvent event)
    {
        MIDAS_GLOVE = new MidasGlove(MOD_ID.id("midas_glove")).setTranslationKey(MOD_ID, "midas_glove");
    }
}
