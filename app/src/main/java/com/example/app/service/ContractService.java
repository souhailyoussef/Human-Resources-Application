package com.example.app.service;

import com.example.app.domain.Client;
import com.example.app.domain.Contract;

import java.util.List;

public interface ContractService {
    Contract saveContract(Contract contract);
    Contract getContract(long id);
    List<Contract> getContracts();
}
