package com.example.app.api;

import com.example.app.domain.AppUser;
import com.example.app.domain.Node;
import com.example.app.repository.FileDBRepository;
import com.example.app.repository.NodeRepository;
import com.example.app.service.NodeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tools.ant.taskdefs.condition.Http;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.net.URI;
import java.util.*;
import java.util.logging.Logger;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController @Slf4j @RequiredArgsConstructor @RequestMapping(value = "/api" )
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}, allowedHeaders = "*", allowCredentials = "true")
public class NodeController {

    private final NodeService nodeService;
    private final NodeRepository nodeRepository;
    private final FileDBRepository fileDBRepository;

    @RequestMapping("/nodes")
    public ResponseEntity<Node> getNodes() {
        return ResponseEntity.ok().body(nodeService.getNode("TOTAL"));
    }

    @GetMapping(value = "/nodes/node/{nodeName}", produces = "application/json")
    public ResponseEntity<Node> getNode(@PathVariable String nodeName, HttpServletResponse response) {
        Node node = nodeService.getNode(nodeName);
        if (node == null) {
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("username : {} does not exist!" + username);
            return ResponseEntity.status(NOT_FOUND).body(null);
        }
        return ResponseEntity.ok().body(nodeService.getNode(nodeName));
    }

    //TODO : editing node name can be duplicates in parent!!!
    @Transactional
    @PostMapping("/node/save")
    public ResponseEntity<Node> saveNode(@RequestBody NodeForm nodeForm) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/node/save").toUriString());
        Optional<Node> parentNode = nodeRepository.findById(nodeForm.getParent_id());
        if (nodeForm.getValue() == null) {
            Node node = new Node(parentNode.get(), nodeForm.getName());
            if (node.isLeafNode()) {
                node.getParent().setScript(fileDBRepository.findByName("script_somme.groovy"));
            }
            return ResponseEntity.created(uri).body(nodeService.saveNode(node));
        } else {

            Node node = new Node(parentNode.get(), nodeForm.getName(), nodeForm.getValue());
            if (node.isLeafNode()) {
                log.info("i'm here now");

                node.getParent().setScript(fileDBRepository.findByName("script_somme.groovy"));
            }
            return ResponseEntity.created(uri).body(nodeService.saveNode(node));
        }

    }

    @PostMapping("/node/update")
    public ResponseEntity<?> setValue(@RequestBody UpdateNodeForm form) {
        nodeService.setNodeValue(form.getNodeName(), form.getValue());
        return ResponseEntity.ok().build();

    }

    @GetMapping("/nodes/sum")
    public ResponseEntity<Double> getSum() {
        Double sum = nodeService.sumNode("TOTAL");
        return ResponseEntity.ok().body(sum);
    }

    @DeleteMapping(value = "/node/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> deleteNode(@PathVariable Long id) {

        var exists = nodeRepository.findById(id);
        if (exists.get().isRootNode()) {
            return new ResponseEntity<>(new StringResponse("action forbidden!"), HttpStatus.FORBIDDEN);
        }
        var parent_id = exists.get().getParent().getId();
        if (exists.get() == null) {
            return new ResponseEntity<>(new StringResponse("rubrique not found"), HttpStatus.NOT_FOUND);
        }
        var parent = exists.get().getParent();
        nodeService.deleteNode(id);

        log.info("{} = {}", parent.getChildren().size() == 1, parent.getChildren().get(0) == exists.get());
        if (parent.getChildren().size() == 1 && parent.getChildren().get(0) == exists.get()) {
            nodeService.addScriptToNode(parent_id, null);
            log.info("parent script changed");
        }
        return new ResponseEntity<>(new StringResponse("rubrique deleted successfully"), HttpStatus.OK);
    }

    @PatchMapping("/node/{id}")
    public ResponseEntity<StringResponse> updateName(
            @PathVariable Long id,
            @RequestBody String newName) {

        try {
            Node node = nodeRepository.findById(id).get();

            if (newName.toLowerCase(Locale.ROOT).equals(node.getName().toLowerCase(Locale.ROOT)) || node.isRootNode() || newName.toLowerCase(Locale.ROOT).equals("total")) {
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            } else if (!node.getParent().getChildren().contains(nodeRepository.findByNameIgnoreCaseOrderById(newName))) {
                node.setName(newName);
                nodeService.saveNode(node);
                return new ResponseEntity<>(new StringResponse("node updated"), HttpStatus.OK);
            }
            else {return new ResponseEntity<>(new StringResponse("name already exists"),HttpStatus.INTERNAL_SERVER_ERROR);}
        } catch (Exception e) {
            return new ResponseEntity<>(new StringResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }



    @PatchMapping("/node/{childId}/parent/{parentId}")
    public ResponseEntity<StringResponse> updateParent(@PathVariable Long childId,@PathVariable Long parentId){

        try {
            Node parent = nodeRepository.findById(parentId).get();
            Node child = nodeRepository.findById(childId).get();
            nodeService.addChildToNode(parent.getName(),child.getName());
            return new ResponseEntity<>(new StringResponse("success"),HttpStatus.OK);
        }
        catch (Exception e)  {
            return new ResponseEntity<>(new StringResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }



    }





/*

    @Transactional
    @RequestMapping(
            value = "/update/{nodeId}/parent/{parentId}",
            method = PUT,
            produces = "application/json")
    @ResponseBody
    public Node replaceParent(@PathVariable("nodeId") long nodeId, @PathVariable("parentId") long parentId) {
        Node currNode = repo.findById(nodeId).get();
        Node parNode = repo.findById(parentId).get();
        currNode.setParent(parNode);
        repo.save(currNode);
        return currNode;
    } */
}
@Data
class ChildToParentForm {
    private String parentNode;
    private String childNode;
}
@Data
class UpdateNodeForm {
    private String nodeName;
    private double value;
}
@Data
class NodeForm {
    private Long parent_id;
    private String name;
    private Double Value;

}

@Data
 class StringResponse {

    private String response;

    public StringResponse(String s) {
        this.response = s;
    }

    // get/set omitted...
}

