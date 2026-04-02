package org.global.housing.repository;

import org.global.housing.entity.municipality.MunicipalityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface MunicipalityRepository extends
        JpaRepository<MunicipalityEntity, String>,
        JpaSpecificationExecutor<MunicipalityEntity> {
}
