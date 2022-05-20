package com.example.app.service;


import com.example.app.domain.AppUser;
import com.example.app.domain.Project;
import com.example.app.domain.Team;
import com.example.app.repository.TeamRepository;
import com.example.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TeamServiceImpl implements TeamService {



    @Autowired
    private ProjectService projectService;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Team saveTeam(Team team) {
        log.info("saving new team {}", team.getTeamName());
        return teamRepository.save(team);
    }

    @Override
    public Team getTeam(long id) {
        log.info("fetching team with id {}", id);
        return teamRepository.findById(id);
    }

    @Override
    public boolean addMember(String username, long team_id) {
        Team team = teamRepository.findById(team_id);
        AppUser member = userRepository.findByUsernameIgnoreCase(username);
        log.info("adding user {} to team with id {}", username,team_id);
        return team.addMember(member);
    }

    @Override
    public boolean removeMember(String username, long team_id) {
        Team team = teamRepository.findById(team_id);
        AppUser member = userRepository.findByUsernameIgnoreCase(username);
        log.info("removing user {} from team with id {}", username,team_id);
        return team.removeMember(member);
    }

    @Override
    public void addTeamToProject(long team_id, long project_id) {
        Project project = projectService.getProject(project_id);
        Team team = teamRepository.getById(team_id);
        log.info("assigning project {} to team {}", project.getName(), team.getTeamName());
        team.addProjectToTeam(project);
    }
}
