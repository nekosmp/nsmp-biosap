// Copyright 2024 Atakku <https://atakku.dev>
//
// This project is dual licensed under MIT and Apache.

package rs.neko.smp.biosap.mixin;


import net.minecraft.block.BlockState;
import net.minecraft.block.sapling.LargeTreeSaplingGenerator;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LargeTreeSaplingGenerator.class)
public abstract class MixinLargeTreeSaplingGenerator extends SaplingGenerator {
  @Inject(at = @At(value = "HEAD"), method = "generate(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/random/Random;)Z", cancellable = true)
  void generate(ServerWorld w, ChunkGenerator g, BlockPos p, BlockState s, Random r,
      CallbackInfoReturnable<Boolean> c) {
    c.setReturnValue(super.generate(w, g, p, s, r));
  }
}
