package io.redspace.irons_artifice.entity;

import io.redspace.irons_artifice.mixin.MobAccessor;
import io.redspace.irons_artifice.modifier.ModifierItem;
import io.redspace.irons_artifice.registry.ItemRegistry;
import io.redspace.irons_artifice.registry.SoundRegistry;
import io.redspace.irons_artifice.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.zombie.Drowned;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;

import java.util.List;
import java.util.Optional;

public class DrownedPirateHelper {

    public static void trySpawnPirates(ServerLevel level, LivingEntity target, Vec3 center) {
        float distance = 18;
        for (int i = 0; i < 18; i++) {
            Vec3 pos = center.add(new Vec3(0, 0, distance).yRot(i * 60 * Mth.DEG_TO_RAD)).add(Utils.randomVec3(3));
            BlockPos heightSamplePos = BlockPos.containing(pos);
            int waterHeight = level.getHeight(Heightmap.Types.WORLD_SURFACE, heightSamplePos);
            if (pos.y > waterHeight - 3) {
                int y = (int) Math.min((pos.y + pos.y + level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, heightSamplePos)) / 3, waterHeight - 5);
                pos = new Vec3(pos.x, y, pos.z);
            }
            AABB box = AABB.ofSize(pos, 5, 3, 5);
            if (level.isFluidAtPosition(BlockPos.containing(pos), f -> f.is(Tags.Fluids.WATER)) && level.noCollision(box)) {
                for (int j = 0; j < 3; j++) {
                    Drowned pirate = DrownedPirateHelper.createDrownedPirate(level);
                    pirate.setPos(pos.add(Utils.randomVec3(3)));
                    pirate.setTarget(target);

                    // TODO: not available in 1.21.1
                    // ZombieNautilus does not exist in vanilla 1.21.1, so this feature is disabled.
                    // The pirate will simply spawn as a Drowned instead.
                    level.addFreshEntity(pirate);
                }
                level.playSound(null, BlockPos.containing(pos), SoundRegistry.PIRATE_AMBUSH.get(), SoundSource.NEUTRAL, 2.5f, 1);
                break;
            }
        }
    }

    public static Drowned createDrownedPirate(ServerLevel level) {
        Drowned drowned = new Drowned(EntityType.DROWNED, level);
        drowned.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ItemRegistry.TRICORNE_HAT.get()));
        drowned.setItemSlot(EquipmentSlot.MAINHAND, createLoadout(level));
        return drowned;
    }

    public static ItemStack createLoadout(ServerLevel level) {
        return new ItemStack(ItemRegistry.FLINTLOCK_PISTOL.get());
    }
}
