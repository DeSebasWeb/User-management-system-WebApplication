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
        logger.info("Usuario a guardar: "+ this.usuario);
        if (this.usuario.getId()== null){
            this.usuarioServicio.guardarUsuario(this.usuario);
            this.usuarios.add(usuario);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nuevo usuario"));
        }
        PrimeFaces.current().executeScript("PF('ventanaCrearUsuarios').hide()");
        PrimeFaces.current().ajax().update("formulario-usuarios:mensajes", "formulario-usuarios:usuarios-tabla");
        this.usuario=null;
    }

}
