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
import it.almaviva.difesa.cessazione.procedure.service.msproc.ChangeProcedureStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constant.CLOSE_PROCEDURE_INDEX_URL)
@Slf4j
@Validated
@RequiredArgsConstructor
public class CloseProcedureController {

    private final ChangeProcedureStateService changeProcedureStateService;

    @Operation(summary = "Retry close procedures operations")
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<RestApiResponse> retryCloseProcedureOperations(@PathVariable Long id) {
        var userLogged = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        assert userLogged != null;
        return ResponseEntity.ok().body(changeProcedureStateService.retryCloseProceduresOperations(id, userLogged));
    }

}
