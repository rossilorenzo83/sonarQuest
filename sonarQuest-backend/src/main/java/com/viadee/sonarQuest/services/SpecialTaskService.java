package com.viadee.sonarQuest.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viadee.sonarQuest.entities.SpecialTask;
import com.viadee.sonarQuest.entities.World;
import com.viadee.sonarQuest.repositories.SpecialTaskRepository;
import com.viadee.sonarQuest.repositories.WorldRepository;
import com.viadee.sonarQuest.rules.SonarQuestStatus;

@Service
public class SpecialTaskService {

    @Autowired
    private SpecialTaskRepository specialTaskRepository;

    @Autowired
    private WorldRepository worldRepository;

    public void saveDto(final SpecialTask specialTaskDto) {

        final World world = worldRepository.findByProject(specialTaskDto.getWorld().getProject());

        final SpecialTask sp = new SpecialTask(
                specialTaskDto.getTitle(),
                SonarQuestStatus.OPEN,
                specialTaskDto.getGold(),
                specialTaskDto.getXp(),
                specialTaskDto.getQuest(),
                specialTaskDto.getMessage(),
                world);

        specialTaskRepository.save(sp);
    }

    public SpecialTask updateSpecialTask(final SpecialTask taskDto) {
        Optional<SpecialTask> task = specialTaskRepository.findById(taskDto.getId());
        if (task.isPresent()) {
            final SpecialTask realtask = task.get();
            realtask.setTitle(taskDto.getTitle());
            realtask.setGold(taskDto.getGold());
            realtask.setXp(taskDto.getXp());
            realtask.setMessage(taskDto.getMessage());
            return specialTaskRepository.save(realtask);
        }
        return null;
    }

    public List<SpecialTask> findAll() {
        return specialTaskRepository.findAll();
    }

    public List<SpecialTask> findByWorld(final World w) {
        return specialTaskRepository.findByWorld(w);
    }

    public SpecialTask findById(final Long id) {
        return specialTaskRepository.findById(id).orElse(null);
    }

}
