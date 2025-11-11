package com.easyjob.easyjobapi.modules.applierProfile.project;

import com.easyjob.easyjobapi.modules.applierProfile.project.models.ProjectPageResponse;
import com.easyjob.easyjobapi.modules.applierProfile.project.models.ProjectRequest;
import com.easyjob.easyjobapi.modules.applierProfile.project.services.ProjectCreateService;
import com.easyjob.easyjobapi.modules.applierProfile.project.services.ProjectDeleteService;
import com.easyjob.easyjobapi.modules.applierProfile.project.services.ProjectEditService;
import com.easyjob.easyjobapi.modules.applierProfile.project.services.ProjectGetService;
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
@RequestMapping("/api/user/applier-profile/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectGetService projectGetService;
    private final ProjectEditService projectEditService;
    private final ProjectDeleteService projectDeleteService;
    private final ProjectCreateService projectCreateService;

    private final static String DEFAULT_RESPONSE = "Operation successful!";

    @PostMapping(value = "/")
    @Operation(
            description = "Create applier project",
            summary = "Create applier project"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> create(
            @Valid @RequestBody ProjectRequest request
    ) {
        projectCreateService.create(request);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping(value = "/{project-id}")
    @Operation(
            description = "Edit applier project",
            summary = "Edit applier profile"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> edit(
            @PathVariable("project-id")UUID projectId,
            @Valid @RequestBody ProjectRequest request
    ) {
        projectEditService.edit(request, projectId);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping(value = "/list")
    @Operation(
            description = "Get applier projects",
            summary = "Get applier projects"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<ProjectPageResponse>> get(
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page
    ){
        ProjectPageResponse response = projectGetService.get(limit, page);

        return new ResponseEntity<>(new CustomResponse<>(response, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{project-id}")
    @Operation(
            description = "Delete applier project",
            summary = "Delete applier project"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> delete(
            @PathVariable("project-id") UUID projectId
    ) {
        projectDeleteService.delete(projectId);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }
}
