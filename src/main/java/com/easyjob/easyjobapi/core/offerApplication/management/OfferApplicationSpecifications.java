package com.easyjob.easyjobapi.core.offerApplication.management;

import com.easyjob.easyjobapi.core.offer.models.OfferDAO;
import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplicationDAO;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class OfferApplicationSpecifications {
    public static Specification<OfferApplicationDAO> findByUserId(UUID userId) {
        return ((root, query, criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.and(criteriaBuilder.equal(
                    root.get("applierProfile").get("user").get("id"),
                    userId
            ));
        });
    }

    public static Specification<OfferApplicationDAO> findByOffer(OfferDAO offer) {
        return ((root, query, criteriaBuilder) -> {
            if (offer == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("offer"),
                    offer);
        });
    }

    public static Specification<OfferApplicationDAO> findByUser(UserDAO user) {
        return ((root, query, criteriaBuilder) -> {
            if (user == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get("offer").get("firm").get("user"), user);
        });
    }
}
