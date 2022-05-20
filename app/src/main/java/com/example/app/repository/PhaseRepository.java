package com.example.app.repository;

import com.example.app.domain.Phase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhaseRepository extends JpaRepository<Phase, Long> {
    public Phase findById(long id) ;
}
