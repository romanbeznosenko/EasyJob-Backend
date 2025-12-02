package com.easyjob.easyjobapi.core.offerApplication.management;

import com.easyjob.easyjobapi.core.offer.magenement.OfferMapper;
import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplication;
import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplicationDAO;
import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplicationId;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileMapper;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), uses = {OfferMapper.class, ApplierProfileMapper.class})
public interface OfferApplicationMapper {
    @Mapping(target = "id", expression = "java(toMap.getOfferApplicationId().getId())")
    OfferApplicationDAO mapToEntity(OfferApplication toMap,
                                    @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "offerApplicationId", source = "toMap", qualifiedByName = "longToObject")
    OfferApplication mapToDomain(OfferApplicationDAO toMap,
                                 @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Named("longToObject")
    default OfferApplicationId fromLongToObject(OfferApplicationDAO offerApplicationDAO) {
        return OfferApplicationId.of(offerApplicationDAO.getId());
    }
}