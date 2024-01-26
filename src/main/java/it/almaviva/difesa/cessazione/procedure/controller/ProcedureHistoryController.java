package it.almaviva.difesa.cessazione.procedure.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.ProcedureHistory;
import it.almaviva.difesa.cessazione.procedure.model.common.RestApiError;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.ProcedureHistoryDTO;
import it.almaviva.difesa.cessazione.procedure.service.msproc.ProcedureHistoryService;
import it.almaviva.difesa.cessazione.procedure.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link ProcedureHistory}.
 */
@RestController
@RequestMapping(Constant.PROCEDURE_HISTORY_INDEX_URL)
@Slf4j
@RequiredArgsConstructor
public class ProcedureHistoryController {

    private final ProcedureHistoryService procedureHistoryService;

    /**
     * {@code POST  /} : Create a new procedureHistory.
     *
     * @param procedureHistoryDTO the procedureHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the new procedureHistoryDTO, or with status {@code 400 (Bad Request)} if the procedureHistory has already an ID.
     */
    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<ProcedureHistoryDTO> createProcedureHistory(@Valid @RequestBody ProcedureHistoryDTO procedureHistoryDTO) {
        log.debug("REST request to save ProcedureHistory : {}", procedureHistoryDTO);
        ProcedureHistoryDTO result = procedureHistoryService.saveOrUpdate(procedureHistoryDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * {@code PUT  /:id} : Updates an existing procedureHistory.
     *
     * @param id                  the id of the procedureHistoryDTO to save.
     * @param procedureHistoryDTO the procedureHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated procedureHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the procedureHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the procedureHistoryDTO couldn't be updated.
     */
    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<ProcedureHistoryDTO> updateProcedureHistory(@PathVariable(value = "id", required = false) final Long id,
                                                                      @Valid @RequestBody ProcedureHistoryDTO procedureHistoryDTO) {
        log.debug("REST request to update ProcedureHistory : {}, {}", id, procedureHistoryDTO);
        ProcedureHistoryDTO result = procedureHistoryService.saveOrUpdate(procedureHistoryDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * {@code GET  /procedure-histories} : get all the procedureHistories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of procedureHistories in body.
     */
    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<List<ProcedureHistoryDTO>> getAllProcedureHistories(@ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ProcedureHistories");
        Page<ProcedureHistoryDTO> page = procedureHistoryService.findAll(pageable);
        return ResponseEntity.ok().body(page.getContent());
    }

    /**
     * {@code GET  /:id} : get the "id" procedureHistory.
     *
     * @param id the id of the procedureHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the procedureHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<ProcedureHistoryDTO> getProcedureHistory(@PathVariable Long id) {
        log.debug("REST request to get ProcedureHistory : {}", id);
        Optional<ProcedureHistoryDTO> procedureHistoryDTO = procedureHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(procedureHistoryDTO);
    }

    /**
     * {@code DELETE  /:id} : delete the "id" procedureHistory.
     *
     * @param id the id of the procedureHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<Void> deleteProcedureHistory(@PathVariable Long id) {
        log.debug("REST request to delete ProcedureHistory : {}", id);
        procedureHistoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
