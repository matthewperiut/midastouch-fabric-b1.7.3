package com.slainlight.midastouch.texture;

import com.slainlight.midastouch.item.ItemListener;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;

public class TextureListener
{
    @Entrypoint.Namespace
    public static Namespace MOD_ID = Null.get();

    @EventListener
    public void registerTextures(TextureRegisterEvent event)
    {
        ItemListener.MIDAS_GLOVE.setTexture(MOD_ID.id("item/midas_glove"));
    }
}
