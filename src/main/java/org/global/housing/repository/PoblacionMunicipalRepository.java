package org.global.housing.repository;

import org.global.housing.entity.municipality.PoblacionMunicipalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PoblacionMunicipalRepository extends JpaRepository<PoblacionMunicipalEntity, Object> {

    @Query("SELECT p FROM PoblacionMunicipalEntity p WHERE p.municipioId = :municipioId ORDER BY p.anio ASC, p.edadNum ASC")
    List<PoblacionMunicipalEntity> findByMunicipioIdOrdered(@Param("municipioId") String municipioId);

    @Query("SELECT p.municipioId, p.anio, SUM(p.poblacion) " +
           "FROM PoblacionMunicipalEntity p " +
           "WHERE p.municipioId IN :ids AND p.anio IN :years AND p.edadNum BETWEEN :minAge AND :maxAge " +
           "GROUP BY p.municipioId, p.anio")
    List<Object[]> sumByMunicipioAndYearsAndAgeRange(@Param("ids") List<String> municipioIds,
                                                      @Param("years") List<Integer> years,
                                                      @Param("minAge") Integer minAge,
                                                      @Param("maxAge") Integer maxAge);
}
