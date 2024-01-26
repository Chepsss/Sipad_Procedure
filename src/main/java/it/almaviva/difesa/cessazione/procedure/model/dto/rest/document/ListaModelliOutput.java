package it.almaviva.difesa.cessazione.procedure.model.dto.rest.document;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class ListaModelliOutput implements Serializable {

    private static final long serialVersionUID = 2162463331655447874L;

    private List<ModelloDTO> modelli;

}
