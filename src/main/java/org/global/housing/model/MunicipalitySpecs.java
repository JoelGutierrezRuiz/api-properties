package org.global.housing.model;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.global.housing.dto.request.GrowthFilterRequest;
import org.global.housing.dto.request.PopulationFilterRequest;
import org.global.housing.dto.request.AgeGrowthFilterRequest;
import org.global.housing.entity.municipality.MunicipalityEntity;
import org.global.housing.entity.municipality.MunicipalityPopulationEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.criteria.Predicate;

public class MunicipalitySpecs {

    private static final Double MIN_PERCENTAGE = -99999d;
    // Límites de los datos por edad según lo indicado
    private static final int AGE_YEAR_MIN = 2021;
    private static final int AGE_YEAR_MAX = 2025;

    public static Specification<MunicipalityEntity> population(PopulationFilterRequest f, Integer previousYear) {
        return (root, query, cb) -> {

            Join<MunicipalityEntity, MunicipalityPopulationEntity> popJoin =
                    root.join("populations", JoinType.INNER);

            List<Predicate> predicates = new ArrayList<>();

            // Filtrar SIEMPRE por el año anterior
            predicates.add(cb.equal(popJoin.get("year"), previousYear));

            // Filtrar por población mínima
            if (f.getMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(popJoin.get("population"), f.getMin()));
            }

            // Filtrar por población máxima
            if (f.getMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(popJoin.get("population"), f.getMax()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<MunicipalityEntity> growth(GrowthFilterRequest f) {
        return (root, query, cb) -> {

            Join<MunicipalityEntity, MunicipalityPopulationEntity> popStart =
                    root.join("populations", JoinType.INNER);

            Join<MunicipalityEntity, MunicipalityPopulationEntity> popEnd =
                    root.join("populations", JoinType.INNER);

            List<Predicate> predicates = new ArrayList<>();

            // Año inicial
            if(Objects.nonNull(f.getYearStart())) {
                predicates.add(cb.equal(popStart.get("year"), f.getYearStart()));
            }
            else{
                predicates.add(cb.equal(popStart.get("year"), 1996));
            }
            // Año final
            if(Objects.nonNull(f.getYearEnd())) {
                predicates.add(cb.equal(popEnd.get("year"), f.getYearEnd()));
            }
            else{
                int previousYear = Year.now().getValue() - 1;
                predicates.add(cb.equal(popEnd.get("year"), previousYear));
            }

            // Convertimos a Double desde el principio
            Expression<Double> startPop = cb.toDouble(popStart.get("population"));
            Expression<Double> endPop   = cb.toDouble(popEnd.get("population"));

            // (end - start)
            Expression<Double> diff = cb.diff(endPop, startPop);

            // (end - start) * 100.0  ← OBLIGATORIO usar 100.0 (Double)
            Expression<Double> diffTimes100 = cb.prod(diff, 100.0D);

            // growth = ((end - start) * 100) / start
            Expression<Number> growthExpr = cb.quot(diffTimes100, startPop);

            // growth >= percentMin
            if (f.getPercentMin() != null) {
                predicates.add(cb.ge(growthExpr, f.getPercentMin().doubleValue()));
            }
            else {
                predicates.add(cb.ge(growthExpr,MIN_PERCENTAGE));
            }

            // growth <= percentMax
            if (f.getPercentMax() != null) {
                predicates.add(cb.le(growthExpr, f.getPercentMax().doubleValue()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * ageGrowth: Similar a `growth` pero sin filtrar por edad en la BD (no hay campo age). Solo usa años 2021-2025.
     */
    public static Specification<MunicipalityEntity> ageGrowth(AgeGrowthFilterRequest f) {
        return (root, query, cb) -> {

            Join<MunicipalityEntity, MunicipalityPopulationEntity> popStart =
                    root.join("populations", JoinType.INNER);

            Join<MunicipalityEntity, MunicipalityPopulationEntity> popEnd =
                    root.join("populations", JoinType.INNER);

            List<Predicate> predicates = new ArrayList<>();

            // Normalizar y validar los años en el rango 2021-2025
            int yearStart = Objects.nonNull(f.getYearStart()) ? f.getYearStart() : AGE_YEAR_MIN;
            if (yearStart < AGE_YEAR_MIN) yearStart = AGE_YEAR_MIN;
            if (yearStart > AGE_YEAR_MAX) yearStart = AGE_YEAR_MAX;

            int yearEnd = Objects.nonNull(f.getYearEnd()) ? f.getYearEnd() : AGE_YEAR_MAX;
            if (yearEnd < AGE_YEAR_MIN) yearEnd = AGE_YEAR_MIN;
            if (yearEnd > AGE_YEAR_MAX) yearEnd = AGE_YEAR_MAX;

            // Asegurar que start <= end
            if (yearStart > yearEnd) {
                int tmp = yearStart;
                yearStart = yearEnd;
                yearEnd = tmp;
            }

            // Año inicial y final (ya normalizados)
            predicates.add(cb.equal(popStart.get("year"), yearStart));
            predicates.add(cb.equal(popEnd.get("year"), yearEnd));

            // Convertimos a Double desde el principio
            Expression<Double> startPop = cb.toDouble(popStart.get("population"));
            Expression<Double> endPop   = cb.toDouble(popEnd.get("population"));

            // (end - start)
            Expression<Double> diff = cb.diff(endPop, startPop);

            // (end - start) * 100.0
            Expression<Double> diffTimes100 = cb.prod(diff, 100.0D);

            // growth = ((end - start) * 100) / start
            Expression<Number> growthExpr = cb.quot(diffTimes100, startPop);

            // growth >= percentMin
            if (f.getPercentMin() != null) {
                predicates.add(cb.ge(growthExpr, f.getPercentMin().doubleValue()));
            }
            else {
                predicates.add(cb.ge(growthExpr,MIN_PERCENTAGE));
            }

            // growth <= percentMax
            if (f.getPercentMax() != null) {
                predicates.add(cb.le(growthExpr, f.getPercentMax().doubleValue()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


}

