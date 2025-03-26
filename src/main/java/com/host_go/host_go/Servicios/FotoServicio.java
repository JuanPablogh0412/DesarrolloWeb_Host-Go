package com.host_go.host_go.Servicios;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.host_go.host_go.Dtos.FotoDto;
import com.host_go.host_go.Dtos.ImgurResponse;
import com.host_go.host_go.Repositorios.FotoRepositorio;
import com.host_go.host_go.Repositorios.PropiedadRepositorio;
import com.host_go.host_go.modelos.Foto;
import com.host_go.host_go.modelos.Propiedad;
import com.host_go.host_go.modelos.Status;

import io.jsonwebtoken.io.IOException;

@Service
public class FotoServicio {

    @Autowired
    FotoRepositorio FotoRepositorio;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    PropiedadRepositorio propiedadRepositorio;
    
    @Value("${imgur.client-id}") 
    private String imgurClientId;

    public FotoDto get(Long id){
        Optional<Foto> FotoOptional = FotoRepositorio.findById(id);
        FotoDto FotoDto = null;
        if( FotoOptional != null){
            FotoDto = modelMapper.map(FotoOptional.get(), FotoDto.class);
        }
        return FotoDto;
    }

    public List<FotoDto> get( ){
        List<Foto>Fotos = (List<Foto>) FotoRepositorio.findAll();
        List<FotoDto> FotoDtos = Fotos.stream().map(Foto -> modelMapper.map(Foto, FotoDto.class)).collect(Collectors.toList());
        return FotoDtos;
    }

    public FotoDto save( FotoDto FotoDto){
        Foto Foto = modelMapper.map(FotoDto, Foto.class);
        Foto.setStatus(Status.ACTIVE);
        Foto = FotoRepositorio.save(Foto);
        FotoDto.setFotoId(Foto.getFotoId());
        return FotoDto;
    }

    public FotoDto update (FotoDto FotoDto) throws ValidationException{
        FotoDto = get(FotoDto.getFotoId());
        if(FotoDto == null){
            throw new ValidationException(null);//no deja poner string "Registro indefinido" pide lista.
        }
        Foto Foto = modelMapper.map(FotoDto, Foto.class);
        Foto.setStatus(Status.ACTIVE);
        Foto = FotoRepositorio.save(Foto);
        FotoDto = modelMapper.map(Foto, FotoDto.class);
        return FotoDto;
    }

    public void delete (Long id){
        FotoRepositorio.deleteById(id);
    }

    public FotoDto subirFoto(Long propiedadId, MultipartFile archivo) throws java.io.IOException {
        // Validar que el archivo sea una imagen
        if (archivo.isEmpty() || !archivo.getContentType().startsWith("image/")) {
            throw new RuntimeException("Archivo no válido");
        }

        // 1. Subir imagen a Imgur
        String imgurUrl = subirImagenAImgur(archivo);

        // 2. Obtener la propiedad
        Propiedad propiedad = propiedadRepositorio.findByPropiedadId(propiedadId)
            .orElseThrow(() -> new IllegalArgumentException("Propiedad no encontrada"));

        // 3. Crear y guardar la entidad Foto
        Foto foto = new Foto();
        foto.setUrl(imgurUrl);
        foto.setPropiedad(propiedad);
        foto.setStatus(Status.ACTIVE);
        foto = FotoRepositorio.save(foto);

        return modelMapper.map(foto, FotoDto.class);
    }

    private String subirImagenAImgur(MultipartFile archivo) throws java.io.IOException {
        try {
            // Codificar imagen en Base64
            String base64Image = Base64.getEncoder().encodeToString(archivo.getBytes());

            // Crear la solicitud HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Client-ID " + imgurClientId);
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", base64Image);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Hacer POST a la API de Imgur
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<ImgurResponse> response = restTemplate.exchange(
                "https://api.imgur.com/3/image",
                HttpMethod.POST,
                requestEntity,
                ImgurResponse.class
            );

            // Obtener URL de la imagen
            return response.getBody().getData().getLink();

        } catch (IOException e) {
            throw new RuntimeException("Error al procesar la imagen: " + e.getMessage());
        }
    }


}
