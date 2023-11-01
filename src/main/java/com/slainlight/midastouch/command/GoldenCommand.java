package com.slainlight.midastouch.command;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.SharedCommandSource;
import com.slainlight.midastouch.entity.GoldenEntity;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.util.maths.Box;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class GoldenCommand implements Command
{
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters)
    {
        if (parameters.length < 2)
        {
            manual(commandSource);
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
            commandSource.sendFeedback("Invalid entity id");
            return;
        }

        EntityBase target = null;
        for (Object o : commandSource.getPlayer().level.entities)
        {
            EntityBase e = (EntityBase) o;

            if (e.entityId == targetId)
            {
                target = e;
            }
        }

        if (target == null)
        {
            commandSource.sendFeedback("Target not found");
            return;
        }

        if (target instanceof Living living)
        {
            ((GoldenEntity) living).midastouch$setGolden(!((GoldenEntity) living).midastouch$getGolden());
            commandSource.sendFeedback("Golden: " + ((GoldenEntity) living).midastouch$getGolden());
        }
    }

    @Override
    public String name()
    {
        return "golden";
    }

    @Override
    public void manual(SharedCommandSource commandSource)
    {
        commandSource.sendFeedback("Usage: /golden {entity id}");
    }

    @Override
    public String[] suggestion(SharedCommandSource source, int parameterNum, String currentInput, String totalInput) {
        if (parameterNum == 1) {
            PlayerBase p = source.getPlayer();
            List<EntityBase> entities = p.level.getEntities(EntityBase.class, Box.create(p.x-20, p.y-20, p.z-20, p.x+20, p.y+20, p.z+20));

            // Use TreeMap to keep entries in order based on the distance
            TreeMap<Double, EntityBase> distanceMap = new TreeMap<>();

            for (EntityBase entity : entities) {
                double distance = p.distanceTo(entity);
                // Handle potential duplicates (unlikely but possible)
                while (distanceMap.containsKey(distance)) {
                    distance += 0.0001;  // Small offset to handle entities at almost same distance
                }
                distanceMap.put(distance, entity);
            }

            // Extract entity IDs from sorted entities and convert them to String
            // If entity is a PlayerBase, use getName() instead
            List<String> sortedEntityIDs = distanceMap.values().stream()
                    .filter(entityBase -> !(entityBase instanceof PlayerBase))
                    .filter(entityBase -> entityBase instanceof Living)
                    .map(entity -> Integer.toString(entity.entityId))
                    .collect(Collectors.toList());

            // Filter and modify the suggestions based on currentInput
            for (int i = sortedEntityIDs.size() - 1; i >= 0; i--) {  // Change loop condition to i >= 0
                if (!sortedEntityIDs.get(i).startsWith(currentInput)) {
                    sortedEntityIDs.remove(i);
                } else {
                    sortedEntityIDs.set(i, sortedEntityIDs.get(i).substring(currentInput.length()));
                }
            }

            return sortedEntityIDs.toArray(new String[0]);
        }
        return new String[0];
    }
}
