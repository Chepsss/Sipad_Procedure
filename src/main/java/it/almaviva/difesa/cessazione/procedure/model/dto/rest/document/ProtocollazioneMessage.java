package it.almaviva.difesa.cessazione.procedure.model.dto.rest.document;

import it.almaviva.difesa.cessazione.procedure.model.dto.rest.DocTipoENomeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtocollazioneMessage implements Serializable {

    private List<String> messages;
    private List<DocTipoENomeDto> files;

}
