package it.almaviva.difesa.cessazione.procedure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.model.common.RestApiError;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.DeclarationProcedureDTOResponse;
import it.almaviva.difesa.cessazione.procedure.service.msproc.DeclarationProcedureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(Constant.DECLARATIONS_PROCEDURE_INDEX_URL)
@Slf4j
@Validated
@RequiredArgsConstructor
public class DeclarationProcedureController {

    private final DeclarationProcedureService declarationProcedureService;

    /**
     * {@code GET  /} : get all declarations By id procedure.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of DeclarationProcedureDTO.
     */
    @Operation(summary = "Get all declarations By id procedure")
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<List<DeclarationProcedureDTOResponse>> getAllDeclarationsByIdProcedure(@PathVariable @Min(1) Long id) {
        log.debug("REST request to get all declarations By id procedure");
        return ResponseEntity.ok(declarationProcedureService.getAllDeclarationsByIdProcedure(id));
    }

}
