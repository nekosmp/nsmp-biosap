// Copyright 2024 Atakku <https://atakku.dev>
//
// This project is dual licensed under MIT and Apache.

package rs.neko.smp.biosap;

import java.nio.file.Files;
import java.nio.file.Path;

import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.loader.api.FabricLoader;

public class BioSapConfig {
  private static final Gson GSON = new GsonBuilder().registerTypeAdapter(Identifier.class, new Identifier.Serializer()).setPrettyPrinting().create();

  private static Object2ObjectOpenHashMap<Identifier, Object2ObjectOpenHashMap<Identifier, Int2ObjectOpenHashMap<Identifier>>> data = null;

  public static Object2ObjectOpenHashMap<Identifier, Object2ObjectOpenHashMap<Identifier, Int2ObjectOpenHashMap<Identifier>>> getConfig() {
    if (data == null) {
      loadData();
      saveData();
    }
    return data;
  }

  private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("nsmp-biosap.json");

  public static void loadData() {
    String json = "{}";
    try {
      json = Files.readString(PATH);
      data = JsonHelper.deserialize(GSON, json, new TypeToken<Object2ObjectOpenHashMap<Identifier, Object2ObjectOpenHashMap<Identifier, Int2ObjectOpenHashMap<Identifier>>>>() {
      });
    } catch (Exception ex) {
      BioSap.LOGGER.warn("Failed to load json from {}: {}", PATH, ex);
      data = new Object2ObjectOpenHashMap<Identifier, Object2ObjectOpenHashMap<Identifier, Int2ObjectOpenHashMap<Identifier>>>();
    }
  }

  public static void saveData() {
    try {
      Files.writeString(PATH, GSON.toJson(data));
    } catch (Exception ex) {
      BioSap.LOGGER.warn("Failed to save json to {}: {}", PATH, ex);
    }
  }

  public static Identifier getFeature(Identifier biome, Identifier sapling, int r) {
    Object2ObjectOpenHashMap<Identifier, Int2ObjectOpenHashMap<Identifier>> local_saplings = getConfig().get(biome);
    if (local_saplings == null)
      return null;
    Int2ObjectOpenHashMap<Identifier> sapling_sizes = local_saplings.get(sapling);
    if (sapling_sizes == null)
      return null;
    return sapling_sizes.get(r);
  }
}
