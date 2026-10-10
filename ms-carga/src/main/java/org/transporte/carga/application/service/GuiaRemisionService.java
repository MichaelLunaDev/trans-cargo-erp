package org.transporte.carga.application.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.transporte.carga.application.port.in.GuiaRemisionUseCase;
import org.transporte.carga.application.port.out.GuiaRemisionOutService;
import org.transporte.carga.application.port.out.MercanciaOutService;
import org.transporte.carga.domian.bean.GuiaRemision;
import org.transporte.carga.domian.constraints.GuiaRemisionConstraint;
import org.transporte.carga.domian.enums.EstadoGuia;
import org.transporte.carga.domian.model.RecursoNoEncontradoException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GuiaRemisionService implements GuiaRemisionUseCase {

    private final GuiaRemisionOutService guiaRemisionOutService;
    private final MercanciaOutService mercanciaOutService;
    private final GuiaRemisionConstraint guiaRemisionConstraint;

    @Override
    @Transactional
    public GuiaRemision emitir(GuiaRemision guiaRemision) {
        guiaRemision.setSerie(guiaRemision.getSerie().toUpperCase());
        guiaRemisionConstraint.validarSerie(guiaRemision.getSerie());
        if (mercanciaOutService.buscarPorId(guiaRemision.getMercanciaId()).isEmpty()) {
            throw new RecursoNoEncontradoException("No existe la mercancia con id " + guiaRemision.getMercanciaId());
        }

        Integer ultimoNumero = guiaRemisionOutService.obtenerUltimoNumero(guiaRemision.getSerie());
        guiaRemision.setNumero(ultimoNumero + 1);
        guiaRemision.setEstado(EstadoGuia.EMITIDA);
        guiaRemision.setFechaEmision(LocalDateTime.now());
        return guiaRemisionOutService.guardar(guiaRemision);
    }

    @Override
    @Transactional(readOnly = true)
    public GuiaRemision buscarPorId(Long id) {
        return guiaRemisionOutService.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la guia de remision con id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GuiaRemision> listarPorViaje(Long viajeId) {
        return guiaRemisionOutService.listarPorViaje(viajeId);
    }

    @Override
    @Transactional
    public GuiaRemision anular(Long id) {
        GuiaRemision guiaRemision = buscarPorId(id);
        guiaRemisionConstraint.validarAnulacion(guiaRemision);
        guiaRemision.setEstado(EstadoGuia.ANULADA);
        return guiaRemisionOutService.guardar(guiaRemision);
    }
}
