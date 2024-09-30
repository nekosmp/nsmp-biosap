// Copyright 2024 Atakku <https://atakku.dev>
//
// This project is dual licensed under MIT and Apache.

package rs.neko.smp.biosap;

import static net.minecraft.server.command.CommandManager.literal;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BioSap implements DedicatedServerModInitializer {
  public static final Logger LOGGER = LoggerFactory.getLogger("nsmp-biosap");

  @Override
  public void onInitializeServer() {
    LOGGER.info("Initializing NSMP BioSap");
    BioSapConfig.getConfig();
    CommandRegistrationCallback.EVENT.register((d, r, e) -> {
      d.register(literal("nsmp-biosap").then(literal("reload").requires(s -> s.hasPermissionLevel(2)).executes(ctx -> {
        BioSapConfig.loadData();
        return 1;
      })));
    });
  }
}
