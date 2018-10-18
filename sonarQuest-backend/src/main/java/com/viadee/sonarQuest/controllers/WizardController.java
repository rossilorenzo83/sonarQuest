package com.viadee.sonarQuest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.viadee.sonarQuest.entities.WizardMessage;
import com.viadee.sonarQuest.entities.World;
import com.viadee.sonarQuest.services.WizardService;
import com.viadee.sonarQuest.services.WorldService;

@RestController
@RequestMapping("/wizard")
public class WizardController {

    @Autowired
    private WizardService wizardService;

    @Autowired
    private WorldService worldService;

    @RequestMapping(value = "/{worldId}", method = RequestMethod.GET)
    public WizardMessage getWizardMessageForWorld(@PathVariable(value = "worldId") final Long worldId) {
        final World world = worldService.findById(worldId);
        return wizardService.getMostImportantMessageFor(world);
    }

}
