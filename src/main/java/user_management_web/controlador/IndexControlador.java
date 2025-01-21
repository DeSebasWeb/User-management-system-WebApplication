package user_management_web.controlador;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import user_management_web.modelo.Usuario;
import user_management_web.servicio.IUsuarioServicio;

import java.util.List;

@Component

@Data
@ViewScoped
public class IndexControlador {
    @Autowired
    IUsuarioServicio usuarioServicio;
    private Usuario usuario;
    private List<Usuario> usuarios;

    private static final Logger logger = LoggerFactory.getLogger(IndexControlador.class);

    @PostConstruct
    public void constructor(){
        cargarDatos();
    }

    public void cargarDatos(){
        this.usuarios = this.usuarioServicio.mostrarUsuarios();
        this.usuarios.forEach(usuario -> logger.info(usuario.toString()));
    }

    public void crearUsuario(){
        this.usuario = new Usuario();
    }

    public void guardarUsuario(){

    }

}
