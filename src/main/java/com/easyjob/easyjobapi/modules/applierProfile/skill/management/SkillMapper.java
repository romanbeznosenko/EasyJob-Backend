package com.easyjob.easyjobapi.modules.applierProfile.skill.management;

import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileMapper;
import com.easyjob.easyjobapi.modules.applierProfile.skill.models.Skill;
import com.easyjob.easyjobapi.modules.applierProfile.skill.models.SkillDAO;
import com.easyjob.easyjobapi.modules.applierProfile.skill.models.SkillId;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), uses = {ApplierProfileMapper.class})
public interface SkillMapper {
    @Mapping(target = "id", expression = "java(toMap.getSkillId().getId())")
    SkillDAO mapToEntity(Skill toMap,
                         @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "skillId", source = "toMap", qualifiedByName = "longToObject")
    Skill mapToDomain(SkillDAO toMap,
                      @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Named("longToObject")
    default SkillId fromLongToObject(SkillDAO skillDAO) {
        return SkillId.of(skillDAO.getId());
    }
}