package br.com.escola.feiraciencias.users.domain.repositories;

import br.com.escola.feiraciencias.users.domain.enums.StatusConvite;
import br.com.escola.feiraciencias.users.domain.model.ConviteRegistro;
import br.com.escola.feiraciencias.shared.domain.pagination.Page;
import java.time.LocalDateTime;
import java.util.Optional;

public interface ConviteRegistroRepository {
    ConviteRegistro salvar(ConviteRegistro convite);
    Optional<ConviteRegistro> buscarPorToken(String token);
    Optional<ConviteRegistro> buscarPorId(Integer id);
    /**
     * Lista convites com paginacao.
     * criadorId null = sem filtro de criador (uso do ADMIN).
     * status null = todos os status.
     */
    Page<ConviteRegistro> listarPorCriador(Integer criadorId, StatusConvite status, int page, int size);
    boolean existeConviteAtivoPorMatricula(String matricula);
    int expirarConvitesAtrasados(LocalDateTime agora);
}

