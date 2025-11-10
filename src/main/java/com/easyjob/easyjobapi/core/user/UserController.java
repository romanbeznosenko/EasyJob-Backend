package com.easyjob.easyjobapi.core.user;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.easyjob.easyjobapi.core.user.management.UserNotFoundException;
import com.easyjob.easyjobapi.core.user.models.UserDeleteRequest;
import com.easyjob.easyjobapi.core.user.models.UserEditRequest;
import com.easyjob.easyjobapi.core.user.models.UserResponse;
import com.easyjob.easyjobapi.core.user.services.UserDeleteService;
import com.easyjob.easyjobapi.core.user.services.UserEditService;
import com.easyjob.easyjobapi.core.user.services.UserGetService;
import com.easyjob.easyjobapi.utils.CustomResponse;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserGetService userGetService;
    private final UserEditService userEditService;
    private final UserDeleteService userDeleteService;

    @GetMapping(value = "/user")
    @Operation(
            summary = "Get information about user",
            description = "Get information about logged user"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<UserResponse>> getUerDetails() {
        UserResponse serviceResponse = userGetService.getUserDetails();
        return new ResponseEntity<>(new CustomResponse<>(serviceResponse, "Operation successful", HttpStatus.OK),
                                    HttpStatus.OK);
    }

    @PatchMapping(value = "/user")
    @Operation(
            summary = "Edit information about user",
            description = "Edit information about logged user"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> editUerDetails(
            @RequestBody @Valid UserEditRequest userEditRequest
    ) {

        userEditService.editUserDetails(userEditRequest);
        return ResponseEntity.ok(new CustomResponse<>(null, "User details updated successfully", HttpStatus.OK));

    }

    @DeleteMapping(value = "/user")
    @Operation(
            summary = "Delete user",
            description = "Delete user"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> deleteUser(
            @RequestBody @Valid UserDeleteRequest userDeleteRequest
    ) throws UserNotFoundException {
        userDeleteService.deleteUser(userDeleteRequest);
        return new ResponseEntity<>(new CustomResponse<>(null, "User deleted successfully", HttpStatus.OK),
                                    HttpStatus.OK);
    }
}
