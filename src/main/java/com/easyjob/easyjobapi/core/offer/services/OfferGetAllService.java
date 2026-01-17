package com.easyjob.easyjobapi.core.offer.services;

import com.easyjob.easyjobapi.core.offer.magenement.OfferManager;
import com.easyjob.easyjobapi.core.offer.magenement.OfferSpecifications;
import com.easyjob.easyjobapi.core.offer.models.OfferDAO;
import com.easyjob.easyjobapi.core.offer.models.OfferPageResponse;
import com.easyjob.easyjobapi.core.offer.models.OfferResponse;
import com.easyjob.easyjobapi.core.user.management.UserMapper;
import com.easyjob.easyjobapi.files.storage.services.StorageService;
import com.easyjob.easyjobapi.utils.enums.EmploymentTypeEnum;
import com.easyjob.easyjobapi.utils.enums.ExperienceLevelEnum;
import com.easyjob.easyjobapi.utils.enums.WorkModeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OfferGetAllService {
    private final OfferManager offerManager;
    private final StorageService storageService;
    private final UserMapper userMapper;

    public OfferPageResponse getAllOffers(int page, int limit,
                                          List<ExperienceLevelEnum> experienceLevels,
                                          List<EmploymentTypeEnum> employmentTypes,
                                          List<WorkModeEnum> workModes,
                                          List<String> skills,
                                          Long salaryBottom,
                                          Long salaryTop,
                                          String name) {
        log.info("Get All Offers");

        Pageable pageable = PageRequest.of(page - 1, limit);
        Specification<OfferDAO> specification = OfferSpecifications.byEmploymentType(employmentTypes)
                .and(OfferSpecifications.bySkillsAll(skills))
                .and(OfferSpecifications.byExperience(experienceLevels))
                .and(OfferSpecifications.byWorkMode(workModes))
                .and(OfferSpecifications.bySalaryRange(salaryBottom, salaryTop))
                .and(OfferSpecifications.byNameLike(name));

        Page<OfferDAO> offerDAOS = offerManager.findAll(specification, pageable);

        List<OfferResponse> offerResponses = offerDAOS.get()
                .filter(item -> item.getIsArchived() == false)
                .map(item -> OfferBuilders.buildResponse(
                        item,
                        storageService,
                        userMapper
                ))
                .toList();

        return OfferPageResponse.builder()
                .count(offerDAOS.getTotalElements())
                .data(offerResponses)
                .build();
    }
}
