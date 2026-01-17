package com.easyjob.easyjobapi.core.offer.magenement;

import com.easyjob.easyjobapi.core.offer.models.OfferDAO;
import com.easyjob.easyjobapi.utils.enums.EmploymentTypeEnum;
import com.easyjob.easyjobapi.utils.enums.ExperienceLevelEnum;
import com.easyjob.easyjobapi.utils.enums.WorkModeEnum;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OfferSpecifications {
    public static Specification<OfferDAO> byExperience(List<ExperienceLevelEnum> experienceLevels) {
        return (root, query, criteriaBuilder) -> {
            if (experienceLevels == null || experienceLevels.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return root.get("experienceLevel").in(experienceLevels);
        };
    }

    public static Specification<OfferDAO> byWorkMode(List<WorkModeEnum> workModes) {
        return ((root, query, criteriaBuilder) -> {
            if (workModes == null || workModes.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return root.get("workMode").in(workModes);
        });
    }

    public static Specification<OfferDAO> byEmploymentType(List<EmploymentTypeEnum> employmentTypes) {
        return ((root, query, criteriaBuilder) -> {
            if (employmentTypes == null || employmentTypes.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return root.get("employmentType").in(employmentTypes);
        });
    }

    public static Specification<OfferDAO> bySkillsAll(List<String> skills) {
        return (root, query, cb) -> {
            if (skills == null || skills.isEmpty()) {
                return cb.conjunction();
            }

            query.distinct(true);

            Join<OfferDAO, String> skillsJoin = root.join("skills");

            Expression<Long> skillCount = cb.countDistinct(skillsJoin);

            query.groupBy(root.get("id"));
            query.having(cb.equal(skillCount, (long) skills.size()));

            return skillsJoin.in(skills);
        };
    }

    public static Specification<OfferDAO> bySalaryRange(Long minSalary, Long maxSalary) {
        return (root, query, cb) -> {

            if (minSalary == null && maxSalary == null) {
                return cb.conjunction();
            }

            Predicate salaryNotDisclosed = cb.and(
                    cb.isNull(root.get("salaryBottom")),
                    cb.isNull(root.get("salaryTop"))
            );

            List<Predicate> disclosed = new ArrayList<>();

            disclosed.add(cb.isNotNull(root.get("salaryBottom")));
            disclosed.add(cb.isNotNull(root.get("salaryTop")));

            if (minSalary != null) {
                disclosed.add(
                        cb.greaterThanOrEqualTo(root.get("salaryTop"), minSalary)
                );
            }

            if (maxSalary != null) {
                disclosed.add(
                        cb.lessThanOrEqualTo(root.get("salaryBottom"), maxSalary)
                );
            }

            Predicate salaryDisclosedAndMatching =
                    cb.and(disclosed.toArray(new Predicate[0]));

            return cb.or(salaryDisclosedAndMatching, salaryNotDisclosed);
        };
    }

    public static Specification<OfferDAO> byNameLike(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) {
                return cb.conjunction();
            }

            return cb.like(
                    cb.lower(root.get("name")),
                    "%" + search.toLowerCase() + "%"
            );
        };
    }
}
