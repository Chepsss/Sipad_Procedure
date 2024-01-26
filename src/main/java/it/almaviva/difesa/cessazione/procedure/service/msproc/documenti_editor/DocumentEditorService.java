package it.almaviva.difesa.cessazione.procedure.service.msproc.documenti_editor;

import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.documenti_editor.InputPreviewDocumentDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.documenti_editor.OutputPreviewDocumentDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.templates.DocumentDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.templates.TemplateResponseDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.InputDocument;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.ModelloDTO;

import java.util.ArrayList;
import java.util.HashMap;

public interface DocumentEditorService {

    TemplateResponseDTO getTemplate(InputDocument document);

    ArrayList<ModelloDTO> getTemplates(String codice, String nome);

    OutputPreviewDocumentDTO preview(InputPreviewDocumentDTO input);

    DocumentDTO getDocumentoPdf(InputPreviewDocumentDTO input);

    HashMap<String, Object> retrievePlaceHolders(Long idProcedura, Long numAttoDecreto);

    HashMap<String, Object> retrievePlaceHolderForTemplate(InputPreviewDocumentDTO document);

    String getStringOfFileCss();

}
