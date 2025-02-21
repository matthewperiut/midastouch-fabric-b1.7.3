package com.slainlight.midastouch.item;

import com.matthewperiut.accessoryapi.api.Accessory;
import com.slainlight.midastouch.entity.GoldenEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;

public class MidasGlove extends TemplateItem implements Accessory
{
    public MidasGlove(Identifier identifier)
    {
        super(identifier);
        setMaxCount(1);
        setMaxDamage(9);
    }

    @Override
    public String[] getAccessoryTypes(ItemStack item)
    {
        return new String[]{"gloves"};
    }

    @Override
    public ItemStack tickWhileWorn(PlayerEntity player, ItemStack itemInstance)
    {
        World level = player.world;

        // field_1645 is ticks alive
        if (player.age % 5 == 0) // occur every 1/4 seconds
        {
            List<Entity> entities = level.getEntities(player, Box.create(player.x - 1.F, player.y - 1.F, player.z - 1.F, player.x + 1.F, player.y + 1.F, player.z + 1.F));
            for (Entity entity : entities)
            {
                if (entity instanceof LivingEntity living && !(entity instanceof PlayerEntity))
                {
                    if (!((GoldenEntity) (Object) living).midastouch$getGolden())
                    {
                        ((GoldenEntity) (Object) living).midastouch$setGolden(true);
                        if (itemInstance != null)
                        {
                            if (itemInstance.getDamage() < itemInstance.getMaxDamage() - 1)
                                itemInstance.damage(1, null);
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
