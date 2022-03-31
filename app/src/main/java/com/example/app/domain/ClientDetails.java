package com.example.app.domain;

public class ClientDetails {
    private int projectId;
    private int clientId;
    private int ContractId;
    private String projectName;
    private String clientName;
    private double cost;

    public ClientDetails(int projectId, int clientId, int contractId, String projectName, String clientName, double cost) {
        this.projectId = projectId;
        this.clientId = clientId;
        ContractId = contractId;
        this.projectName = projectName;
        this.clientName = clientName;
        this.cost = cost;
    }
}
