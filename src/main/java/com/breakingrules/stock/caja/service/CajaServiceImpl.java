package com.breakingrules.stock.caja.service;

import com.breakingrules.stock.caja.entity.MovimientoCaja;
import com.breakingrules.stock.caja.entity.TipoMovimiento;
import com.breakingrules.stock.caja.repository.MovimientoCajaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CajaServiceImpl implements CajaService {

    private final MovimientoCajaRepository movimientoCajaRepository;

    @Override
    public BigDecimal obtenerSaldoActual() {
        List<MovimientoCaja> movs = movimientoCajaRepository.findAll();

        BigDecimal saldo = BigDecimal.ZERO;

        for (MovimientoCaja m : movs) {
            if (m.getTipo() == TipoMovimiento.INGRESO) {
                saldo = saldo.add(m.getMonto());
            } else {
                saldo = saldo.subtract(m.getMonto());
            }
        }

        return saldo;
    }

    @Override
    @Transactional
    public void registrarMovimiento(TipoMovimiento tipo, BigDecimal monto, String referencia) {
        MovimientoCaja mov = new MovimientoCaja();
        mov.setFecha(LocalDateTime.now());
        mov.setTipo(tipo);
        mov.setMonto(monto);
        mov.setReferencia(referencia);

        movimientoCajaRepository.save(mov);
    }

    @Override
    public List<MovimientoCaja> listarMovimientos(){
        return movimientoCajaRepository.findAll();
    }
}