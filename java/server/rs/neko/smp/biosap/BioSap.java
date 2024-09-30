// Copyright 2024 Atakku <https://atakku.dev>
//
// This project is dual licensed under MIT and Apache.

package rs.neko.smp.biosap;

import net.fabricmc.api.DedicatedServerModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BioSap implements DedicatedServerModInitializer {
  public static final Logger LOGGER = LoggerFactory.getLogger("nsmp-biosap");

  @Override
  public void onInitializeServer() {
    LOGGER.info("Initializing NSMP BioSap");
  }
}
