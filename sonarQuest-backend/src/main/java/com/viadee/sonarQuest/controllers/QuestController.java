package com.viadee.sonarQuest.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.viadee.sonarQuest.constants.QuestState;
import com.viadee.sonarQuest.entities.Adventure;
import com.viadee.sonarQuest.entities.Quest;
import com.viadee.sonarQuest.entities.Task;
import com.viadee.sonarQuest.entities.User;
import com.viadee.sonarQuest.entities.World;
import com.viadee.sonarQuest.repositories.AdventureRepository;
import com.viadee.sonarQuest.repositories.QuestRepository;
import com.viadee.sonarQuest.repositories.WorldRepository;
import com.viadee.sonarQuest.rules.SonarQuestStatus;
import com.viadee.sonarQuest.services.AdventureService;
import com.viadee.sonarQuest.services.GratificationService;
import com.viadee.sonarQuest.services.QuestService;
import com.viadee.sonarQuest.services.UserService;

@RestController
@RequestMapping("/quest")
public class QuestController {

    @Autowired
    private QuestRepository questRepository;

    @Autowired
    private WorldRepository worldRepository;

    @Autowired
    private AdventureRepository adventureRepository;

    @Autowired
    private QuestService questService;

    @Autowired
    private GratificationService gratificationService;

    @Autowired
    private AdventureService adventureService;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Quest> getAllQuests() {
        return questRepository.findAll();
    }

    @RequestMapping(value = "/world/{id}", method = RequestMethod.GET)
    public List<Quest> getAllQuestsForWorld(@PathVariable(value = "id") final Long world_id) {
        final World w = worldRepository.findById(world_id).orElse(null);
        return questRepository.findByWorld(w);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Quest getQuestById(@PathVariable(value = "id") final Long questId) {
        return questRepository.findById(questId).orElse(null);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Quest createQuest(@RequestBody final Quest questDto) {
        questDto.setStatus(QuestState.OPEN);
        return questRepository.save(questDto);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Quest updateQuest(@PathVariable(value = "id") final Long questId, @RequestBody final Quest data) {
        Optional<Quest> quest = questRepository.findById(questId);
        if (quest.isPresent()) {
            Quest realQuest = quest.get();
            realQuest.setTitle(data.getTitle());
            realQuest.setGold(data.getGold());
            realQuest.setXp(data.getXp());
            realQuest.setStory(data.getStory());
            realQuest.setImage(data.getImage());
            return questRepository.save(realQuest);
        }
        return null;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteQuest(@PathVariable(value = "id") final Long questId) {
        final Optional<Quest> quest = questRepository.findById(questId);
        if (quest.isPresent()) {
            Quest realQuest = quest.get();
            final List<Task> tasks = realQuest.getTasks();
            tasks.forEach(task -> task.setStatus(SonarQuestStatus.OPEN));
            questRepository.delete(realQuest);
        }
    }

    @RequestMapping(value = "/{questId}/solveQuest/", method = RequestMethod.PUT)
    public void solveQuest(@PathVariable(value = "questId") final Long questId) {
        final Optional<Quest> quest = questRepository.findById(questId);
        if (quest.isPresent()) {
            Quest realQuest = quest.get();
            gratificationService.rewardUsersForSolvingQuest(realQuest);
            adventureService.updateAdventure(realQuest.getAdventure());
            realQuest.setStatus(QuestState.SOLVED);
            questRepository.save(realQuest);
        }
    }

    @RequestMapping(value = "/{questId}/addWorld/{worldId}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Quest addWorld(@PathVariable(value = "questId") final Long questId,
            @PathVariable(value = "worldId") final Long worldId) {
        final Optional<Quest> quest = questRepository.findById(questId);
        if (quest.isPresent()) {
            Quest realQuest = quest.get();
            final Optional<World> world = worldRepository.findById(worldId);
            if (world.isPresent()) {
                realQuest.setWorld(world.get());
                return questRepository.save(realQuest);
            }
        }
        return null;
    }

    @RequestMapping(value = "/{questId}/addAdventure/{adventureId}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Quest addAdventure(@PathVariable(value = "questId") final Long questId,
            @PathVariable(value = "adventureId") final Long adventureId) {
        final Optional<Quest> quest = questRepository.findById(questId);
        if (quest.isPresent()) {
            Quest realQuest = quest.get();
            final Optional<Adventure> adventure = adventureRepository.findById(adventureId);
            if (adventure.isPresent()) {
                realQuest.setAdventure(adventure.get());
                return questRepository.save(realQuest);
            }
        }
        return null;
    }

    @RequestMapping(value = "/{questId}/deleteWorld", method = RequestMethod.DELETE)
    public void deleteWorld(@PathVariable(value = "questId") final Long questId) {
        final Optional<Quest> quest = questRepository.findById(questId);
        if (quest.isPresent()) {
            Quest realQuest = quest.get();
            realQuest.setWorld(null);
            questRepository.save(realQuest);
        }
    }

    @RequestMapping(value = "/{questId}/removeAdventure", method = RequestMethod.DELETE)
    public void deleteAdventure(@PathVariable(value = "questId") final Long questId) {
        final Optional<Quest> quest = questRepository.findById(questId);
        if (quest.isPresent()) {
            Quest realQuest = quest.get();
            realQuest.setAdventure(null);
            questRepository.save(realQuest);
        }
    }

    @RequestMapping(value = "/suggestTasksForQuestByGoldAmount/{worldId}/{goldAmount}", method = RequestMethod.GET)
    public List<Task> suggestTasksForQuestByGoldAmount(@PathVariable("worldId") final Long worldId,
            @PathVariable("goldAmount") final Long goldAmount) {
        final Optional<World> world = worldRepository.findById(worldId);
        if (world.isPresent()) {
            return questService.suggestTasksWithApproxGoldAmount(world.get(), goldAmount);
        }
        return null;

    }

    @RequestMapping(value = "/suggestTasksForQuestByXpAmount/{worldId}/{xpAmount}", method = RequestMethod.GET)
    public List<Task> suggestTasksForQuestByXpAmount(@PathVariable("worldId") final Long worldId,
            @PathVariable("xpAmount") final Long xpAmount) {
        final Optional<World> world = worldRepository.findById(worldId);
        if (world.isPresent()) {
            return questService.suggestTasksWithApproxXpAmount(world.get(), xpAmount);
        }
        return null;

    }

    @RequestMapping(value = "/getAllFreeForWorld/{worldId}", method = RequestMethod.GET)
    public List<Quest> getAllFreeQuestsForWorld(@PathVariable(value = "worldId") final Long worldId) {
        final Optional<World> world = worldRepository.findById(worldId);
        if (world.isPresent()) {
            List<Quest> freeQuests = questRepository.findByWorldAndAdventure(world.get(), null);
            freeQuests.removeIf(q -> q.getStatus() == QuestState.SOLVED);
            return freeQuests;
        }
        return null;
    }

    @RequestMapping(value = "/getAllQuestsForWorldAndUser/{worldId}", method = RequestMethod.GET)
    public List<List<Quest>> getAllQuestsForWorldAndUser(final Principal principal,
            @PathVariable(value = "worldId") final Long worldId) {
        final String username = principal.getName();
        final User user = userService.findByUsername(username);
        final Optional<World> world = worldRepository.findById(worldId);
        List<List<Quest>> quests = null;
        if (world.isPresent() && user != null) {
            final List<List<Quest>> allQuestsForWorldAndDeveloper = questService
                    .getAllQuestsForWorldAndUser(world.get(), user);

            quests = allQuestsForWorldAndDeveloper.stream()
                    .map(questlist -> questlist.stream().collect(Collectors.toList()))
                    .collect(Collectors.toList());
        }
        return quests;
    }

}
