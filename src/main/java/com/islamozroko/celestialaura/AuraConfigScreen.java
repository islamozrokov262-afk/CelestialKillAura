package com.islamozroko.celestialaura;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class AuraConfigScreen {
    public static Screen create(Screen parent) {
        AuraConfig config = CelestialAuraMod.getConfig();
        
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal("Celestial Aura"))
                .setSavingRunnable(config::save);
        
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        
        // 1. Категория "Цели"
        ConfigCategory targets = builder.getOrCreateCategory(Component.literal("Цели"));
        targets.addEntry(entryBuilder.startBooleanToggle(Component.literal("Игроки"), config.targetPlayers)
                .setDefaultValue(true).setSaveConsumer(v -> config.targetPlayers = v).build());
        targets.addEntry(entryBuilder.startBooleanToggle(Component.literal("Мобы"), config.targetMobs)
                .setDefaultValue(false).setSaveConsumer(v -> config.targetMobs = v).build());
        targets.addEntry(entryBuilder.startStringDropdownMenu(Component.literal("Сортировка"), config.sorting)
                .setSelections(java.util.List.of("Distance", "Health", "FOV"))
                .setDefaultValue("Distance").setSaveConsumer(v -> config.sorting = v).build());

        // 2. Категория "Дистанции"
        ConfigCategory ranges = builder.getOrCreateCategory(Component.literal("Дистанции"));
        ranges.addEntry(entryBuilder.startFloatField(Component.literal("Дистанция удара"), config.range)
                .setDefaultValue(3.2f).setMin(1.0f).setMax(6.0f).setSaveConsumer(v -> config.range = v).build());
        ranges.addEntry(entryBuilder.startFloatField(Component.literal("Дистанция наводки"), config.lookRange)
                .setDefaultValue(4.5f).setMin(0.0f).setMax(32.0f).setSaveConsumer(v -> config.lookRange = v).build());

        // 3. Категория "Удары"
        ConfigCategory hits = builder.getOrCreateCategory(Component.literal("Удары"));
        hits.addEntry(entryBuilder.startBooleanToggle(Component.literal("Только криты"), config.onlyCrits)
                .setDefaultValue(true).setSaveConsumer(v -> config.onlyCrits = v).build());
        hits.addEntry(entryBuilder.startBooleanToggle(Component.literal("Бить через стены"), config.throughWalls)
                .setDefaultValue(false).setSaveConsumer(v -> config.throughWalls = v).build());
        hits.addEntry(entryBuilder.startBooleanToggle(Component.literal("Бить через щит"), config.breakShield)
                .setDefaultValue(true).setSaveConsumer(v -> config.breakShield = v).build());

        // 4. Категория "Ротация"
        ConfigCategory rotation = builder.getOrCreateCategory(Component.literal("Ротация"));
        rotation.addEntry(entryBuilder.startBooleanToggle(Component.literal("Silent Rotation"), config.silentRotation)
                .setDefaultValue(true).setSaveConsumer(v -> config.silentRotation = v).build());
        rotation.addEntry(entryBuilder.startStringDropdownMenu(Component.literal("Режим"), config.rotationMode)
                .setSelections(java.util.List.of("Matrix", "Snap", "Funtime"))
                .setDefaultValue("Matrix").setSaveConsumer(v -> config.rotationMode = v).build());
        rotation.addEntry(entryBuilder.startFloatField(Component.literal("Скорость Yaw"), config.yawSpeed)
                .setDefaultValue(40.0f).setMin(10.0f).setMax(180.0f).setSaveConsumer(v -> config.yawSpeed = v).build());
        rotation.addEntry(entryBuilder.startFloatField(Component.literal("Скорость Pitch"), config.pitchSpeed)
                .setDefaultValue(20.0f).setMin(10.0f).setMax(180.0f).setSaveConsumer(v -> config.pitchSpeed = v).build());
        
        return builder.build();
    }
}
