package com.easyjob.easyjobapi.core.firm.management;

import com.easyjob.easyjobapi.core.firm.models.FirmDAO;
import org.springframework.data.jpa.domain.Specification;

public class FirmSpecifications {
    public static Specification<FirmDAO> isNotArchived() {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.and(criteriaBuilder.equal(root.get("isArchived"), false)));
    }
}
