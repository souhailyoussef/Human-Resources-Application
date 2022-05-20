package com.example.app.service;

import com.example.app.domain.Phase;

public interface PhaseService {
    Phase getPhase(long id);
    Phase savePhase(Phase phase);
    Phase addSubPhase(long parent_id, Phase subPhase);
    void addPhaseToProject(long phase_id,long project_id);
    void deletePhase(long id);
    void addSubPhaseToPhase(long id, Phase phase);
}
