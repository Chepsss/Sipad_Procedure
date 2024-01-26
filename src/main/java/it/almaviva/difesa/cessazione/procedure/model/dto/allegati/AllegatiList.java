package it.almaviva.difesa.cessazione.procedure.model.dto.allegati;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AllegatiList {

    List<AllegatoDto> allegati = new ArrayList<>();

}
