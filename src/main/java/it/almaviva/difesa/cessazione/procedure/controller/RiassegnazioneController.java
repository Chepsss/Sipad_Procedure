package it.almaviva.difesa.cessazione.procedure.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.model.CustomUserDetail;
import it.almaviva.difesa.cessazione.procedure.model.common.RestApiError;
import it.almaviva.difesa.cessazione.procedure.model.common.RestApiResponse;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.RiassegnazioneDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.RiassegnazioneRequest;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.RiassegnazioneSearchRequest;
import it.almaviva.difesa.cessazione.procedure.service.msproc.RiassegnazioneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(Constant.RIASSEGNAZIONE_INDEX_URL)
@Slf4j
@Validated
@RequiredArgsConstructor
public class RiassegnazioneController {

    private final RiassegnazioneService riassegnazioneService;

    @PostMapping(value = "/search")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<Page<RiassegnazioneDTO>> searchProcedures(@RequestBody RiassegnazioneSearchRequest request, @ParameterObject Pageable pageable) {
        log.debug("REST request to search Procedures for reassignment");
        Page<RiassegnazioneDTO> page = riassegnazioneService.search(request, pageable);
        return ResponseEntity.ok().body(page);
    }

    @PostMapping(value = "/reassignment", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = RestApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    public ResponseEntity<RestApiResponse> updateProcedureAssignedTo(@Valid @RequestBody RiassegnazioneRequest assignedDto) {
        log.debug("REST request to reassign Procedures");
        var userLogged = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Assert.notNull(userLogged, "User is not authenticated");
        return ResponseEntity.ok().body(riassegnazioneService.reassignment(assignedDto));
    }

}
