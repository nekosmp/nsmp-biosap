// Copyright 2024 Atakku <https://atakku.dev>
//
// This project is dual licensed under MIT and Apache.

package rs.neko.smp.biosap.mixin;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SaplingGenerator.class)
public abstract class MixinSaplingGenerator {
  @Invoker(value = "areFlowersNearby")
  abstract boolean isNearFlowers(WorldAccess w, BlockPos p);

  @Inject(at = @At(value = "HEAD"), method = "generate(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/random/Random;)Z", cancellable = true)
  void generate(ServerWorld w, ChunkGenerator g, BlockPos p, BlockState s, Random r,
      CallbackInfoReturnable<Boolean> c) {
    System.out.println("SaplingGenerator called");
    if (isNearFlowers(w, p)) {
      
    }
    c.cancel();
  }
}