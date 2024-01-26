package it.almaviva.difesa.cessazione.procedure.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.TbCeProcParereRag;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.TbCeProcPensione;
import it.almaviva.difesa.cessazione.procedure.model.common.RestApiError;
import it.almaviva.difesa.cessazione.procedure.model.common.RestApiResponse;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.ProcedureCompleteDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.ProcedureDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.ProcedureOpeningDataDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.AddressElectedRequest;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.DeclarationProcWrapper;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.NoteRequest;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.ParereRagRequest;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.PensionRequest;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.PriorityRequest;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.ProcedureSearchRequest;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.TbCeProcTransitoDTORequest;
import it.almaviva.difesa.cessazione.procedure.service.msproc.ProcedureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * REST controller for managing {@link Procedure}.
 */
@RestController
@RequestMapping(Constant.PROCEDURE_INDEX_URL)
@Slf4j
@Validated
@RequiredArgsConstructor
public class ProcedureController {

    private final ProcedureService procedureService;

    /**
     * {@code POST  /} : Create a new procedure.
     *
     * @param procedureOpeningDataDTO the procedureOpeningDataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new procedureOpeningDataDTO, or with status {@code 400 (Bad Request)} if the procedure has already an ID.
     */
    @PostMapping("/createOpeningData")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<ProcedureDTO> createProcedureOpeningData(@Valid @RequestBody ProcedureOpeningDataDTO procedureOpeningDataDTO) {
        log.debug("REST request to create Procedure opening data : {}", procedureOpeningDataDTO);
        ProcedureDTO result = procedureService.createOrUpdateProcedureOpeningData(procedureOpeningDataDTO);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/updateOpeningData")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<ProcedureDTO> updateProcedureOpeningData(@Valid @RequestBody ProcedureOpeningDataDTO procedureOpeningDataDTO) {
        log.debug("REST request to update Procedure opening data : {}", procedureOpeningDataDTO);
        ProcedureDTO result = procedureService.createOrUpdateProcedureOpeningData(procedureOpeningDataDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * {@code GET  /} : get all procedures.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of procedures in body.
     */
    @PostMapping(value = "/all")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<Page<ProcedureCompleteDTO>> getAllProcedures(@RequestBody ProcedureSearchRequest procedureSearchRequest, @ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Procedures");
        Page<ProcedureCompleteDTO> page = procedureService.findAll(procedureSearchRequest, pageable);
        return ResponseEntity.ok().body(page);
    }

    /**
     * {@code GET  /:id} : get the procedure by "id".
     *
     * @param id the id of the procedureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the procedureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<ProcedureDTO> getProcedure(@PathVariable Long id) {
        log.debug("REST request to get Procedure : {}", id);
        ProcedureDTO procedureDTO = procedureService.findOne(id);
        return ResponseEntity.ok().body(procedureDTO);
    }

    /**
     * {@code DELETE  /:id} : delete the procedure by "id".
     *
     * @param id the id of the procedureDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<Void> deleteProcedure(@PathVariable Long id) {
        log.debug("REST request to delete Procedure : {}", id);
        procedureService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * {@code POST  /saveAddressElected} : Save the address elected on the procedure.
     *
     * @param request the addressElectedRequest to save on the procedure.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the saved procedureDTO,
     * or with status {@code 400 (Bad Request)} if the addressElectedRequest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the address elected couldn't be saved.
     */
    @PostMapping(value = "/saveAddressElected", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<ProcedureDTO> saveAddressElected(@Valid @RequestBody AddressElectedRequest request) {
        log.debug("REST request to save Address elected : {}", request);
        ProcedureDTO result = procedureService.saveOrUpdateAddressElected(request);
        return ResponseEntity.ok(result);
    }

    /**
     * {@code PUT  /updateAddressElected} : Update the address elected on the procedure.
     *
     * @param request the addressElectedRequest to update on the procedure.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated procedureDTO,
     * or with status {@code 400 (Bad Request)} if the addressElectedRequest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the address elected couldn't be updated.
     */
    @PutMapping(value = "/updateAddressElected", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<ProcedureDTO> updateAddressElected(@Valid @RequestBody AddressElectedRequest request) {
        log.debug("REST request to update Address elected : {}", request);
        ProcedureDTO result = procedureService.saveOrUpdateAddressElected(request);
        return ResponseEntity.ok(result);
    }

    /**
     * {@code POST  /saveDeclarations} : Save the declaration on the relational table DeclarationProcedure.
     *
     * @param request the declarationProc to save on the REL table DeclarationProcedure.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the saved procedureDTO,
     * or with status {@code 400 (Bad Request)} if the declarationProc is not valid,
     * or with status {@code 500 (Internal Server Error)} if the declarations couldn't be saved.
     */
    @PostMapping(value = "/saveDeclarations", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<ProcedureDTO> saveDeclarations(@Valid @RequestBody DeclarationProcWrapper request) {
        log.debug("REST request to save declarations : {}", request);
        ProcedureDTO result = procedureService.saveOrUpdateDeclarations(request);
        return ResponseEntity.ok(result);
    }

    /**
     * {@code PUT  /updateDeclarations} : Update the declaration on the relational table DeclarationProcedure.
     *
     * @param request the declarationProc to update on the REL table DeclarationProcedure.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated procedureDTO,
     * or with status {@code 400 (Bad Request)} if the declarationProc is not valid,
     * or with status {@code 500 (Internal Server Error)} if the declarations couldn't be updated.
     */
    @PutMapping(value = "/updateDeclarations", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<ProcedureDTO> updateDeclarations(@Valid @RequestBody DeclarationProcWrapper request) {
        log.debug("REST request to update declarations : {}", request);
        ProcedureDTO result = procedureService.saveOrUpdateDeclarations(request);
        return ResponseEntity.ok(result);
    }

    /**
     * {@code POST  /saveTransition} : Save the data on the relational table TcCeProcTransito.
     *
     * @param request the transitoProc to save on the REL table TcCeProcTransito.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the saved procedureDTO,
     * or with status {@code 400 (Bad Request)} if the transitoProc is not valid,
     * or with status {@code 500 (Internal Server Error)} if the data couldn't be saved.
     */
    @PostMapping(value = "/saveTransition", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<ProcedureDTO> saveTransition(@Valid @RequestBody TbCeProcTransitoDTORequest request) {
        log.debug("REST request to save transition : {}", request);
        ProcedureDTO result = procedureService.saveOrUpdateTransition(request);
        return ResponseEntity.ok(result);
    }

    /**
     * {@code PUT  /updateTransition} : Update the Transition on the relational table TcCeProcTransito.
     *
     * @param request the transitoProc to update on the REL table TcCeProcTransito.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated procedureDTO,
     * or with status {@code 400 (Bad Request)} if the transitoProc is not valid,
     * or with status {@code 500 (Internal Server Error)} if the data couldn't be updated.
     */
    @PutMapping(value = "/updateTransition", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<ProcedureDTO> updateTransition(@Valid @RequestBody TbCeProcTransitoDTORequest request) {
        log.debug("REST request to update transition : {}", request);
        ProcedureDTO result = procedureService.saveOrUpdateTransition(request);
        return ResponseEntity.ok(result);
    }

    /**
     * {@code POST  /savePension} : Save the Pension on the TbCeProcPensione table.
     *
     * @param request the pensionRequest to save on the {@link TbCeProcPensione} table.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the saved procedureDTO,
     * or with status {@code 400 (Bad Request)} if the pensionRequest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pension couldn't be saved.
     */
    @PostMapping(value = "/savePension", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<ProcedureDTO> savePension(@Valid @RequestBody PensionRequest request) {
        log.debug("REST request to save pension : {}", request);
        ProcedureDTO result = procedureService.saveOrUpdatePensione(request);
        return ResponseEntity.ok(result);
    }

    /**
     * {@code PUT  /updatePension} : Update the Pension on the TbCeProcPensione table.
     *
     * @param request the pensionRequest to update on the TbCeProcPensione table.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated procedureDTO,
     * or with status {@code 400 (Bad Request)} if the pensionRequest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pension couldn't be updated.
     */
    @PutMapping(value = "/updatePensione", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<ProcedureDTO> updatePension(@Valid @RequestBody PensionRequest request) {
        log.debug("REST request to update pension : {}", request);
        ProcedureDTO result = procedureService.saveOrUpdatePensione(request);
        return ResponseEntity.ok(result);
    }

    /**
     * {@code POST  /saveParereRag} : Save the Parere Ragioneria on the TbCeProcParereRag table.
     *
     * @param request the parereRagRequest to save on the {@link TbCeProcParereRag} table.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the saved procedureDTO,
     * or with status {@code 400 (Bad Request)} if the parereRagRequest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parere ragioneria couldn't be saved.
     */
    @PostMapping(value = "/saveParereRag", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<ProcedureDTO> saveParereRag(@Valid @RequestBody ParereRagRequest request) {
        log.debug("REST request to save parere ragioneria : {}", request);
        ProcedureDTO result = procedureService.saveOrUpdateParereRag(request);
        return ResponseEntity.ok(result);
    }

    /**
     * {@code PUT  /updateParereRag} : Update the Parere Ragioneria on the TbCeProcParereRag table.
     *
     * @param request the parereRagRequest to update on the TbCeProcParereRag table.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated procedureDTO,
     * or with status {@code 400 (Bad Request)} if the parereRagRequest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parere ragioneria couldn't be updated.
     */
    @PutMapping(value = "/updateParereRag", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<ProcedureDTO> updateParereRag(@Valid @RequestBody ParereRagRequest request) {
        log.debug("REST request to update parere ragioneria : {}", request);
        ProcedureDTO result = procedureService.saveOrUpdateParereRag(request);
        return ResponseEntity.ok(result);
    }

    /**
     * {@code PUT  /saveNote} : Save procedure note.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)},
     * or with status {@code 400 (Bad Request)} if the procedureId is not valid,
     * or with status {@code 500 (Internal Server Error)} if the note couldn't be saved.
     */
    @PutMapping(value = "/saveNote", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<RestApiResponse> saveProcedureNote(@RequestBody NoteRequest request) {
        log.debug("REST request to save procedure note");
        RestApiResponse restApiResponse = procedureService.saveNote(request);
        return ResponseEntity.ok(restApiResponse);
    }

    /**
     * {@code PUT  /deleteNote} : Delete procedure note.
     *
     * @return void with status {@code 200 (OK)},
     * or with status {@code 400 (Bad Request)} if the procedureId is not valid,
     * or with status {@code 500 (Internal Server Error)} if the note couldn't be deleted.
     */
    @PutMapping(value = "/deleteNote", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<Void> deleteProcedureNote(@RequestParam Long procedureId) {
        log.debug("REST request to delete procedure note");
        procedureService.deleteNote(procedureId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/proceduresNotClosedByIdAssignedTo/{idAssignedTo}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<List<ProcedureDTO>> getProceduresNotClosedByIdAssignedTo(@PathVariable Long idAssignedTo) {
        log.debug("REST request to get procedures not closed");
        List<ProcedureDTO> procedureDTOS = procedureService.getProceduresNotClosedByIdAssignedTo(idAssignedTo);
        return ResponseEntity.ok(procedureDTOS);
    }

    /**
     * {@code PUT  /updatePriority} : Update procedure priority.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)},
     * or with status {@code 400 (Bad Request)} if the procedureId is not valid,
     * or with status {@code 500 (Internal Server Error)} if the priority couldn't be updated.
     */
    @PutMapping(value = "/updatePriority", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<ProcedureDTO> updateProcedurePriority(@RequestBody PriorityRequest request) {
        log.debug("REST request to update procedure priority");
        ProcedureDTO result = procedureService.updatePriority(request);
        return ResponseEntity.ok(result);
    }

}
