package com.viadee.sonarQuest.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.viadee.sonarQuest.entities.AvatarRace;
import com.viadee.sonarQuest.repositories.AvatarRaceRepository;

@RestController
@RequestMapping("/avatarRace")
public class AvatarRaceController {

    private AvatarRaceRepository avatarRaceRepository;

    public AvatarRaceController(final AvatarRaceRepository avatarRaceRepository) {
        this.avatarRaceRepository = avatarRaceRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<AvatarRace> getAllAvatarRaces() {
        return avatarRaceRepository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public AvatarRace getAvatarRaceById(@PathVariable(value = "id") final Long id) {
        return avatarRaceRepository.findById(id).orElse(null);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public AvatarRace createAvatarRace(@RequestBody final AvatarRace avatarRace) {
        return avatarRaceRepository.save(avatarRace);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public AvatarRace updateAvatarRace(@PathVariable(value = "id") final Long id, @RequestBody final AvatarRace data) {
        Optional<AvatarRace> avatarRace = avatarRaceRepository.findById(id);
        if (avatarRace.isPresent()) {
            AvatarRace realAvatarRace = avatarRace.get();
            realAvatarRace.setName(data.getName());
            return avatarRaceRepository.save(realAvatarRace);
        }
        return null;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteAvatarRace(@PathVariable(value = "id") final Long id) {
        Optional<AvatarRace> avatarRace = avatarRaceRepository.findById(id);
        if (avatarRace.isPresent()) {
            avatarRaceRepository.delete(avatarRace.get());
        }
    }

}