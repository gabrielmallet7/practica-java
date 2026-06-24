package org.app.services;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.*;
import java.math.BigDecimal;
import org.app.services.*;
import org.app.models.*;
import org.app.dtos.*;
import org.hibernate.Session;
import org.app.utils.HibernateUtil;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AlquilarAutosServiceTest {
    private AlquilerAutosService service;
    private Session session;

    private Vehicle vehicle1;
    private Vehicle vehicle2;
    private Long idVeh1;
    private Long idVeh2;

    private Reservation res1;
    private Long idRes1;

    @BeforeAll
    void setUp() {
        service = AlquilerAutosService.getInstance();

        session = HibernateUtil.getSession();
        session.beginTransaction();

        // Crear vehículos de prueba
        vehicle1 = new Vehicle();
        vehicle1.setPlateNumber("AAA111");
        vehicle1.setBrand("Toyota");
        vehicle1.setModel("Corolla");
        vehicle1.setDailyRate(new BigDecimal("50.00"));
        vehicle1.setAvailable(true);
        session.persist(vehicle1);

        vehicle2 = new Vehicle();
        vehicle2.setPlateNumber("BBB222");
        vehicle2.setBrand("Ford");
        vehicle2.setModel("Focus");
        vehicle2.setDailyRate(new BigDecimal("60.00"));
        vehicle2.setAvailable(false);
        session.persist(vehicle2);

        session.getTransaction().commit();

        idVeh1 = vehicle1.getVehicleId();
        idVeh2 = vehicle2.getVehicleId();

        // Crear una reserva existente para finalizarla más adelante
        session.beginTransaction();
        res1 = new Reservation();
        res1.setVehicle(vehicle1);
        res1.setStatus(Status.RESERVED);
        res1.setStartDate(LocalDate.now());
        res1.setEndDate(LocalDate.now().plusDays(2));
        res1.setClientName("Juan Perez");
        res1.setTotalCost(vehicle1.getDailyRate().multiply(BigDecimal.valueOf(2)));
        vehicle1.setAvailable(false);  // marcamos no disponible
        session.merge(vehicle1);
        session.persist(res1);
        session.getTransaction().commit();

        idRes1 = res1.getReservationId();
        session.beginTransaction();
        Reservation resLuis = new Reservation();
        resLuis.setVehicle(vehicle1);
        resLuis.setStatus(Status.RESERVED);
        resLuis.setStartDate(LocalDate.now());
        resLuis.setEndDate(LocalDate.now().plusDays(1));
        resLuis.setClientName("Luis Martínez");
        resLuis.setTotalCost(vehicle1.getDailyRate());

        // marca vehículo no disponible
        vehicle1.setAvailable(false);
        session.merge(vehicle1);

        session.persist(resLuis);
        session.getTransaction().commit();

    }

    @AfterAll
    void tearDown() {
        if (session != null && session.isOpen()) {
            session.beginTransaction();
            session.createQuery("delete from Reservation").executeUpdate();
            session.createQuery("delete from Vehicle").executeUpdate();
            session.getTransaction().commit();
            session.close();
        }
    }

    @Test
    void testNuevaReserva_Success() {
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end   = start.plusDays(3);

        VehicleDTO dto = service.nuevaReserva("María López", idVeh1, start, end);

        assertNotNull(dto, "El DTO no debe ser null cuando la reserva es correcta");
        assertEquals(idVeh1, dto.getVehicleId(), "El ID de vehículo debe coincidir");
        assertFalse(dto.isAvailable(), "El vehículo debe quedar marcado como no disponible");
    }

    @Test
    void testNuevaReserva_VehicleNotAvailable() {
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end   = start.plusDays(2);

        VehicleDTO dto = service.nuevaReserva("Pedro Ruiz", idVeh2, start, end);

        assertNull(dto, "Debe retornar null si el vehículo no está disponible");
    }

    @Test
    void testFinalizarReserva_SuccessWithoutPenalty() {
        // ya existe res1 para Juan Perez que termina hoy+2
        LocalDate finish = res1.getEndDate();

        ReservationDTO dto = service.finalizarReserva(idRes1, finish);

        assertNotNull(dto, "El DTO no debe ser null al finalizar una reserva válida");
        assertEquals(Status.COMPLETED, dto.getStatus(), "El estado debe quedar COMPLETED");
        assertTrue(dto.getVehicle().isAvailable(), "El vehículo debe quedar disponible");
        assertEquals(res1.getTotalCost(), dto.getTotalCost(), "No debe aplicarse penalización");
    }

    @Test
    void testFinalizarReserva_WithPenalty() {
        // Creamos otra reserva para penalizar días extra
        session.beginTransaction();
        Reservation res2 = new Reservation();
        res2.setVehicle(vehicle1);
        res2.setStatus(Status.RESERVED);
        LocalDate s2 = LocalDate.now().plusDays(3);
        LocalDate e2 = s2.plusDays(1);
        res2.setStartDate(s2);
        res2.setEndDate(e2);
        res2.setClientName("Luis Martínez");
        res2.setTotalCost(vehicle1.getDailyRate());
        vehicle1.setAvailable(false);
        session.merge(vehicle1);
        session.persist(res2);
        session.getTransaction().commit();

        Long idRes2 = res2.getReservationId();
        LocalDate lateReturn = e2.plusDays(2);

        ReservationDTO dto2 = service.finalizarReserva(idRes2, lateReturn);

        assertNotNull(dto2);
        assertEquals(Status.COMPLETED, dto2.getStatus());
        assertTrue(dto2.getVehicle().isAvailable());
        // costo original + (10 × 2 días extra)
        BigDecimal expected = vehicle1.getDailyRate()
                .add(BigDecimal.valueOf(10L * 2));
        assertEquals(0, dto2.getTotalCost().compareTo(expected),
                "Debe aplicarse penalización por días extra");
    }

    @Test
    void testFinalizarReserva_NonExisting() {
        ReservationDTO dto = service.finalizarReserva(9999L, LocalDate.now());
        assertNull(dto, "Debe retornar null si la reserva no existe");
    }

    @Test
    void testObtenerReservasCliente_FilterByClient() {
        // 1) Creamos la reserva antes de filtrar
        LocalDate inicio = LocalDate.now();
        LocalDate fin    = inicio.plusDays(1);
        service.nuevaReserva("Luis Martínez", idVeh1, inicio, fin);

        // 2) Ejecutamos el filtro
        FiltrosReservaDTO filtro = new FiltrosReservaDTO();
        filtro.setClientName("Luis Martínez");
        List<ReservationDTO> list = service.obtenerReservasCliente(filtro);

        // 3) Assertions
        assertNotNull(list);
        assertFalse(list.isEmpty(), "La lista debe contener la reserva de Luis Martínez");
        assertTrue(list.stream()
                .allMatch(r -> "Luis Martínez".equals(r.getClientName())));
    }

}
