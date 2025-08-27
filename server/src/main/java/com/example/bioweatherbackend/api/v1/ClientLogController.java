package com.example.bioweatherbackend.api.v1;

import com.example.bioweatherbackend.repository.DeviceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/client-logs")
@Slf4j
public class ClientLogController {

    private final String uploadClientLogsPath;
    private final DeviceRepository deviceRepository;

    @Autowired
    public ClientLogController(@Value("${upload.client-logs-path}") String uploadClientLogsPath, DeviceRepository deviceRepository) {
        this.uploadClientLogsPath = uploadClientLogsPath;
        this.deviceRepository = deviceRepository;
    }

    @PostMapping()
    public ResponseEntity<Void> uploadFiles(@RequestParam("file") MultipartFile[] files, @RequestParam("pushToken") String pushToken) {
        if (!deviceRepository.existsById(pushToken)) {
            log.error("Push token not found {}", pushToken);
            return ResponseEntity.internalServerError().build();
        }

        var uploadDir = new File(uploadClientLogsPath.trim(), pushToken);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        try {
            saveFilesToDisk(files, uploadDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{pushToken}")
    public ResponseEntity<String> getLogs(@PathVariable("pushToken") String pushToken) {
        if (!deviceRepository.existsById(pushToken)) {
            log.error("Push token not found {}", pushToken);
            return ResponseEntity.internalServerError().build();
        }

        var uploadDir = new File(uploadClientLogsPath.trim(), pushToken);
        if (!uploadDir.exists() || !uploadDir.isDirectory()) {
            log.error("Upload directory does not exist or is not a directory: {}", uploadDir.getAbsolutePath());
            return ResponseEntity.notFound().build();
        }

        StringBuilder response = new StringBuilder();
        File[] files = uploadDir.listFiles();
        if (files!=null) {
            Arrays.stream(files).filter(File::isFile).sorted(Comparator.comparing(File::getName)).filter(file -> file.getName().endsWith(".log")).forEach(file -> {
                try {
                    response.append(Files.readString(file.toPath()));
                } catch (IOException e) {
                    log.error("Error reading file: {}, skipping", file.getName(), e);
                }
            });
        } else {
            log.error("Failed to list files in directory: {}", uploadDir.getAbsolutePath());
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().body(response.toString());
    }

    private void saveFilesToDisk(MultipartFile[] files, File uploadDir) throws IOException {
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                File dest = new File(uploadDir, Objects.requireNonNull(file.getOriginalFilename()));
                file.transferTo(dest);
            }
        }
    }

}
