package br.com.escola.feiraciencias.events.application.usecases;

import br.com.escola.feiraciencias.events.application.services.EventoService;
import br.com.escola.feiraciencias.events.domain.model.Evento;
import br.com.escola.feiraciencias.events.domain.repositories.EventoRepository;
import br.com.escola.feiraciencias.storage.application.contracts.StorageService;
import br.com.escola.feiraciencias.storage.application.dto.StorageFileInput;
import br.com.escola.feiraciencias.storage.application.dto.StorageUploadResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AtualizarCapaEventoUseCase {

    @Inject
    EventoRepository eventoRepository;

    @Inject
    StorageService storageService;

    @Inject
    EventoService eventoService;

    @Transactional
    public Evento execute(Integer id, StorageFileInput capaInput, Integer professorId) {
        Evento evento = eventoService.buscarPorIdOuFalhar(id);

        if (evento.getImagemCapaChave() != null) {
            storageService.delete(evento.getImagemCapaChave());
        }

        StorageUploadResult resultado = storageService.upload(capaInput, professorId);
        evento.setImagemCapaChave(resultado.chave());

        return eventoRepository.salvar(evento);
    }
}
