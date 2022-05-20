package com.example.app.service;

import com.example.app.domain.Phase;
import com.example.app.domain.Project;
import com.example.app.repository.PhaseRepository;
import com.example.app.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class PhaseServiceImpl implements PhaseService{
    @Autowired
    private PhaseRepository phaseRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public Phase getPhase(long id) {
        log.info("fetching phase with id {}" , id);
        Phase phase = phaseRepository.getById(id);
        return phase;
    }

    @Override
    public Phase savePhase(Phase phase) {
        log.info("saving phase {} to database" , phase.getName());
        return phaseRepository.save(phase);
    }

    @Override
    public Phase addSubPhase(long parent_id, Phase subPhase) {
        return null;
    }

    @Override
    public void addPhaseToProject(long phase_id,long project_id) {
        Phase phase = phaseRepository.getById(phase_id);
        Project project = projectRepository.getById(project_id);
        log.info("adding phase {} to project {}" , phase.getName() , project.getName());
        project.addPhase(phase);
       // projectRepository.save(project);

    }

    @Override
    public void deletePhase(long id) {
        log.info("deleting phase with id {}",id);
        phaseRepository.deleteById(id);
    }

    @Override
    public void addSubPhaseToPhase(long id, Phase phase) {
        Phase parentPhase = phaseRepository.getById(id);
        log.info("adding subPhase {} to phase {}", phase.getName(), parentPhase.getName());
        phase.setParent(parentPhase);
        phaseRepository.save(phase);



    }
}
