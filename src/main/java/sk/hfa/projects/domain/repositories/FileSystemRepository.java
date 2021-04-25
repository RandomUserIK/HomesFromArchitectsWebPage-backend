package sk.hfa.projects.domain.repositories;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Repository
public class FileSystemRepository {

    private static final String RESOURCES_DIR = FileSystemRepository.class.getResource("/").getPath();

    // TODO:
    public String save(byte[] content, String imageName) throws IOException { // NOSONAR
        Path newFile = Paths.get(RESOURCES_DIR + new Date().getTime() + "-" + imageName); // NOSONAR
        Files.createDirectories(newFile.getParent()); // NOSONAR
        Files.write(newFile, content); // NOSONAR
        return newFile.toAbsolutePath().toString();
    }

    public FileSystemResource findInFileSystem(String location) {
        return new FileSystemResource(Paths.get(location));
    }

}
