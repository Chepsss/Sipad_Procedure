package it.almaviva.difesa.cessazione.procedure.domain.common;

import org.springframework.data.domain.Sort;

public class SortConstant {

    public static final String ID = "id";
    public static final String ID_DICH = "idDich";
    public static final String ORD_STATE = "ordState";

    private SortConstant() {
    }

    public static final Sort UNSORTED = Sort.unsorted();
    public static final Sort SORT_BY_ID = Sort.by(ID);
    public static final Sort SORT_BY_ID_DICH = Sort.by(ID_DICH);
    public static final Sort SORT_BY_ORD_STATE = Sort.by(ORD_STATE);

}
