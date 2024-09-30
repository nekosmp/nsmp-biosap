// Copyright 2024 Atakku <https://atakku.dev>
//
// This project is dual licensed under MIT and Apache.

package rs.neko.smp.biosap.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rs.neko.smp.biosap.BioSap;
import rs.neko.smp.biosap.BioSapConfig;

@Mixin(SaplingGenerator.class)
public abstract class MixinSaplingGenerator {
  @Invoker(value = "areFlowersNearby")
  abstract boolean isNearFlowers(WorldAccess w, BlockPos p);

  @Inject(at = @At(value = "HEAD"), method = "generate(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/random/Random;)Z", cancellable = true)
  public void generate(ServerWorld w, ChunkGenerator g, BlockPos p, BlockState s, Random r,
      CallbackInfoReturnable<Boolean> c) {
    System.out.println("SaplingGenerator called");

    if (checkAndPlace(w, g, p, s, r, 2)) {
      BioSap.LOGGER.info("Placed a 3x3 tree");
      c.setReturnValue(true);
      return;
    }
    if (checkAndPlace(w, g, p, s, r, 1)) {
      BioSap.LOGGER.info("Placed a 2x2 tree");
      c.setReturnValue(true);
      return;
    }
    if (checkAndPlace(w, g, p, s, r, 0)) {
      BioSap.LOGGER.info("Placed a single tree");
      c.setReturnValue(true);
      return;
    }

    if (isNearFlowers(w, p)) {

    }
    BioSap.LOGGER.warn("Didn't place any tree");
    c.setReturnValue(false);
  }

  private static boolean checkAndPlace(ServerWorld w, ChunkGenerator g, BlockPos p, BlockState s, Random r, int rad) {
    if (rad <= 0)
      return tryPlace(w, g, p, s, r, rad);
    Block b = s.getBlock();
    for (int ox = 0; ox >= -rad; --ox) {
      for (int oy = 0; oy >= -rad; --oy) {
        BlockPos op = p.add(ox, 0, oy);
        boolean check = true;
        for (int cx = 0; cx <= rad; cx++) {
          for (int cy = 0; cy <= rad; cy++) {
            check &= w.getBlockState(op.add(cx, 0, cy)).isOf(b);
          }
        }
        if (check && tryPlace(w, g, op, s, r, rad))
          return true;
      }
    }
    return false;
  }

  private static boolean tryPlace(ServerWorld w, ChunkGenerator g, BlockPos p, BlockState s, Random r, int rad) {
    // Remove all saplings
    for (int x = 0; x <= rad; x++) {
      for (int y = 0; y <= rad; y++) {
        w.setBlockState(p.add(x, 0, y), Blocks.AIR.getDefaultState());
      }
    }
    // Try to place the feature
    if (place(w, g, p, s, r, rad))
      return true;
    // Undo sapling removal
    for (int x = 0; x <= rad; x++) {
      for (int y = 0; y <= rad; y++) {
        w.setBlockState(p.add(x, 0, y), s);
      }
    }
    return false;
  }

  private static boolean place(ServerWorld w, ChunkGenerator g, BlockPos p, BlockState s, Random r, int rad) {
    Identifier biome = w.getBiome(p).getKey().get().getValue();
    System.out.println("gwagwa " + biome + " " + Registries.BLOCK.getId(s.getBlock()));
    Identifier feature_id = BioSapConfig.getFeature(biome, Registries.BLOCK.getId(s.getBlock()), rad);
    if (feature_id == null) {
      BioSap.LOGGER.warn("No sdasd '{}'", feature_id);
      return false;
    }
    ConfiguredFeature<?, ?> feature = w.getRegistryManager().get(RegistryKeys.CONFIGURED_FEATURE).get(feature_id);
    if (feature == null) {
      BioSap.LOGGER.warn("Failed to find feature '{}'", feature_id);
      return false;
    }
    return feature.generate(w, g, r, p);
  }

}
