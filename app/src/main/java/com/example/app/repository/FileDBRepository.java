package com.example.app.repository;

import com.example.app.domain.FileDB;
import com.example.app.domain.FileDBParams;
import com.example.app.domain.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Stream;


@Transactional
public interface FileDBRepository extends JpaRepository<FileDB,Long> {
    @Query(
            value = "SELECT node.id as node_id, script.id, script.name, script.type\n" +
                    "    FROM node LEFT OUTER JOIN script ON (node.script_id = script.id);\n" +
                    "\t\n" +
                    "\t",
            nativeQuery = true)
    Stream<FileDBParams> loadFilesWithoutData();

    @Query(
            value = "SELECT id,name,type FROM script WHERE name = :filename",
            nativeQuery = true)
    FileDBParams findByNameIgnoreCase(@Param("filename")String fileName);

    @Query(
            value = "SELECT * FROM script WHERE name = :filename",
            nativeQuery = true)
    FileDB findByName(@Param("filename")String fileName);

    @Query(value = "SELECT node.id as node_id, script.id, script.name, script.type, script.data" +
            " FROM node LEFT OUTER JOIN script ON (node.script_id = script.id)" +
            "WHERE node.id = :nodeId",
    nativeQuery = true)
    FileDB loadFileByNodeId(@Param("nodeId")long nodeId);
    FileDB findById(long id);
}
