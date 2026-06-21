package br.com.escola.feiraciencias.storage.application.contracts;

import br.com.escola.feiraciencias.storage.application.dto.StorageFileInput;
import br.com.escola.feiraciencias.storage.application.dto.StorageUploadResult;


public interface StorageService {
    StorageUploadResult upload(StorageFileInput input, Integer usuarioId);
    void delete(String chave);
    String gerarUrl(String chave);
}
