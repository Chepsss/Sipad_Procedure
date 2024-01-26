package it.almaviva.difesa.cessazione.procedure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.model.CustomUserDetail;
import it.almaviva.difesa.cessazione.procedure.model.common.RestApiError;
import it.almaviva.difesa.cessazione.procedure.model.common.RestApiResponse;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.ChangeStateProcRequest;
import it.almaviva.difesa.cessazione.procedure.service.msproc.ChangeProcedureStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(Constant.CHANGE_PROCEDURE_STATE_INDEX_URL)
@Slf4j
@RequiredArgsConstructor
public class ChangeProcedureStateController {

    private final ChangeProcedureStateService changeProcedureStateService;

    @Operation(summary = "Change procedure state")
    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<RestApiResponse> changeProcedureState(@Valid @RequestBody ChangeStateProcRequest request) {
        var userLogged = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Assert.notNull(userLogged, "User is not authenticated");
        RestApiResponse restApiResponse = changeProcedureStateService.changeProcedureState(request, userLogged);
        if (restApiResponse.getStatusCode() >= HttpStatus.BAD_REQUEST.value()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restApiResponse);
        } else {
            return ResponseEntity.ok(restApiResponse);
        }
    }

}
