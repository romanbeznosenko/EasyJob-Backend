package com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.management;

import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models.CVDAO;
import org.springframework.data.jpa.domain.Specification;

public class CVSpecifications {
    public static Specification<CVDAO> findByApplierProfile(ApplierProfileDAO applierProfile) {
        return ((root, query, criteriaBuilder) -> {
            if (applierProfile == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get("applierProfile"), applierProfile);
        });
    }
}
