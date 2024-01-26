package it.almaviva.difesa.cessazione.procedure.service.msproc.document.templatehelper;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.TbCeProcTransito;
import it.almaviva.difesa.documenti.document.utils.DateUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TbCeProcTransitoPlaceHolderHelper {

    public static void addPlaceHolders(Map<String, Object> model, TbCeProcTransito procTransito) {

        HashMap<String, Object> proc = (HashMap<String, Object>) model.get("proc");
        if (Objects.isNull(proc)) {
            proc = new HashMap<>();
        }
        HashMap<String, Object> transito = new HashMap<>();
        if (Objects.nonNull(procTransito)) {
            transito.put("dataDomanda", DateUtils.localDate2String(procTransito.getDataIstanza()));
            transito.put("dataAutorizz", DateUtils.localDate2String(procTransito.getDataNoautPersociv()));
            transito.put("protRinuncia", Objects.toString(procTransito.getProtRinunciaCmd(), ""));
            transito.put("dataRinuncia", DateUtils.localDate2String(procTransito.getDataRinunciaCmd()));
            transito.put("protRinunciaPersomil", Objects.toString(procTransito.getProtRinunciaPersomil(), ""));
            transito.put("dataRinunciaPersomil", DateUtils.localDate2String(procTransito.getDataRinunciaPersomil()));
        }
        proc.put("transito", transito);

        model.put("proc", proc);
    }

}
