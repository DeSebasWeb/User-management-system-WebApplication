package user_management_web.controlador;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import user_management_web.modelo.Usuario;
import user_management_web.servicio.IUsuarioServicio;

import java.util.ArrayList;
import java.util.List;

@Component

@Data
@ViewScoped
@Getter
@Setter
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
        if (this.usuarios== null){
            this.usuarios = new ArrayList<>();
        }
        this.usuarios = this.usuarioServicio.mostrarUsuarios();
        this.usuarios.forEach(usuario -> logger.info(usuario.toString()));
    }

    public void crearUsuario(){
        this.usuarioSeleccionado = null;
        this.usuarioEncontrado = null;
        this.usuarioSeleccionado = new Usuario();
        this.usuarioEncontrado = new Usuario();
    }

    public void salirCancelar(){
        PrimeFaces.current().executeScript("PF('ventanaBuscar').hide()");
        PrimeFaces.current().executeScript("PF('usuarioCrearModificar').hide()");
        logger.info("Se ha eliminado todo registro de los objetos");

    }

    public void buscarUsuario(){
        if(this.usuarioServicio.buscarUsuario(this.cedula)!= null){
            this.usuarioEncontrado = this.usuarioServicio.buscarUsuario(this.cedula);
            PrimeFaces.current().ajax().update("formulario-buscar-usuarios");
            logger.info("nombre: "+usuarioEncontrado.getNombre());
            logger.info("Usuario encontrado: "+usuarioEncontrado);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("El usuario ha sido encontrado:"));
            PrimeFaces.current().ajax().update("formulario-aparicion-clientes:mensajes");
        }else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("El usuario que busca no existe"));
            PrimeFaces.current().ajax().update("formulario-aparicion-clientes:mensajes");
            this.usuarioEncontrado = null;
        }
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
        this.usuarioSeleccionado = null;
        this.cedula = null;
        PrimeFaces.current().executeScript("PF('usuarioCrearModificar').hide()");
        PrimeFaces.current().ajax().update("formulario-aparicion-clientes:mensajes","formulario-aparicion-clientes:usuarios-en-linea");
    }

    public void editarUsuario(){
        if (this.usuarioEncontrado ==null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No se puede editar un usuario que no existe"));
            PrimeFaces.current().executeScript("PF('ventanaBuscar').hide()");
        }else {
            this.usuarioServicio.guardarUsuario(this.usuarioEncontrado);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario actualizado"));

            cargarUsuarios();
            PrimeFaces.current().ajax().update("formulario-aparicion-clientes:mensajes","formulario-aparicion-clientes:usuarios-en-linea");

            PrimeFaces.current().executeScript("PF('ventanaConfirmacionEdicion').hide()");
            PrimeFaces.current().executeScript("PF('ventanaBuscar').hide()");
        }
        this.usuarioEncontrado = null;
        this.cedula = null;
    }

    public void eliminarUsuario(){
        if(this.usuarioSeleccionado == null || this.usuarioEncontrado == null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("El usuario que intenta eliminar no existe"));
        }
        if (this.usuarioSeleccionado !=null){
            logger.info("Usuario a eliminar: "+this.usuarioSeleccionado);
            this.usuarioServicio.eliminarUsuario(this.usuarioSeleccionado);
            this.usuarios.remove(this.usuarioSeleccionado);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario eliminado con exito"));
        }
        if (this.usuarioEncontrado!= null){
            logger.info("Usuario a eliminar: "+this.usuarioEncontrado);
            this.usuarioServicio.eliminarUsuario(this.usuarioEncontrado);
            this.usuarios.remove(this.usuarioEncontrado);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario eliminado con exito"));
        }

        this.usuarioSeleccionado = null;
        this.usuarioEncontrado = null;
        this.cedula = null;

        PrimeFaces.current().executeScript("PF('ventanaConfirmacion').hide()");
        PrimeFaces.current().executeScript("PF('ventanaBuscar').hide()");
        PrimeFaces.current().ajax().update("formulario-aparicion-clientes:mensajes", "formulario-aparicion-clientes:usuarios-en-linea", "formulario-acciones:formulario-buscar-usuarios");
    }
}
