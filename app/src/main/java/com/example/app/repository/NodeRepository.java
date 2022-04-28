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

    @Query(value = "BEGIN;\n" +
            "UPDATE node AS t\n" +
            "SET january = c.january ,february=c.february,march=c.march,april=c.april,\n" +
            "may=c.may,june= c.june, july=c.july, august=c.august,september=c.september, \n" +
            "october=c.october,november=c.november, december=c.december \n" +
            "FROM (\n" +
            "WITH RECURSIVE cte AS(\n" +
            "SELECT t1.id, t1.name, t1.january,t1.february,t1.march,t1.april,t1.may,t1.june,t1.july,t1.august,t1.september,t1.october,t1.november,t1.december, t1.parent_id\n" +
            "FROM node t1\n" +
            "WHERE NOT EXISTS (SELECT 1 FROM node t2 WHERE t2.parent_id = t1.id)\n" +
            "UNION ALL\n" +
            "SELECT t.id, t.name, c.january,c.february,c.march,c.april,c.may,c.june,c.july,c.august,c.september,c.october,c.november,c.december, t.parent_id\n" +
            "FROM node t INNER JOIN cte c \n" +
            "ON c.parent_id = t.id\n" +
            ")\n" +
            "SELECT id, SUM(january) january,SUM(february) february,SUM(march) march,\n" +
            "SUM(april) april,SUM(may) may,SUM(june) june,SUM(july) july,SUM(august) august,\n" +
            "SUM(september) september,SUM(october) october,SUM(november) november,SUM(december) december \n" +
            "FROM cte\n" +
            "GROUP BY id)\n" +
            "c \n" +
            "WHERE c.id = t.id;\n"+
            "\n" +
            "update node as n\n" +
            "set \"value\" = t1.somme\n" +
            "from \n" +
            "(select id, sum(january+february+march+april+may+june+july+august+september+october+november+december) as somme\n" +
            "from node\n" +
            "group by id\n" +
            "order by id) as t1\n" +
            "where n.id=t1.id;\n" +
            "---\n" +
            "COMMIT;",nativeQuery = true)
    void calculateSum();

    @Query(value="SELECT january,february,march,april,may,june,july,august,september,october,november,december FROM currency_rate",nativeQuery = true)
    List<List<Double>> getRates();



}
