package it.almaviva.difesa.cessazione.procedure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.model.common.GenericResponse;
import it.almaviva.difesa.cessazione.procedure.model.common.RestApiError;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.ReasonCessationRequest;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.*;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.TpCegmlGiudMedLegaleDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.TpCetipCessazioneDTO;
import it.almaviva.difesa.cessazione.procedure.service.msproc.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(Constant.LOGICTYPE_INDEX_URL)
@Slf4j
@Validated
@RequiredArgsConstructor
public class LogicTypeController {

    private final TpStfaaForzaArmataService tpStfaaForzaArmataService;
    private final TpPrattAttivazioneService tpPrattAttivazioneService;
    private final TpPrtpoTprocedimentoService tpPrtpoTprocedimentoService;
    private final TpCetipCessazioneService tpCetipCessazioneService;
    private final TpSgtpoPosizioneStatoService tpSgtpoPosizioneStatoService;
    private final TpCegmlGiudMedLegaleService tpCegmlGiudMedLegaleService;
    private final TbStentEnteService tbStentEnteService;
    private final TpDocatCdocumentoService tpDocatCdocumentoService;
    private final PromTitOnorService promTitOnorService;

    private final RubricaCompletaService rubricaAooService;




    /**
     * {@code GET  /} : get armed Force.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of TpStfaaForzaArmataDTO.
     */
    @Operation(summary = "Get for Armed Force")
    @GetMapping("/armedforce")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<List<TpStfaaForzaArmataDTO>> getAllArmedForce() {
        log.debug("REST request to get all armed force");
        return ResponseEntity.ok(tpStfaaForzaArmataService.getAllArmedForce());
    }

    /**
     * {@code GET  /} : get opening cessation.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of TpPrattAttivazioneDTO.
     */
    @Operation(summary = "Get Procedures for opening the Cessation")
    @GetMapping("/openingcessation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<List<TpPrattAttivazioneDTO>> openingCessation() {
        log.debug("REST request to get opening cessation");
        return ResponseEntity.ok(tpPrattAttivazioneService.openingCessation());
    }

    /**
     * {@code GET  /} : get Type cessation.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of TpPrtpoTprocedimentoDTO.
     */
    @Operation(summary = "Get Type of access to Cessation")
    @GetMapping("/typecessation/{type}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<List<TpPrtpoTprocedimentoDTO>> getTypeCessation(@PathVariable @Validated String type) {
        log.debug("REST request to get type cessation");
        return ResponseEntity.ok(tpPrtpoTprocedimentoService.getTypeCessation(type));
    }

    /**
     * {@code GET  /} : get Reason for Cessation.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of TpCetipCessazioneDTO.
     */
    @Operation(summary = "Get Reason for Cessation")
    @GetMapping("/reasoncessation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<List<TpCetipCessazioneDTO>> getReasonCessation(@Validated ReasonCessationRequest request) {
        log.debug("REST request to get reason for cessation");
        return ResponseEntity.ok(tpCetipCessazioneService.getReasonOfCessation(request));
    }

    /**
     * {@code GET  /} : get Category of leave.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of TpSgtpoPosizioneStatoDTO.
     */
    @Operation(summary = "Get Category of leave by ID of reason for Cessation")
    @GetMapping("/categoryleave/{idReason}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<List<TpSgtpoPosizioneStatoDTO>> getCategoryLeave(@PathVariable @Validated Long idReason) {
        log.debug("REST request to get Category of leave");
        return ResponseEntity.ok(tpSgtpoPosizioneStatoService.getCategoryOfLeave(idReason));
    }

    /**
     * {@code GET  /} : get First Type Instance Of GML.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of TpCegmlGiudMedLegaleDTO.
     */
    @Operation(summary = "Get First Type Instance Of GML")
    @GetMapping("/firstinstancegml")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<List<TpCegmlGiudMedLegaleDTO>> getFirstTypeInstanceOfGML() {
        log.debug("REST request to get First Type Instance Of GML");
        return ResponseEntity.ok(tpCegmlGiudMedLegaleService.getFirstTypeInstanceOfGML());
    }

    /**
     * {@code GET  /} : get Second Type Instance Of GML.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of TpCegmlGiudMedLegaleDTO.
     */
    @Operation(summary = "Get Second Type Instance Of GML")
    @GetMapping("/secondinstancegml")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<List<TpCegmlGiudMedLegaleDTO>> getSecondTypeInstanceOfGML() {
        log.debug("REST request to get Second Type Instance Of GML");
        return ResponseEntity.ok(tpCegmlGiudMedLegaleService.getSecondTypeInstanceOfGML());
    }

    /**
     * {@code GET  /} : get authority by denomination.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of TbStentEnteDTO.
     */
    @Operation(summary = "Get authority by denomination")
    @GetMapping("/authority/{denom}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<List<TbStentEnteDTO>> getEnteByDenomEnte(@PathVariable @Validated String denom) {
        log.debug("REST request to get authority by denomination");
        return ResponseEntity.ok(tbStentEnteService.getEnteByDenomEnte(denom));
    }

    //cr16
    @GetMapping("/listRubrica/{denomEnteCC}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<List<RubricaCompletaDTO>> elencoRubricaCompletaByDenomCC(@PathVariable @Validated String denomEnteCC){
        log.debug("elencoRubricaCompletaByDenomCC");
        return ResponseEntity.ok().body(rubricaAooService.getRubricaCompletaDenomEnteCC(denomEnteCC));

    }

//fine cr16


    /**
     * {@code GET  /} : get communication types.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of TpDocatCdocumentoDTO.
     */
    @Operation(summary = "Get for communication types")
    @GetMapping("/communicationTypes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<List<TpDocatCdocumentoDTO>> listCommunicationTypes(@RequestParam("prtpoAcrProc") String prtpoAcrProc, @RequestParam("prattAcrAtt") String prattAcrAtt) {
        log.debug("REST request to get all communication Types");
        return ResponseEntity.ok(tpDocatCdocumentoService.listCommunicationTypes(prtpoAcrProc, prattAcrAtt));
    }

    /**
     * {@code GET  /} : get Category of leave for non admissibility.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of TpSgtpoPosizioneStatoDTO.
     */
    @Operation(summary = "Get Category of leave for non admissibility by ID of typology of Cessation")
    @GetMapping("/categoryleaveNonAdmissibility/{idTypeCessation}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<List<TpSgtpoPosizioneStatoDTO>> getCategoryLeaveNonAdmissibility(@PathVariable @Validated Long idTypeCessation) {
        log.debug("REST request to get Category of leave for non admissibility");
        return ResponseEntity.ok(tpSgtpoPosizioneStatoService.getCategoryOfLeaveNonAdmissibility(idTypeCessation));
    }

    @Operation(summary = "get default value for prom_tit_onor field")
    @GetMapping("/promTitOnorDefault/{typeCessation}/{employeeId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<GenericResponse> getTitOnor(@PathVariable @Validated String typeCessation, @PathVariable @Validated Long employeeId) {
        log.debug("REST request to get prom tit onor defauts values");
        return ResponseEntity.ok(promTitOnorService.promozioneATitoloOnorificoDefault(typeCessation, employeeId));
    }

}
