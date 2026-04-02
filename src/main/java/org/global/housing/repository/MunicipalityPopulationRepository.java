package org.global.housing.repository;

import org.global.housing.entity.municipality.MunicipalityPopulationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MunicipalityPopulationRepository extends JpaRepository<MunicipalityPopulationEntity, Integer> {
    @Query("SELECT p.municipalityId, p.population FROM MunicipalityPopulationEntity p WHERE p.year = :year")
    List<Object[]> findPopulationByYear(@Param("year") int year);

    @Query("SELECT p.year, p.population FROM MunicipalityPopulationEntity p WHERE p.municipalityId = :id")
    List<Object[]> findPopulationByMunicipalityId(@Param("id") String id);
}