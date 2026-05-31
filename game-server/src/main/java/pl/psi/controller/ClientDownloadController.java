package pl.psi.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/api")
public class ClientDownloadController {

   private static final String CLIENT_ZIP_PATH = "/app/downloads/game-client.zip";

    @GetMapping("/download-client")
    public ResponseEntity<Resource> downloadClient() {
        File file = new File(CLIENT_ZIP_PATH);

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"game-client.zip\"")
                .contentType(MediaType.parseMediaType("application/zip"))
                .contentLength(file.length())
                .body(resource);
    }
}