/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.openhome.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;

/**
 *
 * @author Justin
 */
public class IOUtil {

    public static void writeTop(Path path, String data) {
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
        return new int[]{Integer.parseInt(topData[0]), Integer.parseInt(topData[1]), Integer.parseInt(topData[2])};
    }

    public static Player player(URL source) {
        Player player = null;
        try {
            player = Manager.createPlayer(source);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        } catch (NoPlayerException ex) {
            throw new RuntimeException(ex);
        }
        return player;
    }
}
