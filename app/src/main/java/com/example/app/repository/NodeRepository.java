package com.example.app.repository;

import com.example.app.domain.AppUser;
import com.example.app.domain.FileDB;
import com.example.app.domain.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface NodeRepository extends JpaRepository<Node, Long> {
    Optional<Node> findById(Long id);
    Node findByNameIgnoreCaseOrderById(String name);
    void deleteByNameIgnoreCase(String name);
    void deleteById(Long id);
    List<Node> findByScriptId(Long file_id);
    void deleteByScriptId(long file_id);




}
