package org.example.services;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.Session;
import org.example.dto.*;
import org.example.models.*;
import org.example.utils.HibernateUtil;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreditServiceTest {
    private CreditService logica;
    private Session session;

    private Credit creditActive;
    private Credit creditOverdue;
    private Credit creditPaid;

    @BeforeAll
    void setup() {
        logica = CreditService.getInstance();

        if (session != null && session.isOpen()) {
            session.close();
        }

        session = HibernateUtil.getSession();
        session.beginTransaction();

        // Eliminar hijos primero por clave foránea
        session.createQuery("DELETE FROM Payment").executeUpdate();
        session.createQuery("DELETE FROM Credit").executeUpdate();

        session.getTransaction().commit();

        session.beginTransaction();

        // crear créditos de prueba
        creditActive = new Credit();
        creditActive.setClientName("ClienteA");
        creditActive.setCreditType(CreditType.PERSONAL);
        creditActive.setPrincipalAmount(new BigDecimal("1000"));
        creditActive.setInterestRate(new BigDecimal("10"));
        creditActive.setStart_date(LocalDate.of(2024, 05, 06));
        creditActive.setDueDate(LocalDate.of(2026, 03, 03));
        creditActive.setStatus(CreditStatus.ACTIVE);
        session.persist(creditActive);

        creditOverdue = new Credit();
        creditOverdue.setClientName("ClienteB");
        creditOverdue.setCreditType(CreditType.BUSINESS);
        creditOverdue.setPrincipalAmount(new BigDecimal("2000"));
        creditOverdue.setInterestRate(new BigDecimal("5"));
        creditOverdue.setStart_date(LocalDate.of(2024, 05, 06));
        creditOverdue.setDueDate(LocalDate.of(2025, 03, 03));
        creditOverdue.setStatus(CreditStatus.ACTIVE);
        session.persist(creditOverdue);

        creditPaid = new Credit();
        creditPaid.setClientName("ClienteC");
        creditPaid.setCreditType(CreditType.PERSONAL);
        creditPaid.setPrincipalAmount(new BigDecimal("500"));
        creditPaid.setInterestRate(new BigDecimal("20"));
        creditPaid.setStart_date(LocalDate.of(2024, 05, 06));
        creditPaid.setDueDate(LocalDate.of(2026, 03, 03));
        creditPaid.setStatus(CreditStatus.ACTIVE);
        session.persist(creditPaid);

        session.getTransaction().commit();

        session.beginTransaction();
        BigDecimal montoMinimo = creditPaid.getPrincipalAmount()
                .add(creditPaid.getPrincipalAmount()
                        .multiply(creditPaid.getInterestRate().divide(new BigDecimal("100"))));
        logica.pagarCredito(creditPaid.getId(), montoMinimo);
        session.getTransaction().commit();

    }

    @AfterAll
    void tearDown() {
        if (session != null && session.isOpen()) {
            session.beginTransaction();
            session.createQuery("delete from Payment").executeUpdate();
            session.createQuery("delete from Credit").executeUpdate();
            session.getTransaction().commit();
            session.close();
        }
    }

    @Test
    void testPagarCredito_ActiveToActive() {
        BigDecimal pago = new BigDecimal("50");
        CreditDTO result = logica.pagarCredito(creditActive.getId(), pago);
        assertNotNull(result);
        assertEquals(CreditStatus.ACTIVE, result.getStatus());
    }

    @Test
    void testPagarCredito_ActiveToPaid() {
        BigDecimal minimo = creditPaid.getPrincipalAmount()
                .add(creditPaid.getPrincipalAmount()
                        .multiply(creditPaid.getInterestRate().divide(new BigDecimal("100"))));
        CreditDTO result = logica.pagarCredito(creditPaid.getId(), minimo);
        assertNotNull(result);
        assertEquals(CreditStatus.PAID, result.getStatus());
    }

    @Test
    void testPagarCredito_ActiveToOverdue() {
        BigDecimal pago = new BigDecimal("10");
        CreditDTO result = logica.pagarCredito(creditOverdue.getId(), pago);
        assertNotNull(result);
        assertEquals(CreditStatus.OVERDUE, result.getStatus());
    }

    @Test
    void testPagarCredito_Nonexistent() {
        CreditDTO result = logica.pagarCredito(-1L, BigDecimal.ONE);
        assertNull(result);
    }

    @Test
    void testObtenerCreditosCliente_Filters() {
        FiltrosDTO filtros = new FiltrosDTO();
        filtros.setClient_name("ClienteA");
        List<CreditDTO> list = logica.obtenerCreditosCliente(filtros);
        assertEquals(1, list.size());
        assertEquals("ClienteA", list.get(0).getClientName());
    }

    @Test
    void testObtenerCreditosPagadosEntreFechas() {
        LocalDate start = LocalDate.now().minusDays(40);
        LocalDate end = LocalDate.now();
        List<CreditosPagadosDTO> resumen = logica.obtenerCreditosPagadosEntreFechas(start, end);
        assertFalse(resumen.isEmpty());
        assertTrue(resumen.stream().anyMatch(r -> r.getCreditType() == CreditType.PERSONAL));
        assertTrue(resumen.stream().anyMatch(r -> r.getCreditType() == CreditType.BUSINESS));
    }

    @Test
    void testObtenerCreditosNoFinalizados() {
        List<CreditosNoFinalizadosDTO> pendientes = logica.obtenerCreditosNoFinalizados();
        assertFalse(pendientes.stream().anyMatch(p -> p.getCantidadPrestada().equals(creditPaid.getPrincipalAmount())));
        assertEquals(2, pendientes.size());
    }
}
