package com.example.app.service;

import com.example.app.domain.AppUser;
import com.example.app.domain.Node;

import java.util.HashMap;
import java.util.List;

public interface NodeService {
    Node saveNode(Node node);
    Node getNode(String nodeName);
    void addChildToNode(String parentNode,String childNode);
    //nodeNames are not unique!!
    void setNodeValue(String nodeName,double value);
    void deleteNode(Long id);
    double sumNode(String nodeName);
}
