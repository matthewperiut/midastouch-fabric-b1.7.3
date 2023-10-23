package com.slainlight.midastouch.command;

import com.matthewperiut.spc.api.Command;
import com.slainlight.midastouch.entity.GoldenEntity;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;

import static com.matthewperiut.spc.util.SPChatUtil.sendMessage;

public class GoldenCommand implements Command
{
    @Override
    public void command(PlayerBase player, String[] parameters)
    {
        if (parameters.length < 2)
        {
            manual();
            return;
        }

        int targetId;
        try
        {
            String targetStr = parameters[1];
            targetId = Integer.parseInt(targetStr);
        }
        catch (Exception e)
        {
            sendMessage("Invalid entity id");
            return;
        }

        EntityBase target = null;
        for (Object o : player.level.entities)
        {
            EntityBase e = (EntityBase) o;

            if (e.entityId == targetId)
            {
                target = e;
            }
        }

        if (target == null)
        {
            sendMessage("Target not found");
            return;
        }

        if (target instanceof Living living)
        {
            ((GoldenEntity) (Object) living).midastouch$setGolden(!((GoldenEntity) (Object) living).midastouch$getGolden());
            sendMessage("Golden: " + ((GoldenEntity) (Object) living).midastouch$getGolden());
        }
    }

    @Override
    public String name()
    {
        return "golden";
    }

    @Override
    public void manual()
    {
        sendMessage("Usage: /golden {entity id}");
    }
}
