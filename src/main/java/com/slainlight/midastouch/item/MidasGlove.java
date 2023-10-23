package com.slainlight.midastouch.item;

import com.matthewperiut.accessoryapi.api.Accessory;
import com.slainlight.midastouch.entity.GoldenEntity;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.TemplateItemBase;

import java.util.List;

public class MidasGlove extends TemplateItemBase implements Accessory
{
    public MidasGlove(Identifier identifier)
    {
        super(identifier);
        setMaxStackSize(1);
        setDurability(9);
    }

    @Override
    public String[] getAccessoryTypes(ItemInstance item)
    {
        return new String[]{"gloves"};
    }

    @Override
    public ItemInstance tickWhileWorn(PlayerBase player, ItemInstance itemInstance)
    {
        Level level = player.level;

        // field_1645 is ticks alive
        if (player.field_1645 % 5 == 0) // occur every 1/4 seconds
        {
            List<EntityBase> entities = level.getEntities(player, Box.create(player.x - 1.F, player.y - 1.F, player.z - 1.F, player.x + 1.F, player.y + 1.F, player.z + 1.F));
            for (EntityBase entity : entities)
            {
                if (entity instanceof Living living)
                {
                    if (!((GoldenEntity) (Object) living).midastouch$getGolden())
                    {
                        ((GoldenEntity) (Object) living).midastouch$setGolden(true);
                        if (itemInstance != null)
                        {
                            if (itemInstance.getDamage() < itemInstance.getDurability() - 1)
                                itemInstance.applyDamage(1, null);
                            else
                                itemInstance = null;
                        }
                    }
                }
            }
        }


        return itemInstance;
    }
}
