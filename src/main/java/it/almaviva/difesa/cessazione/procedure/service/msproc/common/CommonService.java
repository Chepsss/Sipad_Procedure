package it.almaviva.difesa.cessazione.procedure.service.msproc.common;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpDocatCdocumentoDTO;

import java.time.LocalDate;

public interface CommonService {

    default TpDocatCdocumentoDTO getLetteraRichiestaDTO() {
        TpDocatCdocumentoDTO letteraDTO = new TpDocatCdocumentoDTO();
        letteraDTO.setId(-1L);
        letteraDTO.setDocatAcrCat(Constant.LETTERA);
        letteraDTO.setDocatDescrCat(Constant.LETTERA_RICHIESTA);
        letteraDTO.setDocatDataIniz(LocalDate.parse(Constant.DOCAT_DATA_INIZIO));
        letteraDTO.setDocatDataFine(LocalDate.parse(Constant.DOCAT_DATA_FINE));
        return letteraDTO;
    }

}
