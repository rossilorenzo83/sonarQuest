package com.viadee.sonarQuest.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viadee.sonarQuest.entities.Artefact;
import com.viadee.sonarQuest.entities.Skill;
import com.viadee.sonarQuest.repositories.SkillRepository;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    public Skill createSkill(final Skill skillDto) {
        final Skill skill = new Skill();
        skill.setName(skillDto.getName());
        skill.setType(skillDto.getType());
        skill.setValue(skillDto.getValue());
        return skillRepository.save(skill);
    }

    public List<Skill> getSkillsForArtefact(final Artefact artefact) {
        if (artefact != null) {
            return artefact.getSkills();
        } else {
            return new ArrayList<>();
        }
    }

    public void deleteSkill(final Skill skill) {
        if (skill != null) {
            skillRepository.delete(skill);
        }
    }

}
