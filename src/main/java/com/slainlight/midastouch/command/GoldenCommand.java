package com.slainlight.midastouch.command;

import com.matthewperiut.retrocommands.api.Command;
import com.matthewperiut.retrocommands.util.SharedCommandSource;
import com.slainlight.midastouch.entity.GoldenEntity;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

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

        Entity target = null;
        for (Object o : commandSource.getPlayer().world.entities)
        {
            Entity e = (Entity) o;

            if (e.id == targetId)
            {
                target = e;
            }
        }

        if (target == null)
        {
            commandSource.sendFeedback("Target not found");
            return;
        }

        if (target instanceof LivingEntity living)
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
            PlayerEntity p = source.getPlayer();
            List<Entity> entities = p.world.collectEntitiesByClass(Entity.class, Box.create(p.x-20, p.y-20, p.z-20, p.x+20, p.y+20, p.z+20));

            // Use TreeMap to keep entries in order based on the distance
            TreeMap<Double, Entity> distanceMap = new TreeMap<>();

            for (Entity entity : entities) {
                double distance = p.getDistance(entity);
                // Handle potential duplicates (unlikely but possible)
                while (distanceMap.containsKey(distance)) {
                    distance += 0.0001;  // Small offset to handle entities at almost same distance
                }
                distanceMap.put(distance, entity);
            }

            // Extract entity IDs from sorted entities and convert them to String
            // If entity is a PlayerBase, use getName() instead
            List<String> sortedEntityIDs = distanceMap.values().stream()
                    .filter(entityBase -> !(entityBase instanceof PlayerEntity))
                    .filter(entityBase -> entityBase instanceof LivingEntity)
                    .map(entity -> Integer.toString(entity.id))
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
