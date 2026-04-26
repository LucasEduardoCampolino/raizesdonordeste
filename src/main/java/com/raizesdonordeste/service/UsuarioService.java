    package com.raizesdonordeste.service;

    import com.raizesdonordeste.dto.UsuarioDTO;
    import com.raizesdonordeste.dto.UsuarioResponseDTO;
    import com.raizesdonordeste.model.entity.Usuario;
    import com.raizesdonordeste.model.enums.PerfilEnum;
    import com.raizesdonordeste.repository.UsuarioRepository;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;

    import java.time.LocalDateTime;
    import java.util.List;

    @Service
    public class UsuarioService {

        private final UsuarioRepository repository;
        private final PasswordEncoder encoder;

        public UsuarioService(UsuarioRepository repository, PasswordEncoder encoder) {
            this.repository = repository;
            this.encoder = encoder;
        }

        public Usuario criar(UsuarioDTO dto) {

            if (repository.findByEmail(dto.getEmail()).isPresent()) {
                throw new RuntimeException("Email já cadastrado");
            }

            Usuario usuario = Usuario.builder()
                    .nome(dto.getNome())
                    .email(dto.getEmail())
                    .senha(encoder.encode(dto.getSenha()))
                    .saldoPontos(0)
                    .dataConsentimentoLGPD(LocalDateTime.now())
                    .perfil(PerfilEnum.CLIENTE)
                    .build();

            return repository.save(usuario);
        }

        public Usuario criarComPerfil(UsuarioDTO dto, PerfilEnum perfil) {

            if (repository.findByEmail(dto.getEmail()).isPresent()) {
                throw new RuntimeException("Email já cadastrado");
            }

            return repository.save(
                    Usuario.builder()
                            .nome(dto.getNome())
                            .email(dto.getEmail())
                            .senha(encoder.encode(dto.getSenha()))
                            .saldoPontos(0)
                            .dataConsentimentoLGPD(LocalDateTime.now())
                            .perfil(perfil)
                            .build()
            );
        }

        public List<Usuario> listarTodos() {
            return repository.findAll();
        }

        public Usuario buscarPorId(Long id) {
            return repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        }

        public Usuario atualizar(Long id, UsuarioDTO dto) {
            Usuario usuario = buscarPorId(id);

            usuario.setNome(dto.getNome());
            usuario.setEmail(dto.getEmail());

            return repository.save(usuario);
        }

        public Usuario atualizarPerfil(Long id, PerfilEnum perfil) {
            Usuario usuario = buscarPorId(id);
            usuario.setPerfil(perfil);
            return repository.save(usuario);
        }

        public void deletar(Long id) {
            repository.deleteById(id);
        }

        public UsuarioResponseDTO toDTO(Usuario u) {
            return new UsuarioResponseDTO(
                    u.getId(),
                    u.getNome(),
                    u.getEmail(),
                    u.getSaldoPontos(),
                    u.getPerfil()
            );
        }
    }