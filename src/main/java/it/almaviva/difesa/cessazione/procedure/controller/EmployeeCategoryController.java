package it.almaviva.difesa.cessazione.procedure.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.model.common.RestApiError;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.EmployeeCategoryDTO;
import it.almaviva.difesa.cessazione.procedure.service.msproc.TpSgtpoPosizioneStatoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constant.EMPLOYEE_CATEGORY_INDEX_URL)
@Slf4j
@Validated
@RequiredArgsConstructor
public class EmployeeCategoryController {

    private final TpSgtpoPosizioneStatoService posizioneStatoService;

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<EmployeeCategoryDTO> getAllEmployeeCategories() {
        log.debug("REST request to list of employee categories");
        return ResponseEntity.ok(posizioneStatoService.getCategories());
    }

}
