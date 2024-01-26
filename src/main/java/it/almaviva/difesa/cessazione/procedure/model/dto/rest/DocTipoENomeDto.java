package it.almaviva.difesa.cessazione.procedure.model.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocTipoENomeDto implements Serializable {

    private String tipo;
    private String nome;

}
