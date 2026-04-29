package com.raizesdonordeste.config;

import com.raizesdonordeste.model.entity.Usuario;
import com.raizesdonordeste.model.enums.PerfilEnum;
import com.raizesdonordeste.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    public DataInitializer(UsuarioRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {

        if (repository.findByEmail("admin@raizes.com").isEmpty()) {

            Usuario admin = Usuario.builder()
                    .nome("Administrador")
                    .email("admin@raizes.com")
                    .senha(encoder.encode("123456"))
                    .perfil(PerfilEnum.ADMIN)
                    .saldoPontos(0)
                    .build();

            repository.save(admin);
        }
    }
}