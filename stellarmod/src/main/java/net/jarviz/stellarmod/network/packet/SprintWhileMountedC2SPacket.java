package net.jarviz.stellarmod.network.packet;

import net.jarviz.stellarmod.entity.custom.GnomEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SprintWhileMountedC2SPacket {
    private final boolean sprinting;

    public SprintWhileMountedC2SPacket(boolean sprinting) {
        this.sprinting = sprinting;
    }

    public static void encode(SprintWhileMountedC2SPacket msg, net.minecraft.network.FriendlyByteBuf buf) {
        buf.writeBoolean(msg.sprinting);
    }

    public static SprintWhileMountedC2SPacket decode(net.minecraft.network.FriendlyByteBuf buf) {
        return new SprintWhileMountedC2SPacket(buf.readBoolean());
    }

    public static void handle(SprintWhileMountedC2SPacket msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            var sender = ctx.getSender();
            if (sender != null && sender.getVehicle() instanceof GnomEntity gnom) {
                gnom.setRiderSprinting(msg.sprinting);
            }
        });
        ctx.setPacketHandled(true);
    }
}
