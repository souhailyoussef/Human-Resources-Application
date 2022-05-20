package com.example.app.service;

import com.example.app.domain.Team;

public interface TeamService {
     Team saveTeam(Team team);
     Team getTeam(long id);
     boolean addMember(String username, long team_id);
     boolean removeMember(String username, long team_id);
     void addTeamToProject(long team_id, long project_id);

}
