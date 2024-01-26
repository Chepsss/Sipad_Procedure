package it.almaviva.difesa.cessazione.procedure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.model.common.RestApiError;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.ProceduresArchiveDTO;
import it.almaviva.difesa.cessazione.procedure.service.msproc.ProcedureArchiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constant.PROCEDURES_ARCHIVE_URL)
@RequiredArgsConstructor
@Slf4j
public class ProceduresArchiveController {

    private final ProcedureArchiveService procedureArchiveService;

    @Operation(summary = "Get all procedures in progress from SIPAD")
    @GetMapping(value = "/inProgress/{codiceFiscale}/{idProcedimento}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<Page<ProceduresArchiveDTO>> getAllProceduresInProgress(@PathVariable String codiceFiscale, @PathVariable Long idProcedimento, Pageable pageable) {
        log.debug("REST request to get all procedures archive In progress by fiscal code");
        Page<ProceduresArchiveDTO> page = procedureArchiveService.getProceduresArchiveInProgressFromSIPAD(codiceFiscale, idProcedimento, pageable);
        return ResponseEntity.ok().body(page);
    }

    @Operation(summary = "Get all procedures closed from SIPAD")
    @GetMapping(value = "/closed/{codiceFiscale}/{idProcedimento}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<Page<ProceduresArchiveDTO>> getProceduresArchiveClosedFromSIPAD(@PathVariable String codiceFiscale, @PathVariable Long idProcedimento, Pageable pageable) {
        log.debug("REST request to get all procedures archive closed by fiscalcode");
        Page<ProceduresArchiveDTO> page = procedureArchiveService.getProceduresArchiveClosedInProgressFromSIPAD(codiceFiscale, idProcedimento, pageable);
        return ResponseEntity.ok().body(page);
    }

}
