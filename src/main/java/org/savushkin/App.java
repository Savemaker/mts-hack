package org.savushkin;


import org.rauschig.jarchivelib.ArchiveFormat;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Lets hack (kinda) this bitch ethically (just unziping and unraring lmao)
 *
 * @author ssavushkin
 * @author jarchivelib library on top of apache commons unrar and unzip https://github.com/thrau/jarchivelib.git
 */
public class App {
    static Archiver tarArchiver = ArchiverFactory.createArchiver(ArchiveFormat.TAR);
    static Archiver zipArchiver = ArchiverFactory.createArchiver(ArchiveFormat.ZIP);

    private static String getFileNameWithoutType(String name) {
        StringBuilder stringBuilder = new StringBuilder(name);
        return stringBuilder.delete(name.length() - 4, name.length()).toString();
    }

    public static void main(String[] args) throws IOException {
        showFile(Path.of(".").toRealPath().toFile().listFiles()[0]);

    }

    public static void showFile(File file) throws IOException {
        if (!file.toString().contains(".zip") && !file.toString().contains(".tar")) {
            for (File f : file.listFiles()) {
                System.out.println(f);
            }
        } else {
            if (file.toString().contains(".zip")) {
                zipArchiver.extract(file, file.getParentFile());
            } else if (file.toString().contains(".tar")) {
                tarArchiver.extract(file, file.getParentFile());
            } else
                return;
            for (File t : file.getParentFile().listFiles()) {
                if (t.getName().contentEquals(getFileNameWithoutType(file.getName()))) {
                    for (File ts : t.listFiles()) {
                        if (ts.getName().contains(".zip") || ts.getName().contains(".tar")) {
                            Files.move(ts.toPath(), Path.of(".").toRealPath().resolve(ts.toPath().getFileName()));
                            showFile(Path.of(".").toRealPath().resolve(ts.toPath().getFileName()).toFile());
                        }
                    }
                }
            }
        }
    }
}
