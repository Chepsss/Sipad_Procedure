package it.almaviva.difesa.cessazione.procedure.service.msproc.document.templatehelper;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.TpCetipCessazione;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.RubricaCompletaDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpPrtpoTprocedimentoDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpSgtpoPosizioneStatoDTO;
import it.almaviva.difesa.cessazione.procedure.service.msproc.TpSgtpoPosizioneStatoService;
import it.almaviva.difesa.cessazione.procedure.service.rest.SipadClient;
import it.almaviva.difesa.documenti.document.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProcedimentoPlaceHolderHelper {

    private final SipadClient sipadClient;
    private final TpSgtpoPosizioneStatoService tpSgtpoPosizioneStatoService;

    @Transactional
    public void addPlaceHolders(Map<String, Object> model, Procedure procedure) {

        try {
            TpPrtpoTprocedimentoDTO typeCessation = sipadClient.tipoProcedimentoById(procedure.getTypeCessation());
            HashMap<String, Object> proc = new HashMap<>();

            //cr16

            RubricaCompletaDTO enteCC = null;

            if (Objects.nonNull(procedure.getIdEnte_cc1())) {

                try {
                    enteCC = sipadClient.getEnteByIdEnte(procedure.getIdEnte_cc1());
                    if (enteCC != null) {
                        proc.put("idEnte_cc1", enteCC.getDenomNom());
                    }
                } catch (Exception e) {
                    log.error("Errore di sistema - Impossibile compilare il " +
                            "placeholder idEnte_cc1 per ente con ID " + procedure.getIdEnte_cc1(), e);
                }

            }

            if (Objects.nonNull(procedure.getIdEnte_cc2())) {

                try {
                    enteCC = sipadClient.getEnteByIdEnte(procedure.getIdEnte_cc2());
                    if (enteCC != null) {
                        proc.put("idEnte_cc2", enteCC.getDenomNom());
                    }
                } catch (Exception e) {
                    log.error("Errore di sistema - Impossibile compilare il " +
                            "placeholder idEnte_cc2 per ente con ID " + procedure.getIdEnte_cc2(), e);
                }

            }

            if (Objects.nonNull(procedure.getIdEnte_cc3())) {

                try {
                    enteCC = sipadClient.getEnteByIdEnte(procedure.getIdEnte_cc3());
                    if (enteCC != null) {
                        proc.put("idEnte_cc3", enteCC.getDenomNom());
                    }
                } catch (Exception e) {
                    log.error("Errore di sistema - Impossibile compilare il " +
                            "placeholder idEnte_cc3 per ente con ID " + procedure.getIdEnte_cc3(), e);
                }

            }

            //fine cr16

            proc.put("codice", procedure.getCodeProcess());
            TpCetipCessazione reasonCessation = procedure.getReasonCessation();
            proc.put("motivo", Objects.toString(reasonCessation.getCetipMotivoCessazione(), ""));
            boolean isNotNullTypeCessation = Objects.nonNull(typeCessation);
            proc.put("tipo", isNotNullTypeCessation ? Objects.toString(typeCessation.getPrtpoDescrProc(), "") : "");
            proc.put("durata", isNotNullTypeCessation ? Objects.toString(typeCessation.getPrtpoNumGgDurlav(), "") : "");

            if (Objects.nonNull(procedure.getCategLeaveReq())) {
                TpSgtpoPosizioneStatoDTO posizioneStatoDTO = tpSgtpoPosizioneStatoService.getCategLeaveReqById(procedure.getCategLeaveReq());
                proc.put("catCongedoRichiesta", Objects.nonNull(posizioneStatoDTO) ?
                        Objects.toString(posizioneStatoDTO.getSgtpoDescrPosizione(), "").toLowerCase() : "");
            } else {
                proc.put("catCongedoRichiesta", "");
            }
            proc.put("dataDecorrenza", DateUtils.localDateTime2String(procedure.getDataDecorrenza()));
            proc.put("dataPresDomanda", DateUtils.localDateTime2String(procedure.getDataPresDocRich()));

            gmlPlaceholder(procedure, proc);

            boolean vistoRagioneria = false;
            if (Objects.nonNull(procedure.getFlVistoRagioneria())) {
                vistoRagioneria = procedure.getFlVistoRagioneria();
            }
            proc.put("richiestoParereRag", vistoRagioneria ? "true" : "false");

            proc.put("dataLimiteEta", DateUtils.localDateTime2String(procedure.getDataRaggEta()));

            boolean cetipIsBD169 = Constant.CETIP_PERMANENTLY_UNSUITABLE.equals(reasonCessation.getCetipAcrTiv());
            proc.put("dataInizio266", cetipIsBD169 ? Objects.toString(proc.get("proc.dataGML"), "") : "");

            LocalDateTime dataDecorrenza = procedure.getDataDecorrenza();
            LocalDateTime dataGML = procedure.getDataGml();
            LocalDateTime dataFine266 = dataDecorrenza.minus(1, ChronoUnit.DAYS);
            proc.put("dataFine266", cetipIsBD169 ? DateUtils.localDate2String(dataFine266.toLocalDate()) : "");

            if (Objects.nonNull(dataGML)) {
                Long totGiorni266 = ChronoUnit.DAYS.between(dataGML, dataDecorrenza);
                proc.put("totGiorni266", cetipIsBD169 ? Objects.toString(totGiorni266, "") : "");
            } else {
                proc.put("totGiorni266", "");
            }

            model.put("proc", proc);
        } catch(Exception e) {
            log.error("Errore di sistema", e);
        }
    }

    private static void gmlPlaceholder(Procedure procedure, HashMap<String, Object> proc) {
        boolean isNullDataGmlAppello = Objects.isNull(procedure.getDataGmlAppello());
        LocalDateTime dataGml = isNullDataGmlAppello ? procedure.getDataGml() : procedure.getDataGmlAppello();
        proc.put("dataGML", DateUtils.localDateTime2String(dataGml));

        String numVerbaleGml = isNullDataGmlAppello ? procedure.getNumVerbGml() : procedure.getNVerbGmlAppello();
        proc.put("numVerbaleGml", Objects.toString(numVerbaleGml, ""));

        String organoSanitario = isNullDataGmlAppello ? procedure.getOrganoSanita() : null;
        proc.put("organoSanitario", Objects.toString(organoSanitario, ""));

        String modVerbGML = isNullDataGmlAppello ? procedure.getModVerbaleGml() : null;
        proc.put("modVerbGML", Objects.toString(modVerbGML, ""));
    }

}
