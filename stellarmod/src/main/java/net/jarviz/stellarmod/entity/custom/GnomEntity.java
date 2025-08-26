package net.jarviz.stellarmod.entity.custom;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import net.minecraftforge.common.ForgeMod;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.item.ItemStack;

public class GnomEntity extends PathfinderMob implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean wasMoving;
    private boolean riderSprinting;
    private ItemStack savedMain;
    private ItemStack savedOff;

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation RUN  = RawAnimation.begin().thenLoop("run");

    public GnomEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AttributeSupplier.builder()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.22D)
                .add(ForgeMod.ENTITY_GRAVITY.get(), 0.08D)
                .add(Attributes.FOLLOW_RANGE, 16.0D);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> {
            if (!this.isVehicle()) {
                state.getController().forceAnimationReset();
                return PlayState.STOP;
            }

            boolean geckoMoving = state.isMoving();
            float limbSwingAmount = state.getLimbSwingAmount();
            boolean movingByVel = this.getDeltaMovement().horizontalDistance() > 0.03;

            boolean inputMove = false;
            if (this.getFirstPassenger() instanceof Player p) {
                inputMove = Math.abs(p.xxa) > 0.001f || Math.abs(p.zza) > 0.001f;
            }

            boolean moving = inputMove || geckoMoving || limbSwingAmount > 0.05f || movingByVel;
            this.wasMoving = moving;
            if (moving) {
                return state.setAndContinue(this.riderSprinting ? RUN : WALK);
            }
            return state.setAndContinue(IDLE);
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public void setRiderSprinting(boolean riderSprinting) {
        this.riderSprinting = riderSprinting;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) {
            return super.mobInteract(player, hand);
        }

        if (!this.level().isClientSide) {
            boolean canMount = !this.isVehicle() && this.getPassengers().isEmpty();
            if (canMount) {
                boolean mounted = player.startRiding(this, true);
                if (mounted) {
                    player.setInvisible(true);
                    Abilities a = player.getAbilities();
                    a.mayBuild = false;
                    player.onUpdateAbilities();
                    if (savedMain == null) savedMain = player.getMainHandItem().copy();
                    if (savedOff == null) savedOff = player.getOffhandItem().copy();
                    player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                    player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                }
            }
        }
        return InteractionResult.sidedSuccess(this.level().isClientSide);
    }

    @Override
    protected void addPassenger(@NotNull Entity passenger) {
        super.addPassenger(passenger);
        if (passenger instanceof Player p) {
            p.setYRot(this.getYRot());
            p.setXRot(this.getXRot());
            p.yRotO = p.getYRot();
            p.xRotO = p.getXRot();
            p.setYHeadRot(this.getYRot());
            p.setInvisible(true);
            Abilities a = p.getAbilities();
            a.mayBuild = false;
            p.onUpdateAbilities();
            if (savedMain == null) savedMain = p.getMainHandItem().copy();
            if (savedOff == null) savedOff = p.getOffhandItem().copy();
            p.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            p.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        }
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        if (passenger instanceof Player p) {
            p.setInvisible(false);
            Abilities a = p.getAbilities();
            a.mayBuild = true;
            p.onUpdateAbilities();
            if (p.getMainHandItem().isEmpty() && savedMain != null) {
                p.setItemInHand(InteractionHand.MAIN_HAND, savedMain);
            }
            if (p.getOffhandItem().isEmpty() && savedOff != null) {
                p.setItemInHand(InteractionHand.OFF_HAND, savedOff);
            }
            savedMain = null;
            savedOff = null;
        }
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().isEmpty();
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public double getPassengersRidingOffset() {
        return this.getEyeHeight() + 0.10D;
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (this.isVehicle() && this.getFirstPassenger() instanceof Player p) {
            this.setYRot(p.getYRot());
            this.yRotO = this.getYRot();
            this.setXRot(p.getXRot() * 0.5F);
            this.setRot(this.getYRot(), this.getXRot());
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.getYRot();

            float strafe = p.xxa;
            float forward = p.zza;
            float base = (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
            float speedMul = this.riderSprinting ? 0.90F : 0.45F;
            this.setSpeed(base * speedMul);
            super.travel(new Vec3(strafe, travelVector.y, forward));
            return;
        }
        this.riderSprinting = false;
        super.travel(travelVector);
    }
}
