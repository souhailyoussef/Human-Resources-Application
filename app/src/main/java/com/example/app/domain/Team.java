package com.example.app.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String teamName;

    @ManyToMany
    @JoinTable(
            name = "team_members",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id"))
    private List<AppUser> teamMembers;

    @JsonBackReference
    @OneToMany(mappedBy = "team")
    private List<Project> projects;

    public Map<Long,String> getTeamMembers() {
        Map<Long,String> map=new HashMap<Long,String>();
        for (AppUser member : teamMembers)  {
            map.put(Long.valueOf(member.getId()),member.getUsername());
        }
        return map;
    }

    public Team(String teamName) {
        this.teamName=teamName;
    }

    public void addProjectToTeam(Project project) {
        this.projects.add(project);
        project.setTeam(this);
    }

    public void removeProjectFromTeam(Project project) {
         this.projects.remove(project);
         project.setTeam(null);
    }

    public boolean addMember(AppUser member) {
        return member.getTeams().add(this);
    }
    public boolean removeMember(AppUser member) {
        return this.teamMembers.remove(member);
    }

    public Project checkIfExists(String projectName) {
        for (Project project : this.getProjects()) {
            if (project.getName().equalsIgnoreCase(projectName)) {
                return project;
            }
        }
        return null;
    }
}
