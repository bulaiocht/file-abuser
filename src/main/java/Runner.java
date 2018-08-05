import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.RandomStringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Runner {

    public static void main(String[] args) throws IOException {

        List<String> suffixes = Arrays.asList("txt", "rar", "bin", "wav", "djvu");

        long threshold = 1024*1024;

        String sourcePath = "src/main/resources";

        shitOutFiles(suffixes, sourcePath);

        FileAbuser abuser = new FileAbuser(suffixes, threshold);

        Files.walkFileTree(Paths.get(sourcePath), abuser);

        final List<String> listOfPaths = abuser.getListOfPaths();
        final Map<String, List<Path>> fileMap = abuser.getFileMap();

        String report = "report.json";
        Path path = Paths.get(sourcePath, report);
        Files.deleteIfExists(path);
        Files.createFile(path);

        ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(path.toFile(), fileMap);

        final List<Path> txt = fileMap.get("txt");
        System.out.println(txt);


    }

    private static void shitOutFiles(List<String> suffixes, String sourcePath) {
        suffixes.forEach(suffix -> genFile(sourcePath, suffix));
    }

    private static void genFile(String sourcePath, String suffix) {

        File f = new File(sourcePath,
                String.valueOf(RandomStringUtils.randomAlphabetic(10) + "." + suffix));
        try {

            f.createNewFile();
            RandomAccessFile randomAccessFile = new RandomAccessFile(f.getAbsolutePath(), "rw");
            randomAccessFile.setLength(1024*1024 + 1);

        } catch (FileNotFoundException e ) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
