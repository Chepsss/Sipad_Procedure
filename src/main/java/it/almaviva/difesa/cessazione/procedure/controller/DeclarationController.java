package it.almaviva.difesa.cessazione.procedure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.model.camunda.CamundaVariable;
import it.almaviva.difesa.cessazione.procedure.model.common.RestApiError;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.DeclarationDTO;
import it.almaviva.difesa.cessazione.procedure.service.msproc.DeclarationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(Constant.DECLARATIONS_VISIBILITY_INDEX_URL)
@Slf4j
@Validated
@RequiredArgsConstructor
public class DeclarationController {

    private final DeclarationService declarationService;

    /**
     * {@code GET  /} : get all declarations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of DeclarationDTO.
     */
    @Operation(summary = "Get all declarations")
    @GetMapping("/declarations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<List<DeclarationDTO>> getAllDeclaration() {
        log.debug("REST request to get all declarations");
        return ResponseEntity.ok(declarationService.getAllDeclarations());
    }

    /**
     * {@code POST  /} : get declaration visibility.
     */
    @Operation(summary = "Get declaration visibility")
    @PostMapping("/declarationVisibility")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public List<DeclarationDTO> getDeclarationVisibility(@Valid @RequestBody Map<String, CamundaVariable> variables) {
        return declarationService.findAllDeclarationsByCodeList(variables);
    }

}
