package com.tamscrap.controller;

 import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tamscrap.service.impl.StorageServiceImpl;

@Controller
@CrossOrigin(origins = {"http://localhost:4200", "https://tamscrapt.up.railway.app"})

public class FileController {

    private final StorageServiceImpl storageServiceImpl;

    public FileController(StorageServiceImpl storageServiceImpl) {
        this.storageServiceImpl = storageServiceImpl;
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename){
        Resource file = storageServiceImpl.loadAsResource(filename);
        return ResponseEntity.ok().body(file);
    }
}
