package it.almaviva.difesa.cessazione.procedure.service.msproc.document.templatehelper;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.TbCeProcParereRag;
import it.almaviva.difesa.documenti.document.utils.DateUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TbCeProcParereRagPlaceHolderHelper {

    public static void addPlaceHolders(Map<String, Object> model, TbCeProcParereRag parereRagioneria) {
        HashMap<String, Object> proc = (HashMap<String, Object>) model.get("proc");
        if (Objects.isNull(proc)) {
            proc = new HashMap<>();
        }
        HashMap<String, Object> parereRag = new HashMap<>();
        if (Objects.nonNull(parereRagioneria)) {
            parereRag.put("numReg", Objects.toString(parereRagioneria.getNumRegistrazione(), ""));
            if (Objects.nonNull(parereRagioneria.getEsito())) {
                parereRag.put("esito", parereRagioneria.getEsito() ? "Positivo" : "Negativo");
            } else {
                parereRag.put("esito", "");
            }
            parereRag.put("dataEsito", DateUtils.localDate2String(parereRagioneria.getDataEsito()));
        }
        proc.put("parereRag", parereRag);

        model.put("proc", proc);
    }

}
