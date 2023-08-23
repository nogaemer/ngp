package de.nogaemer.ngp.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public class Reset {
    public static void reset(File world){

        try {
            Files.walk(world.toPath())
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);

            world.mkdir();

            new File(world, "data").mkdir();
            new File(world, "datapacks").mkdir();
            new File(world, "playerdata").mkdir();
            new File(world, "poi").mkdir();
            new File(world, "region").mkdir();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
