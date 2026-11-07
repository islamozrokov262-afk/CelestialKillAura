package com.islamozroko.celestialaura;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class CelestialAuraMod implements ClientModInitializer {
    private static AuraConfig config;
    private static KeyMapping menuKey;
    private static boolean enabled = true;

    public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("celestialaura", "main")
    );

    @Override
    public void onInitializeClient() {
        config = AuraConfig.load();

        // Кнопка для открытия меню (по умолчанию R SHIFT)
        menuKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.celestialaura.menu",
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_RSHIFT,
                CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.level == null) return;

            // Открытие меню
            while (menuKey.consumeClick()) {
                client.setScreen(AuraConfigScreen.create(client.screen));
            }

            if (!enabled) return;

            Entity target = findTarget(client);
            if (target != null) {
                RotationManager.updateRotations(target, config);
                
                // Логика удара (проверка кулдауна и критов)
                if (client.player.getAttackStrengthScale(0.5f) >= 0.95f) {
                    client.interactionManager.attackEntity(client.player, target);
                    client.player.swing(net.minecraft.world.InteractionHand.MAIN_HAND);
                }
            }
        });
    }

    private Entity findTarget(Minecraft mc) {
        Entity best = null;
        double bestDist = config.range;
        for (Entity entity : mc.level.entitiesForRendering()) {
            if (entity == mc.player || !entity.isAlive()) continue;
            if (entity instanceof Player && config.targetPlayers) {
                // Цель игрок
            } else if (entity instanceof LivingEntity && config.targetMobs) {
                // Цель моб
            } else continue;

            double dist = mc.player.distanceTo(entity);
            if (dist < bestDist) {
                bestDist = dist;
                best = entity;
            }
        }
        return best;
    }

    public static AuraConfig getConfig() {
        return config;
    }
}
