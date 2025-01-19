package servicio;

import user_management.modelo.Usuario;

import java.util.List;

public interface IUsuarioServicio {
    public List<Usuario> mostrarUsuarios();

    public Usuario buscarUsuario(Integer cedula);

    public void eliminarUsuario(Usuario usuario);

    public void guardarUsuario(Usuario usuario);
}
