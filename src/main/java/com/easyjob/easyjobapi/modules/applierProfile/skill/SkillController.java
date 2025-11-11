package com.easyjob.easyjobapi.modules.applierProfile.skill;

import com.easyjob.easyjobapi.modules.applierProfile.skill.models.SkillPageResponse;
import com.easyjob.easyjobapi.modules.applierProfile.skill.models.SkillRequest;
import com.easyjob.easyjobapi.modules.applierProfile.skill.services.SkillCreateService;
import com.easyjob.easyjobapi.modules.applierProfile.skill.services.SkillDeleteService;
import com.easyjob.easyjobapi.modules.applierProfile.skill.services.SkillEditService;
import com.easyjob.easyjobapi.modules.applierProfile.skill.services.SkillGetService;
import com.easyjob.easyjobapi.utils.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user/applier-profile/skill")
@RequiredArgsConstructor
public class SkillController {
    private final SkillCreateService skillCreateService;
    private final SkillEditService skillEditService;
    private final SkillGetService skillGetService;
    private final SkillDeleteService skillDeleteService;

    private final static String DEFAULT_RESPONSE = "Operation successful!";

    @PostMapping(value = "/")
    @Operation(
            description = "Add applier skill",
            summary = "Add applier skill"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> addSkill(
            @Valid @RequestBody SkillRequest request
    ) {
        skillCreateService.create(request);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping(value = "/{skill-id}")
    @Operation(
            description = "Edit applier skill",
            summary = "Edit applier skill"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> editSkill(
            @PathVariable("skill-id")UUID skillId,
            @Valid @RequestBody SkillRequest request
    ) {
        skillEditService.edit(request, skillId);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping(value = "/list")
    @Operation(
            description = "Get applier skills",
            summary = "Get applier skills"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<SkillPageResponse>> getSkills(
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page
    ) {
        SkillPageResponse response = skillGetService.get(page, limit);

        return new ResponseEntity<>(new CustomResponse<>(response, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }

    @DeleteMapping("/{skill-id}")
    @Operation(
            description = "Delete applier skill",
            summary = "Delete applier skill"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> deleteSkill(
            @PathVariable("skill-id") UUID skillId
    ) {
        skillDeleteService.delete(skillId);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }
}
