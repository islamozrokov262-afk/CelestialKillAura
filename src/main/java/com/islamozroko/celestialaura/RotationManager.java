package com.islamozroko.celestialaura;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class RotationManager {
    private static float serverYaw;
    private static float serverPitch;

    public static void updateRotations(Entity target, AuraConfig config) {
        Minecraft mc = Minecraft.getInstance();
        if (target == null || mc.player == null || mc.getConnection() == null) return;

        Vec3 eyes = mc.player.getEyePosition();
        double dx = target.getX() - eyes.x;
        double dy = (target.getY() + target.getEyeHeight() * 0.9) - eyes.y;
        double dz = target.getZ() - eyes.z;
        double dist = Math.sqrt(dx * dx + dz * dz);

        float targetYaw = (float) (Math.atan2(dz, dx) * 180.0 / Math.PI) - 90.0F;
        float targetPitch = (float) -(Math.atan2(dy, dist) * 180.0 / Math.PI);

        serverYaw = updateAngle(serverYaw, targetYaw, config.yawSpeed);
        serverPitch = updateAngle(serverPitch, targetPitch, config.pitchSpeed);

        if (config.silentRotation) {
            // Silent: отправляем пакет серверу, не меняя камеру
            mc.getConnection().send(new ServerboundMovePlayerPacket.Rot(serverYaw, serverPitch, mc.player.onGround(), false));
        } else {
            // Legit: крутим камеру на экране
            mc.player.setYRot(serverYaw);
            mc.player.setXRot(serverPitch);
        }
    }

    private static float updateAngle(float current, float target, float speed) {
        float delta = wrapDegrees(target - current);
        if (Math.abs(delta) > speed) {
            delta = Math.signum(delta) * speed;
        }
        return current + delta;
    }

    private static float wrapDegrees(float degrees) {
        degrees = degrees % 360.0F;
        if (degrees >= 180.0F) degrees -= 360.0F;
        if (degrees < -180.0F) degrees += 360.0F;
        return degrees;
    }
}
