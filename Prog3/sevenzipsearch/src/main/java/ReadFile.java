import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;


public class ReadFile {

    public static void main(String[] args) throws IOException {
        String filename = args[0];
        String word = args[1];

        try (SevenZFile archiveFile = new SevenZFile(new File(filename))) {
            SevenZArchiveEntry entry;

            while ((entry = archiveFile.getNextEntry()) != null) {
                String name = entry.getName();
                if (name.contains(".txt")) {
                    System.out.println(name);
                    ByteArrayOutputStream contentBytes = new ByteArrayOutputStream();
                    byte[] buffer = new byte[2048];
                    int bytesRead;

                    while ((bytesRead = archiveFile.read(buffer)) != -1) {
                        contentBytes.write(buffer, 0, bytesRead);
                    }
                    String content = contentBytes.toString(StandardCharsets.UTF_8);

                    Scanner scanner = new Scanner(content);
                    int lineNum = 1;
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        if(line.toLowerCase().contains(word.toLowerCase())) {
                            String line2 = line.replaceAll("(?i)"+word,word.toUpperCase());
                            System.out.printf("%d: %s\n", lineNum, line2);
                        }
                        lineNum++;
                    }
                    scanner.close();
                    System.out.println();
                }
            }
        }
    }
}
