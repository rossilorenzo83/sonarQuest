package com.viadee.sonarQuest.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.viadee.sonarQuest.entities.Artefact;
import com.viadee.sonarQuest.entities.Skill;
import com.viadee.sonarQuest.repositories.SkillRepository;
import com.viadee.sonarQuest.services.ArtefactService;
import com.viadee.sonarQuest.services.SkillService;

@RestController
@RequestMapping("/skill")
public class SkillController {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private ArtefactService artefactService;

    @Autowired
    private SkillService skillService;

    public SkillController(final SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Skill getSkillById(@PathVariable(value = "id") final Long id) {
        return skillRepository.findById(id).orElse(null);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Skill createSkill(@RequestBody final Skill skillDto) {
        return skillRepository.save(skillDto);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Skill updateSkill(@PathVariable(value = "id") final Long id, @RequestBody final Skill data) {
        Skill skill = skillRepository.findById(id).orElse(null);
        if (skill != null) {
            skill.setName(data.getName());
            skill = skillRepository.save(skill);
        }
        return skill;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteSkill(@PathVariable(value = "id") final Long id) {
        Optional<Skill> skill = skillRepository.findById(id);
        if (skill.isPresent()) {
            skillService.deleteSkill(skill.get());
        }
    }

    @RequestMapping(value = "artefact/{artefact_id}", method = RequestMethod.GET)
    public List<Skill> getSkillsForArtefact(@PathVariable(value = "artefact_id") final Long id) {
        final Artefact artefact = artefactService.getArtefact(id);
        return skillService.getSkillsForArtefact(artefact);
    }

}
