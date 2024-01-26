package it.almaviva.difesa.cessazione.procedure.service.msproc.document.templatehelper;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.documenti.document.domain.msdoc.TbDocumento;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DecretoPlaceHolderHelper {

    public static void addPlaceHolders(Map<String, Object> model, TbDocumento documento, Long numAttoDecreto) {
        HashMap<String, Object> decr = new HashMap<>();
        if (Objects.nonNull(documento)) {
            log.debug(">>>>>>> Document NOT NULL NumAtto: {} AnnoAtto: {}", documento.getNumAtto(), documento.getAnnoAtto());
            decr.put("numAttoSipad", String.format(Constant.NUM_ATTO_SIPAD, documento.getNumAtto(), documento.getAnnoAtto()));
            decr.put("annoAttoSipad", String.valueOf(documento.getAnnoAtto()));
        } else if (Objects.nonNull(numAttoDecreto)) {
            log.debug(">>>>>>> NumAttoDecreto NOT NULL: {}", numAttoDecreto);
            decr.put("numAttoSipad", String.format(Constant.NUM_ATTO_SIPAD, numAttoDecreto, Calendar.getInstance().get(Calendar.YEAR)));
            decr.put("annoAttoSipad", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        } else {
            decr.put("numAttoSipad", "");
            decr.put("annoAttoSipad", "");
        }

        model.put("decr", decr);
    }

}
