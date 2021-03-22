package sk.hfa.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sk.hfa.databases.projects.domains.IndividualProject;
import sk.hfa.databases.projects.domains.repositories.FileSystemRepository;
import sk.hfa.databases.projects.domains.repositories.IndividualProjectRepository;

import java.io.InputStream;

@Controller
@RequestMapping(path = "/photo")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class PhotoController {

    @Autowired
    private FileSystemRepository fileSystemRepository;

    @Autowired
    private IndividualProjectRepository individualProjectRepository;


    @PostMapping(path = "/upload")
    @ResponseBody
    String upload(@RequestParam("file") MultipartFile file, @RequestParam("projectId") String projectId) throws Exception {
        String saveFilePath = "";
        IndividualProject individualProject = individualProjectRepository.findById(Long.valueOf(projectId)).orElse(null);
        if (individualProject != null) {
            saveFilePath = fileSystemRepository.save(file.getBytes(), file.getOriginalFilename());
            individualProject.getPhotoPaths().add(saveFilePath);
            individualProjectRepository.save(individualProject);
            log.info("Photo ["+file.getOriginalFilename()+"] successfully uploaded");
        }
        return saveFilePath;
    }


    @PostMapping(path = "/preview", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    ResponseEntity<?> getImage(@RequestParam("path") String path) {
        try {
            FileSystemResource fileSystemResource = fileSystemRepository.findInFileSystem(path);
            String filePath = fileSystemResource.getPath();
            if (fileSystemResource.getFile().exists()) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + (fileSystemResource.getFilename()));
                InputStream inputStream = fileSystemResource.getInputStream();
                return ResponseEntity
                        .ok()
                        .headers(headers)
                        .body(new InputStreamResource(inputStream));
            } else {
                log.warn("Photo not found. Path :" + filePath);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
