package com.easyjob.easyjobapi.modules.applierProfile.workExperience.management;

import com.easyjob.easyjobapi.utils.CustomResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class WorkExperienceExceptionHandler {
    @ExceptionHandler(WorkExperienceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404", description = "Work experience not found in the system.",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                      {
                                      "data": null,
                                      "message": "Work experience not found in the system.",
                                      "status": "404 NOT_FOUND"
                                    }
                                    """
                            )
                    )),
    })
    public ResponseEntity<CustomResponse<String>> handleWorkExperienceNotFoundException(WorkExperienceNotFoundException ex) {
        CustomResponse<String> response = new CustomResponse<>(null, ex.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WorkExperienceUserMismatchException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "403", description = "Work Experience User mismatch.",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                      {
                                      "data": null,
                                      "message": "Work Experience User mismatch.",
                                      "status": "403 FORBIDDEN"
                                    }
                                    """
                            )
                    )),
    })
    public ResponseEntity<CustomResponse<String>> handleWorkExperienceUserMismatch(WorkExperienceUserMismatchException ex) {
        CustomResponse<String> response = new CustomResponse<>(null, ex.getMessage(), HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
