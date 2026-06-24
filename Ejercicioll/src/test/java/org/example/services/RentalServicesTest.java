package org.example.services;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.example.dto.*;
import org.example.models.*;
import org.hibernate.Session;
import org.example.utils.HibernateUtil;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RentalServicesTest {
    private RentalServices service;
    private Session session;
    private RentalContract contractActive;
    private RentalContract contractOverdue;
    private RentalContract contractCompleted;

    @BeforeAll
    void setUp() {
        // Initialize service instance
        service = RentalServices.getInstance();

        // Set up test data
        session = HibernateUtil.getSession();
        session.beginTransaction();

        // Clear existing data
        session.createQuery("delete from RentPayment").executeUpdate();
        session.createQuery("delete from RentalContract").executeUpdate();

        // Create test contracts
        contractActive = new RentalContract();
        contractActive.setTenantName("TenantA");
        contractActive.setPropertyType(PropertyType.APARTMENT);
        contractActive.setMonthlyRent(new BigDecimal("1000"));
        contractActive.setStartDate(LocalDate.now().minusMonths(3));
        contractActive.setEndDate(LocalDate.now().plusMonths(3));
        contractActive.setStatus(ContractStatus.ACTIVE);

        contractOverdue = new RentalContract();
        contractOverdue.setTenantName("TenantB");
        contractOverdue.setPropertyType(PropertyType.HOUSE);
        contractOverdue.setMonthlyRent(new BigDecimal("500"));
        contractOverdue.setStartDate(LocalDate.now().minusMonths(6));
        contractOverdue.setEndDate(LocalDate.now().minusMonths(1));
        contractOverdue.setStatus(ContractStatus.ACTIVE);

        contractCompleted = new RentalContract();
        contractCompleted.setTenantName("TenantC");
        contractCompleted.setPropertyType(PropertyType.OFFICE);
        contractCompleted.setMonthlyRent(new BigDecimal("2000"));
        contractCompleted.setStartDate(LocalDate.now().minusMonths(12));
        contractCompleted.setEndDate(LocalDate.now().minusMonths(6));
        contractCompleted.setStatus(ContractStatus.ACTIVE);

        session.persist(contractActive);
        session.persist(contractOverdue);
        session.persist(contractCompleted);

        session.getTransaction().commit();

        // Mark the completed contract as COMPLETED by paying full amount
        BigDecimal months = BigDecimal.valueOf(
                ChronoUnit.MONTHS.between(contractCompleted.getStartDate(), contractCompleted.getEndDate())
        );
        BigDecimal fullAmount = contractCompleted.getMonthlyRent().multiply(months);
        service.nuevoRentPayment(contractCompleted.getContractId(), fullAmount);
    }

    @AfterAll
    void tearDown() {
        if (session != null && session.isOpen()) {
            session.beginTransaction();
            session.createQuery("delete from RentPayment").executeUpdate();
            session.createQuery("delete from RentalContract").executeUpdate();
            session.getTransaction().commit();
            session.close();
        }
    }

    @Test
    void testNuevoRentPayment_Nonexistent() {
        RentalContractDTO dto = service.nuevoRentPayment(-1L, BigDecimal.ONE);
        assertNull(dto);
    }

    @Test
    void testNuevoRentPayment_ToCompleted() {
        RentalContractDTO dto = service.nuevoRentPayment(contractActive.getContractId(),
                contractActive.getMonthlyRent()
                        .multiply(BigDecimal.valueOf(
                                ChronoUnit.MONTHS.between(
                                        contractActive.getStartDate(), contractActive.getEndDate()
                                )
                        ))
        );
        assertNotNull(dto);
        assertEquals(ContractStatus.COMPLETED, dto.getStatus());
    }

    @Test
    void testObtenerContratosDeInquilinoConFiltros() {
        FiltrosDTO filtros = new FiltrosDTO();
        filtros.setTenantName("TenantA");
        List<RentalContractDTO> list = service.obtenerContratosDeInquilinoConFiltros(filtros);
        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertTrue(list.stream().allMatch(r -> r.getTenantName().equals("TenantA")));
    }

    @Test
    void testObtenerContratosCompletados() {
        LocalDate start = LocalDate.now().minusYears(1);
        LocalDate end = LocalDate.now();
        List<ContratosCompletadosDTO> resumen = service.obtenerContratosCompletados(start, end);
        assertNotNull(resumen);
        assertFalse(resumen.isEmpty());
        assertTrue(resumen.stream().anyMatch(r -> r.getPropertyType() == PropertyType.OFFICE));
    }

    @Test
    void testObtenerContratosNoCompletados() {
        List<ContratoNoCompletadosDTO> pendientes = service.obtenerContratosNoCompletados();
        assertNotNull(pendientes);
        assertEquals(2, pendientes.size());
        assertTrue(pendientes.stream().allMatch(d -> d.getMontoPagado().compareTo(d.getMontoTotal()) < 0));
    }
}
