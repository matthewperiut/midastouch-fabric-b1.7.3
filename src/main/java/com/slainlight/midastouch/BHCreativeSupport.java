package com.slainlight.midastouch;

import com.slainlight.midastouch.item.ItemListener;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ItemStack;
import paulevs.bhcreative.api.CreativeTab;
import paulevs.bhcreative.api.SimpleTab;
import paulevs.bhcreative.registry.TabRegistryEvent;

public class BHCreativeSupport {
    public static CreativeTab tabMidasGlove;

    @EventListener
    public void onTabInit(TabRegistryEvent event){
        tabMidasGlove = new SimpleTab(ItemListener.MOD_ID.id("midas_glove"), ItemListener.MIDAS_GLOVE);
        event.register(tabMidasGlove);
        tabMidasGlove.addItem(new ItemStack(ItemListener.MIDAS_GLOVE, 1));
    }
}