package repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import user_management.modelo.Usuario;

import java.util.Optional;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCedula(Integer cedula);
}
