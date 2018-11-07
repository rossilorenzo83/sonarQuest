package com.viadee.sonarQuest.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viadee.sonarQuest.entities.Participation;
import com.viadee.sonarQuest.entities.Quest;
import com.viadee.sonarQuest.entities.User;
import com.viadee.sonarQuest.repositories.ParticipationRepository;
import com.viadee.sonarQuest.repositories.QuestRepository;

@Service
public class ParticipationService {

    @Autowired
    private QuestRepository questRepository;

    @Autowired
    private ParticipationRepository participationRepository;

    @Autowired
    private UserService userService;

    public Participation findParticipationByQuestIdAndUserId(final Long questId, final Long userId) {
        final Optional<Quest> quest = questRepository.findById(questId);
        User user = userService.findById(userId);
        Participation foundParticipation = null;
        if (quest.isPresent() && user != null) {
            foundParticipation = participationRepository.findByQuestAndUser(quest.get(), user);
        }
        return foundParticipation;
    }

    public List<Participation> findParticipationByQuestId(final Long questId) {
        final Optional<Quest> quest = questRepository.findById(questId);
        if (quest.isPresent()) {
            return participationRepository.findByQuest(quest.get());
        }
        return null;
    }
}
