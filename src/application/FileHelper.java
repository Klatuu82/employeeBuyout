package application;

import java.nio.file.Path;
import java.nio.file.Paths;

class FileHelper {

    static Path getMitarbeiterCSV(String fileName) {
        String nameHomeVerzeichnis = System.getProperty("user.dir");
        return Paths.get(nameHomeVerzeichnis, fileName);
    }
}
