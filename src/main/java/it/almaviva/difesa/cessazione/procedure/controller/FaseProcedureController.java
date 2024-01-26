package it.almaviva.difesa.cessazione.procedure.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.FaseProcedure;
import it.almaviva.difesa.cessazione.procedure.model.common.RestApiError;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.FaseProcedureDTO;
import it.almaviva.difesa.cessazione.procedure.service.msproc.FaseProcedureService;
import it.almaviva.difesa.cessazione.procedure.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link FaseProcedure}.
 */
@RestController
@RequestMapping(Constant.FASE_PROCEDURE_INDEX_URL)
@Slf4j
@Validated
@RequiredArgsConstructor
public class FaseProcedureController {

    private final FaseProcedureService faseProcedureService;

    /**
     * {@code GET  /} : get all the faseProcedures.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of faseProcedures in body.
     */
    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<List<FaseProcedureDTO>> getAllFaseProcedures(@ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of FaseProcedures");
        Page<FaseProcedureDTO> page = faseProcedureService.findAll(pageable);
        return ResponseEntity.ok().body(page.getContent());
    }

    /**
     * {@code GET  /:id} : get the "id" faseProcedure.
     *
     * @param id the id of the faseProcedureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the faseProcedureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<FaseProcedureDTO> getFaseProcedure(@PathVariable Long id) {
        log.debug("REST request to get FaseProcedure : {}", id);
        Optional<FaseProcedureDTO> faseProcedureDTO = faseProcedureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(faseProcedureDTO);
    }

}
