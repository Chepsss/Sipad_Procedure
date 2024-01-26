package it.almaviva.difesa.cessazione.procedure.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.StateProcedure;
import it.almaviva.difesa.cessazione.procedure.model.common.RestApiError;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.StateProcedureDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.RoleDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.UserDTO;
import it.almaviva.difesa.cessazione.procedure.service.msproc.StateProcedureService;
import it.almaviva.difesa.cessazione.procedure.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link StateProcedure}.
 */
@RestController
@RequestMapping(Constant.STATE_PROCEDURE_INDEX_URL)
@Slf4j
@RequiredArgsConstructor
public class StateProcedureController {

    private final StateProcedureService stateProcedureService;

    /**
     * {@code GET  /} : get all the stateProcedures.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stateProcedures in body.
     */
    @GetMapping("/all")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<List<StateProcedureDTO>> getAllStateProcedures(@RequestParam(required = false) Long idPhase, @ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of StateProcedures");
        Page<StateProcedureDTO> page = stateProcedureService.findAll(idPhase, pageable);
        return ResponseEntity.ok().body(page.getContent());
    }

    /**
     * {@code GET  /:id} : get the "id" stateProcedure.
     *
     * @param id the id of the stateProcedureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stateProcedureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<StateProcedureDTO> getStateProcedure(@PathVariable Long id) {
        log.debug("REST request to get StateProcedure : {}", id);
        Optional<StateProcedureDTO> stateProcedureDTO = stateProcedureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stateProcedureDTO);
    }

    @GetMapping("/listNextStates/{procedureId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public Page<StateProcedureDTO> listNextStates(@PathVariable Long procedureId) {
        log.debug("REST request to get the list of next status");
        return stateProcedureService.listNextStates(procedureId);
    }

    @GetMapping("/listUsersByStatusAndCatPers/{procedureId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public List<UserDTO> listUsersByStatusAndCatPers(@PathVariable Long procedureId, Long procedureStateId, Long idCatPers) {
        log.debug("REST request to get the list users by state");
        return stateProcedureService.listUsersByStatusAndCatMilitare(procedureId, procedureStateId, idCatPers);
    }

    @GetMapping("/listRolesByStatus/{currentStateCode}/{codeCatPers}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public List<RoleDTO> listRolesByStatus(@PathVariable String currentStateCode, @PathVariable String codeCatPers) {
        log.debug("REST request to get the list roles by current state of procedure");
        return stateProcedureService.listRolesByStatus(currentStateCode, codeCatPers);
    }

    /**
     * {@code GET  /} : get State filtered.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stateProcedures in body.
     */
    @GetMapping("/stateFiltered")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<List<StateProcedureDTO>> getStateFiltered() {
        log.debug("REST request to get a page of StateProcedures");
        return ResponseEntity.ok().body(stateProcedureService.findAllStateFiltered());
    }

}
