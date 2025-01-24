package user_management_web.controlador;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Data;
import org.primefaces.PrimeFaces;
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
    private List<Usuario> usuarios;
    private Usuario usuarioSeleccionado;

    private static final Logger logger = LoggerFactory.getLogger(IndexControlador.class);

    @PostConstruct
    public void iniciador(){
        cargarUsuarios();
    }

    public void cargarUsuarios(){
        this.usuarios = this.usuarioServicio.mostrarUsuarios();
        this.usuarios.forEach(usuario -> logger.info(usuario.toString()));
    }


}
