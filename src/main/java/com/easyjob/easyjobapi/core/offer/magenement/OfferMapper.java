package com.easyjob.easyjobapi.core.offer.magenement;

import com.easyjob.easyjobapi.core.firm.management.FirmMapper;
import com.easyjob.easyjobapi.core.offer.models.Offer;
import com.easyjob.easyjobapi.core.offer.models.OfferDAO;
import com.easyjob.easyjobapi.core.offer.models.OfferId;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), uses = {FirmMapper.class})
public interface OfferMapper {
    @Mapping(target = "id", expression = "java(toMap.getOfferId().getId())")
    OfferDAO mapToEntity(Offer toMap,
                         @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "offerId", source = "toMap", qualifiedByName = "longToObject")
    Offer mapToDomain(OfferDAO toMap,
                      @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Named("longToObject")
    default OfferId fromLongToObject(OfferDAO offerDAO) {
        return OfferId.of(offerDAO.getId());
    }
}