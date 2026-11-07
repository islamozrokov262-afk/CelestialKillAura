package com.islamozroko.celestialaura;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class CelestialAuraMod implements ClientModInitializer {
    private static AuraConfig config;
    private static KeyMapping menuKey;
    private static KeyMapping toggleKey; // Новая кнопка
    private static boolean enabled = true;

    public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("celestialaura", "main")
    );

    @Override
    public void onInitializeClient() {
        config = AuraConfig.load();

        // Кнопка открытия меню (R SHIFT)
        menuKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.celestialaura.menu",
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_RSHIFT,
                CATEGORY
        ));

        // Кнопка Вкл/Выкл (R)
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.celestialaura.toggle",
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_R,
                CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.level == null) return;

            // Открытие меню
            while (menuKey.consumeClick()) {
                client.setScreen(AuraConfigScreen.create(client.screen));
            }

            // Переключение Вкл/Выкл
            while (toggleKey.consumeClick()) {
                enabled = !enabled;
                client.player.displayClientMessage(
                        Component.literal("Aura: " + (enabled ? "§aON" : "§cOFF")), true
                );
            }

            if (!enabled) return;

            Entity target = findTarget(client);
            if (target != null) {
                RotationManager.updateRotations(target, config);
                
                if (client.player.getAttackStrengthScale(0.5f) >= 0.95f) {
                    client.gameMode.attack(client.player, target);
                    client.player.swing(InteractionHand.MAIN_HAND);
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
