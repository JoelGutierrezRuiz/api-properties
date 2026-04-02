package org.global.housing.repository;

import org.global.housing.entity.municipality.UserFavoriteMunicipalityEntity;
import org.global.housing.entity.municipality.UserFavoriteMunicipalityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFavoriteMunicipalityRepository
        extends JpaRepository<UserFavoriteMunicipalityEntity, UserFavoriteMunicipalityId> {

    List<UserFavoriteMunicipalityEntity> findByUserId(Long userId);

    boolean existsById(UserFavoriteMunicipalityId id);
}
