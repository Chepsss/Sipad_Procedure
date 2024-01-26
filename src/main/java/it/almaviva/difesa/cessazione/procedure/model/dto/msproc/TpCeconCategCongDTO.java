package it.almaviva.difesa.cessazione.procedure.model.dto.msproc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TpCeconCategCongDTO implements Serializable {

    private static final long serialVersionUID = -1085893549202059687L;

    private String ceconSgtpoGdl;

}
