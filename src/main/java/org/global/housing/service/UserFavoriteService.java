package org.global.housing.service;

import org.global.housing.entity.login.UserEntity;
import org.global.housing.entity.municipality.UserFavoriteMunicipalityEntity;
import org.global.housing.entity.municipality.UserFavoriteMunicipalityId;
import org.global.housing.entity.municipality.MunicipalityEntity;
import org.global.housing.repository.UserRepository;
import org.global.housing.repository.MunicipalityRepository;
import org.global.housing.repository.UserFavoriteMunicipalityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserFavoriteService {

    private final UserRepository userRepository;
    private final MunicipalityRepository municipalityRepository;
    private final UserFavoriteMunicipalityRepository userFavoriteMunicipalityRepository;

    public UserFavoriteService(UserRepository userRepository,
                               MunicipalityRepository municipalityRepository,
                               UserFavoriteMunicipalityRepository userFavoriteMunicipalityRepository) {
        this.userRepository = userRepository;
        this.municipalityRepository = municipalityRepository;
        this.userFavoriteMunicipalityRepository = userFavoriteMunicipalityRepository;
    }

    @Transactional
    public List<String> addLikeByEmail(String email, String municipalityId) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        MunicipalityEntity municipality = municipalityRepository.findById(municipalityId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Municipio no encontrado"));

        UserFavoriteMunicipalityId id = new UserFavoriteMunicipalityId();
        id.setUserId(user.getId());
        id.setMunicipalityId(municipalityId);

        if (!userFavoriteMunicipalityRepository.existsById(id)) {
            UserFavoriteMunicipalityEntity fav = new UserFavoriteMunicipalityEntity();
            fav.setId(id);
            fav.setUser(user);
            fav.setMunicipality(municipality);
            userFavoriteMunicipalityRepository.save(fav);
        }

        return listLikesForUser(user.getId());
    }

    @Transactional(readOnly = true)
    public List<String> listLikesByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        return listLikesForUser(user.getId());
    }

    @Transactional
    public List<String> removeLikeByEmail(String email, String municipalityId) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        UserFavoriteMunicipalityId id = new UserFavoriteMunicipalityId();
        id.setUserId(user.getId());
        id.setMunicipalityId(municipalityId);

        if (userFavoriteMunicipalityRepository.existsById(id)) {
            userFavoriteMunicipalityRepository.deleteById(id);
        }

        return listLikesForUser(user.getId());
    }

    private List<String> listLikesForUser(Long userId) {
        return userFavoriteMunicipalityRepository.findByUserId(userId).stream()
                .map(f -> f.getMunicipality().getMunicipalityId())
                .collect(Collectors.toList());
    }
}
