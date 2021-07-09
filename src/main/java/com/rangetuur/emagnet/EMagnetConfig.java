package com.rangetuur.emagnet;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.io.File;

public class EMagnetConfig {
    public static class ServerConfig{

        private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        public static final ForgeConfigSpec SPEC;

        public static ForgeConfigSpec.BooleanValue magnet_always_works;
        public static ForgeConfigSpec.IntValue capacity_basic_magnet;
        public static ForgeConfigSpec.IntValue range_basic_magnet;
        public static ForgeConfigSpec.IntValue capacity_advanced_magnet;
        public static ForgeConfigSpec.IntValue range_advanced_magnet;
        public static ForgeConfigSpec.BooleanValue disable_magnet_jar;
        public static ForgeConfigSpec.BooleanValue disable_magnet_jar_with_redstone;


        static{
            BUILDER.push("Magnets");
            magnet_always_works = BUILDER
                    .comment("If false the magnet only work when holding in your hands else if true the magnets works when in inventory")
                    .define("magnet_always_works", false);
            BUILDER.pop();

            BUILDER.push("Range magnets");
            range_basic_magnet = BUILDER
                    .comment("This value determines how big the range of the basic magnet is.")
                    .defineInRange("range_basic_magnet",6,0,32);
            range_advanced_magnet = BUILDER
                    .comment("This value determines how big the range of the advanced magnet is.")
                    .defineInRange("range_advanced_magnet",8,0,32);
            BUILDER.pop();

            BUILDER.push("Capacity magnets");
            capacity_basic_magnet = BUILDER
                    .comment("This value determines how big the capacity of the basic magnet is.")
                    .defineInRange("capacity_basic_magnet",10000,0,10000000);
            capacity_advanced_magnet = BUILDER
                    .comment("This value determines how big the capacity of the advanced magnet is.")
                    .defineInRange("capacity_advanced_magnet",30000,0,10000000);
            BUILDER.pop();

            BUILDER.push("Blocks");

            disable_magnet_jar = BUILDER
                    .comment("If false the magnet jar is not disabled else if true the magnet jar is disabled")
                    .define("disable_magnet_jar", false);
            /*
            disable_magnet_jar_with_redstone = BUILDER
                    .comment("If false the magnet jar can not be disabled with redstone else if true the magnet jar can be disabled with redstone (when the magnet jar is disabled it can't attract items anymore but it can still charge)")
                    .define("disable_magnet_jar_with_redstone", true);
             */

            BUILDER.pop();

            SPEC=BUILDER.build();
        }
    }

    public static void loadConfig(ForgeConfigSpec config, String path){
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        config.setConfig(file);
    }
}
