package br.com.escola.feiraciencias.projects.application.usecases;

import java.time.LocalDateTime;
import java.util.List;

import br.com.escola.feiraciencias.projects.domain.model.RegistroDiario;
import br.com.escola.feiraciencias.projects.domain.repositories.ProjetoUsuarioRepository;
import br.com.escola.feiraciencias.projects.domain.repositories.RegistroDiarioRepository;
import br.com.escola.feiraciencias.shared.domain.enums.TipoUsuario;
import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.storage.application.contracts.StorageService;
import br.com.escola.feiraciencias.storage.application.dto.StorageFileInput;
import br.com.escola.feiraciencias.users.application.services.UsuarioService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CriarRegistroDiarioUseCase {

    @Inject
    RegistroDiarioRepository registroDiarioRepository;

    @Inject
    ProjetoUsuarioRepository projetoUsuarioRepository;

    @Inject
    BuscarProjetoUseCase buscarProjetoUseCase;

    @Inject
    UsuarioService usuarioService;

    @Inject
    StorageService storageService;

    @Transactional
    public RegistroDiario execute(Integer projetoId, RegistroDiario registro, Integer alunoId, List<StorageFileInput> arquivosInput) {
        buscarProjetoUseCase.execute(projetoId);
        var usuario = usuarioService.buscarUsuarioPorId(alunoId);

        if (!usuario.getTipoUsuario().equals(TipoUsuario.ALUNO)) {
            throw new BusinessRuleException("Apenas alunos podem criar registros diários.");
        }

        var vinculo = projetoUsuarioRepository.buscarPorProjetoEUsuario(projetoId, alunoId);
        if (vinculo.isEmpty()) {
            throw new BusinessRuleException("O aluno precisa ser integrante do projeto para criar registros diários.");
        }

        registro.setProjetoId(projetoId);
        registro.setCriadoPorId(alunoId);
        registro.setDataCriacao(LocalDateTime.now());

        if (arquivosInput != null && !arquivosInput.isEmpty()) {
            if (arquivosInput.size() > RegistroDiario.LIMITE_ARQUIVOS) {
                throw new BusinessRuleException("Limite de " + RegistroDiario.LIMITE_ARQUIVOS + " arquivos por registro excedido.");
            }
            for (var input : arquivosInput) {
                var resultado = storageService.upload(input, alunoId);
                registro.adicionarArquivoChave(resultado.chave());
            }
        }

        return registroDiarioRepository.salvar(registro);
    }
}
