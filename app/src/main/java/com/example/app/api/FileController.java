package com.example.app.api;


import com.example.app.domain.FileDB;
import com.example.app.domain.FileDBParams;
import com.example.app.domain.Node;
import com.example.app.repository.FileDBRepository;
import com.example.app.repository.NodeRepository;
import com.example.app.service.FileDBService;
import com.example.app.service.NodeService;
import com.google.common.io.Files;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@RestController @Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api" )
@CrossOrigin(origins = "http://localhost:4200")

//TODO : when removing script, don't delete orphan nodes

public class FileController {

    @Autowired
    private FileDBService fileDBService;
    @Autowired
    private NodeRepository nodeRepository;
    @Autowired
    private FileDBRepository fileDBRepository;

    @Autowired
    private NodeService nodeService;



    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file")MultipartFile file) {
        String message ="";
        if (!checkGroovyExtension(file)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseMessage("illegal script extension"));
        try {
            FileDB saved_file = fileDBService.saveFile(file);

            message = "Uploaded the file successfully" + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));

        }
        catch (IllegalArgumentException e) {
            message = "script file must be in .groovy extension!";
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseMessage(message));
        }
        catch (DataIntegrityViolationException e) {
            message = "script name " + file.getOriginalFilename() +"already used! choose another script name!";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
        }
        catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @PostMapping("/upload/{id}")
    public ResponseEntity<ResponseMessage> addFileToNode(@RequestParam("file") MultipartFile file, @PathVariable("id") Long id) {
        if (!checkGroovyExtension(file)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseMessage("illegal script extension"));
        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            FileDBParams existing_file = fileDBRepository.findByNameIgnoreCase(fileName);
            if (!nodeRepository.findById(id).get().isLeafNode()) {
                    log.info("can't change script of non leaf nodes"); //can't change script of non leaf nodes
                    return new ResponseEntity<>(new ResponseMessage("can't change script!"),HttpStatus.FORBIDDEN);
            }
            else {

                FileDB saved_file = fileDBService.saveFile(file);
                log.info("saved file id : {}", saved_file.getId());
                nodeService.addScriptToNode(id,saved_file.getId());
            }
            return new ResponseEntity<>(new ResponseMessage("successfully added script to this rubrique"),HttpStatus.OK);
        }
        catch (org.springframework.dao.DataIntegrityViolationException e) {
            var message = "script '" + file.getOriginalFilename() +"' already in use!";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
        }
        catch (Exception e){
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @GetMapping("/files")
    public ResponseEntity<List<ResponseFile>> getListFiles() {
        log.info("{}",fileDBService.getAllFilesWithoutData());
        List<ResponseFile> files = fileDBService.getAllFilesWithoutData().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/files/")
                    .path(String.valueOf(dbFile.getId()))
                    .toUriString();
            return new ResponseFile(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getId(),
                    dbFile.getNode_Id());
        }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getScript(@PathVariable Long id) {
        FileDB fileDB = fileDBService.getFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getData());
    }

    @DeleteMapping("/file/{id}")
    public ResponseEntity<ResponseMessage> deleteFile(@PathVariable("id") Long id) {
        try {
            fileDBService.deleteFile(id);
            return new ResponseEntity<>(new ResponseMessage("script successfully deleted"),HttpStatus.OK);
        }
        catch (Exception e)  {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()),HttpStatus.NOT_FOUND);
        }
    }


    public boolean checkGroovyExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename().toUpperCase();
        return fileName.endsWith(".GROOVY");
    }


}


@Data
class ResponseMessage {
    private String message;
    public ResponseMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}

@Data
 class ResponseFile {
    private String name;
    private String url;
    private String type;
    private Long id;
    private Long rubrique_id;

    public Long getRubrique_id() {
        return rubrique_id;
    }

    public ResponseFile(String name, String url, String type, Long id, Long rubrique_id) {
        this.name = name;
        this.url = url;
        this.type = type;
        this.id = id;
        this.rubrique_id=rubrique_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }


    public Long getId() {
        return id;
    }



    public void setId(Long id) {
        this.id = id;
    }


}

