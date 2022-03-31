package com.example.app.service;

import com.example.app.domain.Contract;
import com.example.app.repository.ClientRepository;
import com.example.app.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;

    @Override
    public Contract saveContract(Contract contract) {
        return contractRepository.save(contract);
    }

    @Override
    public Contract getContract(long id) {
        return contractRepository.findById(id);
    }

    @Override
    public List<Contract> getContracts() {
        return contractRepository.findAll();
    }
}
