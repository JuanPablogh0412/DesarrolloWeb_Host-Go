package com.host_go.host_go.Servicios;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.host_go.host_go.Dtos.PagoDto;
import com.host_go.host_go.Repositorios.PagoRepositorio;
import com.host_go.host_go.Repositorios.SolicitudRepositorio;
import com.host_go.host_go.modelos.Pago;
import com.host_go.host_go.modelos.Solicitud;
import com.host_go.host_go.modelos.Status;

@Service
public class PagoServicio {

    @Autowired
    PagoRepositorio pagoRepositorio;
    @Autowired
    private SolicitudRepositorio solicitudRepositorio;
    @Autowired
    ModelMapper modelMapper;

    public PagoDto get(Long id){
        Optional<Pago> pagoOptional = pagoRepositorio.findById(id);
        PagoDto pagoDto = null;
        if(pagoOptional.isPresent()){
            pagoDto = modelMapper.map(pagoOptional.get(), PagoDto.class);
        }
        return pagoDto;
    }

    public List<PagoDto> get(){
        List<Pago> pagos = (List<Pago>) pagoRepositorio.findAll();
        List<PagoDto> pagoDtos = pagos.stream()
                .map(pago -> modelMapper.map(pago, PagoDto.class))
                .collect(Collectors.toList());
        return pagoDtos;
    }

    public PagoDto save(PagoDto pagoDto){
        Pago pago = modelMapper.map(pagoDto, Pago.class);

        Solicitud solicitud = solicitudRepositorio.findById(pagoDto.getSolicitud().getSolicitudId())
            .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));
        pago.setSolicitud(solicitud);

        LocalDate fechaFin = LocalDate.parse(solicitud.getFechaFin());
        if (fechaFin.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La solicitud está vencida y no se puede pagar");
        }
        pago.setBanco(pagoDto.getBanco());
        pago.setNumCuenta(pagoDto.getNumCuenta());
        pago.setValor(solicitud.getCostoTotal());
        pago.setStatus(Status.ACTIVE);
        
        pago = pagoRepositorio.save(pago);
        return modelMapper.map(pago, PagoDto.class);
    }

    public PagoDto update(PagoDto pagoDto) throws ValidationException{
        PagoDto existente = get(pagoDto.getPagoId());
        if(existente == null){
            throw new ValidationException(null);
        }
        Pago pago = modelMapper.map(pagoDto, Pago.class);
        pago.setStatus(Status.ACTIVE);
        pago = pagoRepositorio.save(pago);
        return modelMapper.map(pago, PagoDto.class);
    }

    public void delete(Long id){
        pagoRepositorio.deleteById(id);
    }

}
