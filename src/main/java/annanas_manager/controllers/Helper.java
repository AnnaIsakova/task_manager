package annanas_manager.controllers;


import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import static annanas_manager.services.impl.FilesForProjectServiceImpl.DIR_PATH;

public class Helper {

    public static ResponseEntity<Resource> getFile(String fileName, long timeMarker) throws IOException {

        File file = new File(DIR_PATH + timeMarker + "-" + fileName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        String type = Files.probeContentType(file.toPath());
        HttpHeaders headers = new HttpHeaders();
        headers.set("File-Name", fileName);
        headers.setContentType(MediaType.parseMediaType(type));
        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);

    }
}
