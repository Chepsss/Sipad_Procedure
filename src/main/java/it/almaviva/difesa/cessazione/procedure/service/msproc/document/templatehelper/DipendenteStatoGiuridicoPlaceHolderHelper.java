package it.almaviva.difesa.cessazione.procedure.service.msproc.document.templatehelper;

import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.VwDo001TemplateAnagrDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.VwSg001StgiuridicoMilDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.VwSg122StgiuridicoDsDTO;
import it.almaviva.difesa.documenti.document.utils.DateUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DipendenteStatoGiuridicoPlaceHolderHelper {

    public static void addPlaceHolders(Map<String, Object> model, VwSg122StgiuridicoDsDTO sg122StgiuridicoDsDTO,
                                       VwDo001TemplateAnagrDTO templateAnagrDTO, VwSg001StgiuridicoMilDTO sg001StgiuridicoMilDTO) {
        HashMap<String, Object> dip = (HashMap<String, Object>) model.get("dip");
        HashMap<String, Object> statoGiur = new HashMap<>();

        statoGiur.put("grado", Objects.toString(templateAnagrDTO.getDescrGrado(), ""));
        statoGiur.put("posStato", Objects.toString(templateAnagrDTO.getDescrPosServ(), ""));
        statoGiur.put("ruolo", Objects.toString(templateAnagrDTO.getDescrRuolo(), ""));
        statoGiur.put("forzaArmata", Objects.toString(sg122StgiuridicoDsDTO.getDescrForzaArmata(), ""));
        statoGiur.put("armaCorpo", Objects.toString(sg122StgiuridicoDsDTO.getDescrTipoArmaCorpo(), ""));
        statoGiur.put("catPersonale", Objects.toString(templateAnagrDTO.getDescrCatPers(), ""));
        statoGiur.put("dataArruol", DateUtils.localDate2String(templateAnagrDTO.getDataArr()));
        statoGiur.put("categoria", Objects.toString(templateAnagrDTO.getDescrCatPers(), ""));
        statoGiur.put("matricola", Objects.toString(templateAnagrDTO.getCodMatricola(), ""));
        String numeroDivisione = null;
        long codCatPers = Objects.nonNull(sg122StgiuridicoDsDTO.getIdCatPersonale()) ? sg122StgiuridicoDsDTO.getIdCatPersonale() : 0L;
        if (3L == codCatPers) {
            numeroDivisione = "6";
        } else if (2L == codCatPers) {
            numeroDivisione = "5";
        } else if (1L == codCatPers) {
            numeroDivisione = "4";
        } else if (0L == codCatPers) {
            numeroDivisione = "";
        }
        statoGiur.put("numeroDivisione", numeroDivisione);
        statoGiur.put("gradoSuperiore", Objects.toString(sg001StgiuridicoMilDTO.getDescrGradoSup(), ""));

        dip.put("statoGiur", statoGiur);
    }

}
