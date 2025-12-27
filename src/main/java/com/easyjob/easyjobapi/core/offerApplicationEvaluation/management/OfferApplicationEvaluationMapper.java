package com.easyjob.easyjobapi.core.offerApplicationEvaluation.management;

import com.easyjob.easyjobapi.core.offer.magenement.OfferMapper;
import com.easyjob.easyjobapi.core.offerApplicationEvaluation.models.OfferApplicationEvaluation;
import com.easyjob.easyjobapi.core.offerApplicationEvaluation.models.OfferApplicationEvaluationDAO;
import com.easyjob.easyjobapi.core.offerApplicationEvaluation.models.OfferApplicationEvaluationId;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileMapper;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), uses = {OfferMapper.class, ApplierProfileMapper.class})
public interface OfferApplicationEvaluationMapper {
    @Mapping(target = "id", expression = "java(toMap.getOfferApplicationEvaluationId().getId())")
    OfferApplicationEvaluationDAO mapToEntity(OfferApplicationEvaluation toMap,
                                              @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "offerApplicationEvaluationId", source = "toMap", qualifiedByName = "longToObject")
    OfferApplicationEvaluation mapToDomain(OfferApplicationEvaluationDAO toMap,
                                           @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Named("longToObject")
    default OfferApplicationEvaluationId fromLongToObject(OfferApplicationEvaluationDAO offerApplicationEvaluationDAO) {
        return OfferApplicationEvaluationId.of(offerApplicationEvaluationDAO.getId());
    }
}