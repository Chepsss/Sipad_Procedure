package it.almaviva.difesa.cessazione.procedure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.model.common.RestApiError;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpStComComuneDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpStnazNazioneDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpStproProvinciaDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpStregRegioneDTO;
import it.almaviva.difesa.cessazione.procedure.service.msproc.TpStcomComuneService;
import it.almaviva.difesa.cessazione.procedure.service.msproc.TpStnazNazioneService;
import it.almaviva.difesa.cessazione.procedure.service.msproc.TpStproProvinciaService;
import it.almaviva.difesa.cessazione.procedure.service.msproc.TpStregRegioneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(Constant.LOGICTYPE_INDEX_URL)
@Slf4j
@Validated
@RequiredArgsConstructor
public class PlaceController {

    private final TpStnazNazioneService tpStnazNazioneService;
    private final TpStregRegioneService tpStregRegioneService;
    private final TpStproProvinciaService tpStproProvinciaService;
    private final TpStcomComuneService tpStcomComuneService;

    /**
     * {@code GET  /} : get nations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of TpStnazNazioneDTO.
     */
    @Operation(summary = "Get for Nations")
    @GetMapping("/nation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<List<TpStnazNazioneDTO>> listNations() {
        log.debug("REST request to get all nations");
        return ResponseEntity.ok(tpStnazNazioneService.listNations());
    }

    /**
     * {@code GET  /} : get regions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of TpStregRegioneDTO.
     */
    @Operation(summary = "Get for Regions")
    @GetMapping("/region")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<List<TpStregRegioneDTO>> listRegions() {
        log.debug("REST request to get all regions");
        return ResponseEntity.ok(tpStregRegioneService.listRegions());
    }

    /**
     * {@code GET  /} : get provinces.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of TpStproProvinciaDTO.
     */
    @Operation(summary = "Get for Provinces")
    @GetMapping("/province")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<List<TpStproProvinciaDTO>> listProvinces() {
        log.debug("REST request to get all provinces");
        return ResponseEntity.ok(tpStproProvinciaService.listProvinces());
    }

    /**
     * {@code GET  /} : get cities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of TpStComComuneDTO.
     */
    @Operation(summary = "Get for Cities")
    @GetMapping("/city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<List<TpStComComuneDTO>> listCities() {
        log.debug("REST request to get all cities");
        return ResponseEntity.ok(tpStcomComuneService.listCities());
    }

    /**
     * {@code GET  /} : get cities by province.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of TpStComComuneDTO by province.
     */
    @Operation(summary = "Get for Cities by Province")
    @GetMapping("/city/{provinceId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<List<TpStComComuneDTO>> listCitiesByProvince(@PathVariable Long provinceId) {
        log.debug("REST request to get all cities by province : {}", provinceId);
        return ResponseEntity.ok(tpStcomComuneService.listCitiesByProvince(provinceId));
    }

}
