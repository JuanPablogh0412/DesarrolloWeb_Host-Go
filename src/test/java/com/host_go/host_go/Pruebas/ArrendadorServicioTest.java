package com.host_go.host_go.Pruebas;

import static org.junit.jupiter.api.Assertions.*;

import com.host_go.host_go.Dtos.ArrendadorCreateDto;
import com.host_go.host_go.Dtos.ArrendadorDto;
import com.host_go.host_go.Servicios.ArrendadorServicio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ArrendadorServicioTest {

    @Autowired
    private ArrendadorServicio arrendadorServicio;

    @Test
    public void testSave_ValidInput_ReturnsArrendadorDto() {
        // Arrange: Datos válidos para crear una cuenta de arrendador
        ArrendadorCreateDto createDto = new ArrendadorCreateDto();
        createDto.setCedula(111222333);
        createDto.setNombre("Test");
        createDto.setApellido("User");
        createDto.setCorreo("test@javeriana.edu.co");
        createDto.setTelefono(300111222);
        createDto.setContrasena("password123");

        // Act: Llamada al servicio para guardar
        ArrendadorDto result = arrendadorServicio.save(createDto);

        // Assert: Verificar que se haya creado correctamente
        assertNotNull(result);
        assertTrue(result.getArrendadorId() > 0);
        assertEquals("Test", result.getNombre());
        assertEquals("User", result.getApellido());
        // Se pueden agregar más aserciones según sea necesario
    }

    @Test
    public void testSave_DuplicateEmail_ThrowsException() {
        // Arrange: Crear y guardar un primer arrendador con un correo
        ArrendadorCreateDto createDto1 = new ArrendadorCreateDto();
        createDto1.setCedula(111222333);
        createDto1.setNombre("Test");
        createDto1.setApellido("User");
        createDto1.setCorreo("duplicate@javeriana.edu.co");
        createDto1.setTelefono(300111222);
        createDto1.setContrasena("password123");
        arrendadorServicio.save(createDto1);

        // Act & Assert: Al intentar guardar otro arrendador con el mismo correo se debe lanzar excepción
        ArrendadorCreateDto createDto2 = new ArrendadorCreateDto();
        createDto2.setCedula(444555666);
        createDto2.setNombre("Another");
        createDto2.setApellido("User");
        createDto2.setCorreo("duplicate@javeriana.edu.co");
        createDto2.setTelefono(300333444);
        createDto2.setContrasena("password456");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            arrendadorServicio.save(createDto2);
        });
        assertEquals("El correo ya está registrado", exception.getMessage());
    }

    @Test
    public void testSave_InvalidEmailFormat_ThrowsException() {
        // Arrange: El correo no cumple con el formato corporativo
        ArrendadorCreateDto createDto = new ArrendadorCreateDto();
        createDto.setCedula(111222333);
        createDto.setNombre("Test");
        createDto.setApellido("User");
        createDto.setCorreo("test@gmail.com");  // dominio incorrecto
        createDto.setTelefono(300111222);
        createDto.setContrasena("password123");

        // Act & Assert:
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            arrendadorServicio.save(createDto);
        });
        assertEquals("Correo no cumple con el formato corporativo", exception.getMessage());
    }

    @Test
    public void testSave_PasswordTooShort_ThrowsException() {
        // Arrange: La contraseña es demasiado corta
        ArrendadorCreateDto createDto = new ArrendadorCreateDto();
        createDto.setCedula(111222333);
        createDto.setNombre("Test");
        createDto.setApellido("User");
        createDto.setCorreo("test@javeriana.edu.co");
        createDto.setTelefono(300111222);
        createDto.setContrasena("short");  // menos de 8 caracteres

        // Act & Assert:
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            arrendadorServicio.save(createDto);
        });
        assertEquals("La contraseña debe tener al menos 8 caracteres", exception.getMessage());
    }
}
