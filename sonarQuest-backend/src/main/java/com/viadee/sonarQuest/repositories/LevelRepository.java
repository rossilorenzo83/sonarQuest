package com.viadee.sonarQuest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viadee.sonarQuest.entities.Level;

public interface LevelRepository extends JpaRepository<Level, Long> {

    Level findFirstByMinXpIsLessThanEqualOrderByLevelDesc(Long xp);
}
