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
    private Integer cedula;
    private Usuario usuarioEncontrado;
    private static final Logger logger = LoggerFactory.getLogger(IndexControlador.class);

    @PostConstruct
    public void iniciador(){
        cargarUsuarios();
    }

    public void cargarUsuarios(){
        this.usuarios = this.usuarioServicio.mostrarUsuarios();
        this.usuarios.forEach(usuario -> logger.info(usuario.toString()));
    }

    public void crearUsuario(){
        this.usuarioSeleccionado = new Usuario();
    }

    public void guardarUsuario(){
        if (this.usuarioSeleccionado.getId() == null){
            logger.info("El usuario a guardar es: "+ this.usuarioSeleccionado);
            this.usuarioServicio.guardarUsuario(this.usuarioSeleccionado);
            this.usuarios.add(this.usuarioSeleccionado);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario agregado"));
        }
        else{
            this.usuarioServicio.guardarUsuario(this.usuarioSeleccionado);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario actualizado"));
        }
        PrimeFaces.current().executeScript("PF('usuarioCrearModificar').hide()");
        PrimeFaces.current().ajax().update("formulario-aparicion-clientes:mensajes","formulario-aparicion-clientes:usuarios-en-linea");
        this.usuarioSeleccionado = null;
        this.cedula = null;
    }

    public void cancelar(){
        this.usuarioSeleccionado = null;
        this.cedula = null;
        PrimeFaces.current().ajax().update("formulario-acciones");
    }

    public void eliminarUsuario(){
        if (this.usuarioSeleccionado !=null){
            logger.info("Usuario a eliminar: "+this.usuarioSeleccionado);
            this.usuarioServicio.eliminarUsuario(this.usuarioSeleccionado);
            this.usuarios.remove(this.usuarioSeleccionado);
            this.usuarioSeleccionado = null;
        }else if (this.usuarioEncontrado!= null){
            logger.info("Usuario a eliminar: "+this.usuarioEncontrado);
            this.usuarioServicio.eliminarUsuario(this.usuarioEncontrado);
            this.usuarios.remove(this.usuarioEncontrado);
            this.usuarioEncontrado = null;
            this.cedula = null;

        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario eliminado con exito"));
        PrimeFaces.current().executeScript("PF('ventanaConfirmacion').hide()");
        PrimeFaces.current().executeScript("PF('ventanaBuscar').hide()");
        PrimeFaces.current().ajax().update("formulario-aparicion-clientes:mensajes", "formulario-aparicion-clientes:usuarios-en-linea", "formulario-acciones:formulario-buscar-usuarios");
    }

    public void buscarUsuario(){
        this.usuarioEncontrado = this.usuarioServicio.buscarUsuario(this.cedula);
        if(usuarioEncontrado!= null){
            PrimeFaces.current().ajax().update("formulario-buscar-usuarios");
            logger.info("nombre: "+usuarioEncontrado.getNombre());
            logger.info("Usuario encontrado: "+usuarioEncontrado);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("El usuario ha sido encontrado:"));
            this.cedula = null;
            PrimeFaces.current().ajax().update("formulario-aparicion-clientes:mensajes");
        }else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("El usuario que busca no existe"));
            this.cedula =null;
            PrimeFaces.current().ajax().update("formulario-aparicion-clientes:mensajes");
        }
    }

    public void editarUsuario(){
        this.usuarioServicio.guardarUsuario(this.usuarioSeleccionado);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario actualizado"));
        PrimeFaces.current().executeScript("PF('ventanaBuscar').hide()");
    }
}
