package com.example.app.service;

import com.example.app.domain.FileDB;
import com.example.app.domain.FileDBParams;
import com.example.app.domain.Node;
import com.example.app.repository.FileDBRepository;
import com.example.app.repository.NodeRepository;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

@Service @RequiredArgsConstructor @Transactional
@Slf4j

public class FileDBServiceImpl implements FileDBService{
    @Autowired
    private FileDBRepository fileDBRepository;

    @Autowired
    private NodeRepository nodeRepository;

    @Override
    public FileDB saveFile(MultipartFile file) throws IOException {

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            FileDB fileDB = new FileDB(fileName,file.getContentType(),file.getBytes());

            return fileDBRepository.save(fileDB);


    }

    @Override
    public FileDB getFile(Long id) {
        return fileDBRepository.findById(id).get();
    }

    @Override
    public Stream<FileDB> getAllFiles() {
        return fileDBRepository.findAll().stream();
    }

    @Override
    public void deleteFile(Long id) {
        fileDBRepository.deleteById(id);
    }

    @Override
    public Stream<FileDBParams> getAllFilesWithoutData() {
        return fileDBRepository.loadFilesWithoutData();
    }

    @Override
    public FileDB loadFileByNodeId(long nodeId) {
        return fileDBRepository.loadFileByNodeId(nodeId);
    };

    @Override
    public void executeScript(long id, Binding binding) {
        try {
            GroovyShell gs = new GroovyShell( binding );
            FileDB file = fileDBRepository.findById(id);
            gs.evaluate(new String(file.getData(), StandardCharsets.UTF_8));
        }
        catch (Exception e) {log.warn("{}",e.getMessage());}

    }
}
