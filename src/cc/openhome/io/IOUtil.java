/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cc.openhome.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 *
 * @author Justin
 */
public class IOUtil {

    public static void writeTop(Path path, String data)  {
        try (final BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(data, 0, data.length());
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
    
    public static int[] readTop(Path path) {
        String[] topData;
        try {
            topData = Files.readAllLines(path).get(0).split(",");
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
        return new int[] {Integer.parseInt(topData[0]), Integer.parseInt(topData[1]), Integer.parseInt(topData[2])};
    }
    
}
