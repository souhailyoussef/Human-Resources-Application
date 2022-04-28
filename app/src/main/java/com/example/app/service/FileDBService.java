package com.example.app.service;

import com.example.app.domain.FileDB;
import com.example.app.domain.FileDBParams;
import groovy.lang.Binding;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;



public interface FileDBService {
     FileDB saveFile(MultipartFile file) throws IOException;
     FileDB getFile(Long id);
     Stream<FileDB> getAllFiles();
     void deleteFile(Long id);
     Stream<FileDBParams> getAllFilesWithoutData();
     FileDB loadFileByNodeId(long nodeId);
     void executeScript(long id, Binding binding);
     List<FileDB> getScripts();

}
