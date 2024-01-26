package it.almaviva.difesa.cessazione.procedure.enums;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum StatusEnum {
    CANNOT_DELETE_DATA("CANNOT_DELETE_DATA"),
    CANNOT_SAVE_DATA("CANNOT_SAVE_DATA"),
    CANNOT_UPDATE_DATA("CANNOT_UPDATE_DATA"),
    ;

    private final String nameMessage;

    @Getter
    @RequiredArgsConstructor
    public enum ProcArchiveEnumField {
        COD_FISC(Constant.COD_FISC, Constant.DO_007_COD_FISC),
        PRPRO_ID(Constant.PRPRO_ID, Constant.DO_007_PRPRO_ID),
        PRPRO_COD_PRO(Constant.PRPRO_COD_PRO, Constant.DO_007_PRPRO_COD_PRO),
        DATA_AVVIO(Constant.DATA_AVVIO, Constant.DO_007_DATA_AVVIO),
        DATA_FINE(Constant.DATA_FINE, Constant.DO_007_DATA_FINE),
        FASE(Constant.FASE, Constant.DO_007_FASE),
        STATO(Constant.STATO, Constant.DO_007_STATO),
        TIPO_PROC(Constant.TIPO_PROC, Constant.DO_007_TIPO_PROC),
        AUTORE(Constant.AUTORE, Constant.DO_007_AUTORE),
        ;

        private final String labelDto;
        private final String entityColumn;

        public static ProcArchiveEnumField fromLabelDto(String labelDto) {
            for (ProcArchiveEnumField e : ProcArchiveEnumField.values()) {
                if (e.getLabelDto().equalsIgnoreCase(labelDto)) {
                    return e;
                }
            }
            return null;
        }
    }

}
