package user_management_web.servicio;

import user_management_web.modelo.Usuario;

import java.util.List;

public interface IUsuarioServicio {
    public List<Usuario> mostrarUsuarios();

    public Usuario buscarUsuario(Integer cedula);

    public void eliminarUsuario(Usuario usuario);

    public void guardarUsuario(Usuario usuario);
}
