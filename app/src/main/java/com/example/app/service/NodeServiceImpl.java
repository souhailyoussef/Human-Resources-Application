package com.example.app.service;

import com.example.app.domain.AppUser;
import com.example.app.domain.FileDB;
import com.example.app.domain.Node;
import com.example.app.repository.FileDBRepository;
import com.example.app.repository.NodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class NodeServiceImpl implements NodeService{

    @Autowired
    private  NodeRepository nodeRepository;

    @Autowired
    private FileDBRepository fileDBRepository;


    @Override
    public Node saveNode(Node node) { // check if it exists in parent node, if not save, if yes , dont save(dont check whole db)
        log.info("saving new node {} to DB", node.getName());
        if (!node.isRootNode()) {
            List<Node> children = node.getParent().getChildren();
            log.info("children : {}", children);
            for (Node child : children) {
                if (child.getName().equals(node.getName())) {
                    log.info("duplicate name");
                    return null;
                }
                }
            log.info("node saved");
            return nodeRepository.save(node);
        }
        else {
            if(nodeRepository.findByNameIgnoreCaseOrderById(node.getName())!=null) {
                log.info("root node already exists");
                return null ;
            }
            log.info("node saved");
            return nodeRepository.save(node);
        }
    }

    @Override
    public Node getNode(String nodeName) {
        log.info("fetching node {}",nodeName);
        return nodeRepository.findByNameIgnoreCaseOrderById(nodeName);
    }

    @Override
    public void addChildToNode(String parentNode, String childNode) {
        log.info("adding child {} to node {} in DB",childNode,parentNode);
        Node parent= nodeRepository.findByNameIgnoreCaseOrderById(parentNode);
        log.info("parent : {}",parent.toString());

        Node child= nodeRepository.findByNameIgnoreCaseOrderById(childNode);
        log.info("child: {}",child.toString());
        Node previous_parent= nodeRepository.findByNameIgnoreCaseOrderById(child.getParent().getName());
        //previous_parent.setValue(previous_parent.getValue()- child.getValue());

        if (parent.getChildren().contains(child) ) {
            log.info("child {} already exists for node {} " , childNode , parentNode);
        }
        else {
            child.setParent(parent);
            log.info("added child {} to node {}", childNode, parentNode);
        }
        //if this doesnt work, we need to save both parent and child instances to save changes


    }
    

    @Override
    public void setNodeValue(String nodeName,double value) {
        Node node = nodeRepository.findByNameIgnoreCaseOrderById(nodeName);
        if (node == null || node.isLeafNode()!=true) {
            log.info("node {} does not exist", node.getName());
        }
        else {
            node.setValue(value);
        }
    }

   /* @Override
    public List<Node> getChildren(String nodeName) {
        Node node = nodeRepository.findByNameIgnoreCase(nodeName);

    }*/

    @Override
    public double sumNode(String nodeName) {
        Node node = nodeRepository.findByNameIgnoreCaseOrderById(nodeName);

        node.sumNode();

        return node.getValue();
    }
    @Override
    public void deleteNode(Long id) {

      nodeRepository.deleteById(id);

    }

    @Override
    public List<Node> findByScriptId(Long id) {
        try {
            List<Node> nodes = nodeRepository.findByScriptId(id);
            return nodes;
        }
        catch (Exception e) {
            log.warn("{}", e.getMessage());
            return null;
        }
    }

    @Override
    public void addScriptToNode (Long node_id,Long script_id) {
        try  {

            Node node = nodeRepository.findById(node_id).get();
            if (script_id==null) {
                node.setScript(null);
            }
            else {
                FileDB script = fileDBRepository.findById(script_id).get();
                node.setScript(script);
            }
        }
         catch (Exception e) {
            log.warn(e.getMessage());
         }

    }
    /*

    public List<Node> getChildNodes(long id) {
        Date startTime = new Date();
        List<Node> result = nodeRepository.getAllNodes();

        Map<Node, List<Node>> tree = new TreeMap<Node, List<Node>>();
        List<Node> opList = new ArrayList<Node>();
        for (Node n : result) {
            if (n.getParent() == null) {
                continue;
            }
            if (!tree.containsKey(n.getParent()))
                tree.put(n.getParent(), new ArrayList<Node>());
            tree.get(n.getParent()).add(n);

        }

        for (Map.Entry<Node, List<Node>> elem : tree.entrySet()) {
            if (elem.getKey().getId() == id || opList.contains(elem.getKey())) {
                opList.addAll(elem.getValue());
            }

        }


        log.info("size of child list " + opList.size());
        Date endTime = new Date();
        log.info(" Execution time " + (startTime.getTime() - endTime.getTime()) / 1000);
        return opList;
    }


    public Node replaceParent(long nodeId, long parentId) {
        Node currNode = nodeRepository.findById(nodeId).get();
        Node parNode = nodeRepository.findById(parentId).get();
        currNode.setParent(parNode);
        nodeRepository.save(currNode);
        return currNode;
    }
*/

}
