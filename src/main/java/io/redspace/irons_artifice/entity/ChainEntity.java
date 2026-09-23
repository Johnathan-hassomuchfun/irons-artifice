package io.redspace.irons_artifice.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.UUID;

public class ChainEntity extends Entity {
    public static final float SPAWN_RANGE = 8f;
    public static final float BREAK_RANGE = 16f;
    public static final int DURATION = 200;
    public static final float STRENGTH = 0.05f;
    public static final int VISUAL_WARMUP_TIME = 5;

    private float maxRange, primaryStrength, secondaryStrength;
    private int duration;

    private static final EntityDataAccessor<Integer> DATA_FIRST_ID =
            SynchedEntityData.defineId(ChainEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_SECOND_ID =
            SynchedEntityData.defineId(ChainEntity.class, EntityDataSerializers.INT);

    @Nullable
    private UUID firstUuid;
    @Nullable
    private UUID secondUuid;

    public int warmup;

    public ChainEntity(EntityType<? extends ChainEntity> type, Level level) {
        super(type, level);
        this.maxRange = BREAK_RANGE;
        this.primaryStrength = STRENGTH;
        this.secondaryStrength = STRENGTH;
        this.duration = DURATION;
        this.noPhysics = true;
    }

    public ChainEntity(Level level, LivingEntity first, LivingEntity second) {
        this(EntityRegistry.CHAIN.get(), level);
        setFirst(first);
        setSecond(second);
        setPos(midpoint(first, second));
    }

    public void setMaxRange(float maxRange) {
        this.maxRange = maxRange;
    }

    public void setPrimaryStrength(float primaryStrength) {
        this.primaryStrength = primaryStrength;
    }

    public void setSecondaryStrength(float secondaryStrength) {
        this.secondaryStrength = secondaryStrength;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setFirst(LivingEntity entity) {
        this.firstUuid = entity.getUUID();
        this.entityData.set(DATA_FIRST_ID, entity.getId());
    }

    public void setSecond(LivingEntity entity) {
        this.secondUuid = entity.getUUID();
        this.entityData.set(DATA_SECOND_ID, entity.getId());
    }

    @Nullable
    public LivingEntity getFirst() {
        return resolveBound(entityData.get(DATA_FIRST_ID), firstUuid);
    }

    @Nullable
    public LivingEntity getSecond() {
        return resolveBound(entityData.get(DATA_SECOND_ID), secondUuid);
    }

    @Nullable
    private LivingEntity resolveBound(int entityId, @Nullable UUID uuid) {
        if (entityId != 0) {
            Entity entity = level().getEntity(entityId);
            if (entity instanceof LivingEntity living && !living.isRemoved()) {
                return living;
            }
        }
        if (uuid != null) {
            Entity entity = level().getEntity(uuid);
            if (entity instanceof LivingEntity living && !living.isRemoved()) {
                return living;
            }
        }
        return null;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_FIRST_ID, 0);
        builder.define(DATA_SECOND_ID, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.firstUuid = tag.hasUUID("firstUuid") ? tag.getUUID("firstUuid") : null;
        this.secondUuid = tag.hasUUID("secondUuid") ? tag.getUUID("secondUuid") : null;
        this.entityData.set(DATA_FIRST_ID, tag.getInt("firstId"));
        this.entityData.set(DATA_SECOND_ID, tag.getInt("secondId"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (firstUuid != null) {
            tag.putUUID("firstUuid", firstUuid);
        }
        if (secondUuid != null) {
            tag.putUUID("secondUuid", secondUuid);
        }
        tag.putInt("firstId", entityData.get(DATA_FIRST_ID));
        tag.putInt("secondId", entityData.get(DATA_SECOND_ID));
    }

    @Override
    public void tick() {
        super.tick();
        if (warmup < VISUAL_WARMUP_TIME) {
            warmup++;
        }
    }

    private static Vec3 midpoint(LivingEntity first, LivingEntity second) {
        return first.position().add(second.position()).scale(0.5D);
    }
}
