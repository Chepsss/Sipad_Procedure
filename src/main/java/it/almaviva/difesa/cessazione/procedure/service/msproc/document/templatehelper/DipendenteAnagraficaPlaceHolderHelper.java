package it.almaviva.difesa.cessazione.procedure.service.msproc.document.templatehelper;

import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.VwDo001TemplateAnagrDTO;
import it.almaviva.difesa.documenti.document.utils.DateUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DipendenteAnagraficaPlaceHolderHelper {

    public static void addPlaceHolders(Map<String, Object> model, VwDo001TemplateAnagrDTO templateAnagrDTO) {
        HashMap<String, Object> dip = new HashMap<>();
        HashMap<String, Object> anagr = new HashMap<>();

        anagr.put("nome", Objects.toString(templateAnagrDTO.getDescrNome(), ""));
        anagr.put("cognome", Objects.toString(templateAnagrDTO.getDescrCogn(), ""));
        anagr.put("codFisc", Objects.toString(templateAnagrDTO.getCodFsc(), ""));
        anagr.put("dataNascita", DateUtils.localDate2String(templateAnagrDTO.getDataNasc()));
        anagr.put("comuneNascita", Objects.toString(templateAnagrDTO.getDescrComuneNasc(), ""));
        anagr.put("provinciaNascita", Objects.toString(templateAnagrDTO.getSiglaProv(), ""));

        dip.put("anagr", anagr);
        model.put("dip", dip);
    }

}
