package it.almaviva.difesa.cessazione.procedure.model.dto.rest.document;

import it.almaviva.difesa.documenti.document.model.dto.response.organigramma.OrganigrammaLevel;
import it.almaviva.difesa.documenti.document.model.dto.response.organigramma.TbStoraOrganigrammaAooDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrganigrammaLevelListDTO implements Serializable {

    private List<String> list;
    private List<OrganigrammaLevel> livelli;
    private List<TbStoraOrganigrammaAooDto> assegnatari;

}
