package br.com.escola.feiraciencias.events.application.usecases;

import java.time.LocalDateTime;

import br.com.escola.feiraciencias.events.domain.model.Evento;
import br.com.escola.feiraciencias.events.domain.repositories.EventoRepository;
import br.com.escola.feiraciencias.shared.domain.enums.SituacaoEvento;
import br.com.escola.feiraciencias.storage.application.contracts.StorageService;
import br.com.escola.feiraciencias.storage.application.dto.StorageFileInput;
import br.com.escola.feiraciencias.storage.application.dto.StorageUploadResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CriarEventoUseCase {

    @Inject
    EventoRepository eventoRepository;

    @Inject
    StorageService storageService;

    @Transactional
    public Evento execute(Evento evento, Integer professorId, StorageFileInput capaInput) {   
        evento.setDataCriacao(LocalDateTime.now());
        evento.setCriadoPorId(professorId);
        evento.setSituacao(SituacaoEvento.ATIVO);

        if (capaInput != null) {
            StorageUploadResult resultado = storageService.upload(capaInput, professorId);
            evento.setImagemCapaChave(resultado.chave());
        }

        return eventoRepository.salvar(evento);
    }
}
