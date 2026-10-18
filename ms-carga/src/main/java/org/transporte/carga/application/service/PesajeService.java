package org.transporte.carga.application.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.transporte.carga.application.port.in.PesajeUseCase;
import org.transporte.carga.application.port.out.PesajeOutService;
import org.transporte.carga.domian.bean.Pesaje;
import org.transporte.carga.domian.constraints.PesajeConstraint;
import org.transporte.carga.domian.constraints.PlacaConstraint;
import org.transporte.carga.domian.model.RecursoNoEncontradoException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PesajeService implements PesajeUseCase {

    private final PesajeOutService pesajeOutService;
    private final PesajeConstraint pesajeConstraint;
    private final PlacaConstraint placaConstraint;

    @Override
    @Transactional
    public Pesaje registrar(Pesaje pesaje) {
        pesaje.setPlaca(pesaje.getPlaca().toUpperCase());
        placaConstraint.validarPlaca(pesaje.getPlaca());
        pesajeConstraint.validarPesos(pesaje.getTaraKg(), pesaje.getPesoBrutoKg());

        pesaje.setPesoNetoKg(pesajeConstraint.calcularPesoNeto(pesaje.getTaraKg(), pesaje.getPesoBrutoKg()));
        pesaje.setEstado(pesajeConstraint.determinarEstado(pesaje.getPesoBrutoKg(), pesaje.getCantidadEjes()));
        pesaje.setFechaPesaje(LocalDateTime.now());
        return pesajeOutService.guardar(pesaje);
    }

    @Override
    @Transactional(readOnly = true)
    public Pesaje buscarPorId(Long id) {
        return pesajeOutService.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el pesaje con id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pesaje> listarPorViaje(Long viajeId) {
        return pesajeOutService.listarPorViaje(viajeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pesaje> listarTodos() {
        return pesajeOutService.listarTodos();
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        buscarPorId(id);
        pesajeOutService.eliminar(id);
    }
}
