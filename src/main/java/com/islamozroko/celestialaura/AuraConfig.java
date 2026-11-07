package com.islamozroko.celestialaura;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AuraConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("celestial-aura.json").toFile();

    // Цели
    public boolean targetPlayers = true;
    public boolean targetMobs = false;
    public String sorting = "Distance"; // Distance, Health, FOV

    // Дистанции
    public float range = 3.2f;
    public float lookRange = 4.5f;

    // Удары
    public boolean onlyCrits = true;
    public boolean throughWalls = false;
    public boolean breakShield = true;

    // Ротация
    public boolean silentRotation = true;
    public String rotationMode = "Matrix"; // Matrix, Snap, Funtime
    public float yawSpeed = 40.0f;
    public float pitchSpeed = 20.0f;

    public static AuraConfig load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                AuraConfig config = GSON.fromJson(reader, AuraConfig.class);
                if (config != null) return config;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        AuraConfig config = new AuraConfig();
        config.save();
        return config;
    }

    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
