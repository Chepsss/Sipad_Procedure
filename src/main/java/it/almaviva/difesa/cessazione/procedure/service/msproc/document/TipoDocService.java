package it.almaviva.difesa.cessazione.procedure.service.msproc.document;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.constant.TipoDocumento;
import it.almaviva.difesa.documenti.document.model.dto.response.documenti.ListaCategoriaOutput;
import it.almaviva.difesa.documenti.document.model.dto.response.tipidocumenti.ListaTipiDocumentiOutput;
import it.almaviva.difesa.documenti.document.service.TipodocumentoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class TipoDocService {

    private final TipodocumentoService tipodocumentoService;

    public ListaCategoriaOutput listFilteredCategories() {
        ListaCategoriaOutput list = tipodocumentoService.listCategorie();
        ListaCategoriaOutput filteredList = new ListaCategoriaOutput();
        filteredList.setCategorie(list.getCategorie().stream()
                .filter(cat -> cat.getCodice().equalsIgnoreCase("3") || cat.getCodice().equalsIgnoreCase("4"))
                .collect(Collectors.toList()));
        return filteredList;
    }

    public ListaTipiDocumentiOutput listFilteredDocTypes(long docatId) {
        ListaTipiDocumentiOutput list = tipodocumentoService.listTipiDocumenti(Constant.ID_APPLICATIVO, docatId);
        ListaTipiDocumentiOutput filteredList = new ListaTipiDocumentiOutput();
        if (docatId == 4) {
            filteredList.setTipiDocumenti(list.getTipiDocumenti().stream()
                    .filter(tipo -> tipo.getCodice().equalsIgnoreCase(TipoDocumento.DECRETO))
                    .collect(Collectors.toList()));
        } else {
            filteredList.setTipiDocumenti(list.getTipiDocumenti().stream()
                    .filter(tipo -> !tipo.getCodice().equalsIgnoreCase(TipoDocumento.LETTERA_TRASMISSIONE)
                            && !tipo.getCodice().equalsIgnoreCase(TipoDocumento.LETTERA_COLLOCAMENTO))
                    .collect(Collectors.toList()));
        }
        return filteredList;
    }

    public ListaTipiDocumentiOutput listDocTypes(long docatId) {
        return tipodocumentoService.listTipiDocumenti(Constant.ID_APPLICATIVO, docatId);
    }

}
