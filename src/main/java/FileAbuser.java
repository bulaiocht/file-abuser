import lombok.Data;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class FileAbuser extends SimpleFileVisitor<Path> {

    private static final String NAME_REGEX = ".*.";

    private List<String> extensions;

    private long threshold;

    private Map<String, List<Path>> fileMap;

    public FileAbuser(List<String> extensions, long sizeThreshold){
        this.extensions = extensions;
        this.threshold = sizeThreshold;
        this.fileMap = initMap(extensions);
    }

    private final List<String> listOfPaths = new ArrayList<>();

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        return super.preVisitDirectory(dir, attrs);
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

        String fileName = file.getFileName().toString();

        long size = attrs.size();

        for (String suffix : extensions) {
            if (fileName.matches(NAME_REGEX + suffix) && size >= threshold){
                listOfPaths.add(fileName + " " + file.toFile().getAbsolutePath());
                fileMap.get(suffix).add(file);
            }
        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return super.visitFileFailed(file, exc);
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return super.postVisitDirectory(dir, exc);
    }


    private Map<String, List<Path>> initMap(List<String> keys){

        final HashMap<String, List<Path>> map = new HashMap<>();

        keys.stream()
                .distinct().forEach(key -> map.put(key, new ArrayList<>()));
        return map;
    }
}
