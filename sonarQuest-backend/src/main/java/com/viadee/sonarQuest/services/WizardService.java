package com.viadee.sonarQuest.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.viadee.sonarQuest.entities.WizardMessage;
import com.viadee.sonarQuest.entities.World;

@Service
public class WizardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WizardService.class);

    /**
     * FIXME Dummy Implementierung
     * 
     * Checks the world to see if the following steps are complete. If they are not, a message with the most important
     * next step is delivered.
     * <ol>
     * <li>If the world is null and there are no worlds in the DB => "Worlds aka Projects must be retrieved from the
     * server first"</li>
     * <li>If the world is null => "One of the worlds must be chosen." (more steps to check if worlds aka projects can
     * be generated)</li>
     * <li>If there is no gm => "A Gamemaster must be chosen for the world."</li>
     * <li>If there are no players (other then gm) => "Players should be assigned to the world"</li>
     * <li>If there are no tasks => "The Gamemaster should retrieve tasks from the server to assign them to quests"</li>
     * <li>If there are no quests => "The Gamemaster should create a quest with tasks</li>
     * </ol>
     */
    public WizardMessage getMostImportantMessageFor(World world) {
        LOGGER.error(
                "com.viadee.sonarQuest.services.WizardService.getMostImportantMessageFor(World) not yet implemented! Returning dummy for WizardMessage");
        WizardMessage dummyMsg = new WizardMessage();
        dummyMsg.setMessage("Dummy Message");
        dummyMsg.setSolution("Dummy solution");
        return dummyMsg;
    }

}
