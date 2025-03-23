package com.host_go.host_go.Servicios;

import com.host_go.host_go.modelos.Cuenta;
import com.host_go.host_go.modelos.Status;
import com.host_go.host_go.Repositorios.CuentaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CuentaUserDetailsService implements UserDetailsService {

    @Autowired
    private CuentaRepositorio cuentaRepositorio;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Cuenta cuenta = cuentaRepositorio.findByUsuario(correo)
            .orElseThrow(() -> new UsernameNotFoundException("Cuenta no encontrada"));

            if (cuenta.getStatus() != Status.ACTIVE) {
                throw new UsernameNotFoundException("Cuenta no activa");
            }

        return User.builder()
            .username(cuenta.getUsuario())
            .password(cuenta.getContrasena())
            .roles(cuenta.getTipo().toUpperCase())
            .build();
    }
}