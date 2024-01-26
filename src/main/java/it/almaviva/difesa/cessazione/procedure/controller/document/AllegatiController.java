package it.almaviva.difesa.cessazione.procedure.controller.document;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.model.common.CustomUserDetailDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.allegati.AllegatiList;
import it.almaviva.difesa.cessazione.procedure.model.dto.allegati.DocProtocollatiInputDto;
import it.almaviva.difesa.cessazione.procedure.model.dto.allegati.DocumentPredisposizioneDto;
import it.almaviva.difesa.cessazione.procedure.model.dto.allegati.DocumentProtocollatoDto;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.VwSg155StgiurFastMiCiDTO;
import it.almaviva.difesa.cessazione.procedure.service.SecurityService;
import it.almaviva.difesa.cessazione.procedure.service.msproc.allegati.AttachmentsService;
import it.almaviva.difesa.cessazione.procedure.service.msproc.document.AllegatoDocService;
import it.almaviva.difesa.cessazione.procedure.service.msproc.document.DocumentService;
import it.almaviva.difesa.cessazione.procedure.service.rest.SipadClient;
import it.almaviva.difesa.documenti.document.model.dto.request.allegati.AllegatoAdhocInput;
import it.almaviva.difesa.documenti.document.model.dto.request.allegati.AssociaProtocolloInput;
import it.almaviva.difesa.documenti.document.model.dto.request.allegati.ListAllegatiInput;
import it.almaviva.difesa.documenti.document.model.dto.request.allegati.UploadAllegato;
import it.almaviva.difesa.documenti.document.model.dto.response.allegati.AllegatiOutput;
import it.almaviva.difesa.documenti.document.model.dto.response.documenti.FileOutput;
import it.almaviva.difesa.documenti.document.model.dto.response.documenti.SuccessOutput;
import it.almaviva.difesa.documenti.document.service.AllegatiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

@RestController
@RequestMapping(Constant.ALLEGATI_INDEX_URL)
@Slf4j
@Validated
@RequiredArgsConstructor
public class AllegatiController {

    private final AllegatiService allegatiService;
    private final SipadClient sipadClient;
    private final SecurityService securityService;
    private final AttachmentsService attachmentsService;
    private final AllegatoDocService allegatoDocService;
    private final DocumentService documentService;

    @GetMapping(value = "/{idProcedimento}")
    public ResponseEntity<AllegatiList> list(@PathVariable @Validated Long idProcedimento) {
        log.debug("Lista allegati");
        return ResponseEntity.ok().body(attachmentsService.list(idProcedimento));
    }

    @GetMapping(value = "/allegato/{idAllegato}")
    public ResponseEntity<FileOutput> getAllegatoById(@PathVariable @Validated Long idAllegato) {
        log.debug("Request Lista allegati {}", idAllegato);
        Long userLoggedEmployeeId = securityService.getEmployeeIdOfUserLogged();
        VwSg155StgiurFastMiCiDTO userDetail = sipadClient.getUserLoggedDetailByEmpId(userLoggedEmployeeId);
        return ResponseEntity.ok().body(allegatiService.getAllegatoById(idAllegato, userDetail.getSg155CodiceFiscale()));
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<AllegatiOutput> upload(@Valid @RequestBody UploadAllegato uploadFileDto) {
        log.debug("Request Upload allegati {}", uploadFileDto);

        /* Recupero l'utente collegato */
        CustomUserDetailDTO userLogged = securityService.getUserDetails();
        uploadFileDto.setNomeCognomeUtenteCreazione(userLogged.getFirstName() + " " + userLogged.getLastName());

        AllegatiOutput output = allegatiService.upload(uploadFileDto);
        Long idProcedimento = uploadFileDto.getIdProcedimento();
        documentService.updateProcedureLastModified(idProcedimento, null);
        return ResponseEntity.ok().body(output);
    }

    @PostMapping(value = "/allega")
    public ResponseEntity<List<AllegatiOutput>> allega(@Valid @RequestBody ListAllegatiInput input) {
        log.debug("Request Allega allegati {}", input);

        /* Recupero l'utente collegato e lo setto su tutti gli allegati */
        CustomUserDetailDTO userLogged = securityService.getUserDetails();
        if(input != null && input.getAllegati() != null) {
            for(AllegatoAdhocInput item : input.getAllegati()) {
                item.setNomeCognomeUtenteCreazione(userLogged.getFirstName() + " " + userLogged.getLastName());
            }
        }

        return ResponseEntity.ok().body(allegatiService.allega(input));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<SuccessOutput> delete(@Valid @PathVariable Long id) {
        log.debug("Delete allegati");
        return ResponseEntity.ok().body(allegatiService.delete(id));
    }

    @PutMapping(value = "/associaProtocolloUscita")
    public ResponseEntity<AllegatiOutput> associaProtocolloUscita(@Valid @RequestBody AssociaProtocolloInput input) {
        log.debug("Request associaProtocolloUscita: {}", input);
        return ResponseEntity.ok().body(allegatiService.bindOutgoingProtocol(input));
    }

    @GetMapping(value = {"/docProtocollati/{idProc}/{idAllegato}"})
    public ResponseEntity<Page<DocumentProtocollatoDto>> docProtocollati(@PathVariable Long idProc,
                                                                         @PathVariable Long idAllegato,
                                                                         @ParameterObject Pageable pageable) {
        return ResponseEntity.ok().body(allegatoDocService.listWithPredInUscita(idProc, idAllegato, pageable));
    }

    @PostMapping(value = {"/docProtocollati"})
    public ResponseEntity<Page<DocumentProtocollatoDto>> docProtocollati(@RequestBody DocProtocollatiInputDto input, @ParameterObject Pageable pageable) {
        allegatoDocService.associaDissociaProtocolloUscita(input);
        return ResponseEntity.ok().body(allegatoDocService.listWithPredInUscita(input.getIdProcedura(), input.getIdAllegato(), pageable));
    }

    @GetMapping(value = {"/docPredisposizione/{idProc}/{idAllegato}"})
    public ResponseEntity<Page<DocumentPredisposizioneDto>> docPredisposizione(@PathVariable Long idProc,
                                                                               @PathVariable Long idAllegato,
                                                                               @ParameterObject Pageable pageable) {
        return ResponseEntity.ok().body(allegatoDocService.listProtocollazione(idProc, idAllegato, pageable));
    }

    @PostMapping(value = {"/docPredisposizione"})
    public ResponseEntity<Page<DocumentPredisposizioneDto>> docPredisposizione(@RequestBody DocProtocollatiInputDto input, @ParameterObject Pageable pageable) {
        allegatoDocService.associaDissociaDocPredisposizione(input);
        return ResponseEntity.ok().body(allegatoDocService.listProtocollazione(input.getIdProcedura(), input.getIdAllegato(), pageable));
    }

}
