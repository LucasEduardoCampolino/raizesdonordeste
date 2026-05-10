package com.raizesdonordeste.service;

import com.raizesdonordeste.dto.PromocaoDTO;
import com.raizesdonordeste.model.entity.Promocao;
import com.raizesdonordeste.model.entity.Unidade;
import com.raizesdonordeste.repository.PromocaoRepository;
import com.raizesdonordeste.repository.UnidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromocaoService {

    private final PromocaoRepository repository;
    private final UnidadeRepository unidadeRepository;

    public PromocaoDTO criar(PromocaoDTO dto) {
        Promocao promocao = toEntity(dto);

        Promocao salva = repository.save(promocao);

        return toDTO(salva);
    }

    public List<PromocaoDTO> listar() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private Promocao toEntity(PromocaoDTO dto) {
        Promocao promocao = new Promocao();

        promocao.setNomeEvento(dto.getNome());
        promocao.setValorPromocional(dto.getDesconto());
        promocao.setDataInicio(dto.getDataInicio());
        promocao.setDataFim(dto.getDataFim());
        promocao.setAtivo(dto.getAtivo());

        if (dto.getUnidadeId() != null) {
            promocao.setUnidade(
                    unidadeRepository.findById(dto.getUnidadeId())
                            .orElseThrow(() -> new RuntimeException("Unidade não encontrada"))
            );
        }

        return promocao;
    }

    private PromocaoDTO toDTO(Promocao p) {
        PromocaoDTO dto = new PromocaoDTO();

        dto.setId(p.getId());
        dto.setNome(p.getNomeEvento());
        dto.setDesconto(p.getValorPromocional());
        dto.setDataInicio(p.getDataInicio());
        dto.setDataFim(p.getDataFim());
        dto.setAtivo(p.getAtivo());
        dto.setUnidadeId(p.getUnidade() != null ? p.getUnidade().getId() : null);

        return dto;
    }

    public double aplicarDesconto(Unidade unidade, double totalBruto) {
        List<Promocao> promocoes = buscarPromocoesAtivas(unidade);

        if (promocoes.isEmpty()) {
            return totalBruto;
        }

        double descontoTotal = promocoes.stream()
                .mapToDouble(Promocao::getValorPromocional)
                .sum();

        return Math.max(totalBruto - descontoTotal, 0);
    }

    public List<Promocao> buscarPromocoesAtivas(Unidade unidade) {
        LocalDate hoje = LocalDate.now();

        return repository.findByUnidadeIdAndAtivoTrueAndDataInicioBeforeAndDataFimAfter(
                unidade.getId(),
                hoje,
                hoje
        );
    }
}