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
public class DonationServiceTest {
    private DonationService service;
    private Session session;
    private Donation donation1;
    private Donation donation2;
    private Donation donation3;

    @BeforeAll
    void setUp(){
        service = DonationService.getInstance();

        session = HibernateUtil.getSession();
        session.beginTransaction();

        donation1 = new Donation(
               "Marcos",
               DonorType.INDIVIDUAL,
               BigDecimal.valueOf(1000),
               LocalDate.of(2025, 9,5),
               "uno"
        );
        donation1.setStatus(DonationStatus.ASSIGNED);
        session.persist(donation1);

        donation2 = new Donation(
                "Compania",
                DonorType.COMPANY,
                BigDecimal.valueOf(50000),
                LocalDate.of(2024,3, 3),
                "dos"
        );
        session.persist(donation2);

        donation3 = new Donation(
                "Marcos",
                DonorType.INDIVIDUAL,
                BigDecimal.valueOf(500),
                LocalDate.of(2025,3, 3),
                "dos"
        );
        session.persist(donation3);

        session.getTransaction().commit();
    }

    @AfterAll
    void tearDown(){
        if(session != null && session.isOpen()){
            session.beginTransaction();
            session.createQuery("delete from DonationAssignment").executeUpdate();
            session.createQuery("delete from Donation").executeUpdate();
            session.getTransaction().commit();
            session.close();
        }
    }

    @Test
    void registrarDonacionTest(){
        DonationDTO donation = new DonationDTO(
                "Juan",
                DonorType.INDIVIDUAL,
                BigDecimal.valueOf(500),
                LocalDate.of(2025,3, 3),
                "dos"
        );

        DonationDTO donationDTO = service.registrarDonacion(donation);

        assertNotNull(donationDTO);
        assertEquals(4, donationDTO.getDonationId());
        assertEquals(DonationStatus.RECEIVED, donationDTO.getStatus());

        try (Session session1 = HibernateUtil.getSession()) {
            session.beginTransaction();
            Donation d = session.get(Donation.class, donationDTO.getDonationId());
            if (d != null) {
                session.remove(d);
            }
            session.getTransaction().commit();
        }
    }

    @Test
    void asignarDonacionTestNoExiste(){
        DonationAssignmentDTO dto = service.asignarDonacion(-1L, LocalDate.now(), "notas");

        assertNull(dto);
    }

    @Test
    void asignarDonacionTestYaEstaAsignada(){
        DonationAssignmentDTO dto = service.asignarDonacion(1L, LocalDate.now(), "notas");

        assertNull(dto);
    }

    @Test
    void asignarDonacionTestOk(){
        DonationAssignmentDTO dto = service.asignarDonacion(2L, LocalDate.now(), "notas");

        assertNotNull(dto);
        assertEquals(donation2.getDonationId(), dto.getDonation().getDonationId());
    }

    @Test
    void totalRecaudadoPorTipoDeDonanteTest(){
        List<RecaudacionDonanteDTO> dtos = service.totalRecaudadoPorTipoDeDonante();

        assertNotNull(dtos);
        assertFalse(dtos.isEmpty());
        assertEquals(2, dtos.size());

        assertTrue(dtos.stream().anyMatch(dto ->
                dto.getDonorType().toString().equals("COMPANY") &&
                dto.getCantDonaciones() == 1 &&
                dto.getMontoTotal().compareTo(BigDecimal.valueOf(50000)) == 0
        ));

        assertTrue(dtos.stream().anyMatch(dto ->
                dto.getDonorType().toString().equals("INDIVIDUAL") &&
                dto.getCantDonaciones() == 2 &&
                dto.getMontoTotal().compareTo(BigDecimal.valueOf(1500)) == 0
        ));
    }

    @Test
    void totalRecaudadoPorCategoriaTest(){
        List<RecaudacionCategoriaDTO> resultados = service.totalRecaudadoPorCategoria();

        assertNotNull(resultados);
        assertFalse(resultados.isEmpty());
        assertEquals(2, resultados.size());

        assertTrue(resultados.stream().anyMatch(dto ->
                dto.getCategoria().equals("uno") &&
                dto.getDonacionesAsingnadas() == 1 &&
                dto.getDonacionesRecibidas() == 0 &&
                dto.getTotalRecaudado().compareTo(BigDecimal.valueOf(1000))  == 0
        ));

        assertTrue(resultados.stream().anyMatch(dto ->
                dto.getCategoria().equals("dos") &&
                dto.getDonacionesAsingnadas() == 0 &&
                dto.getDonacionesRecibidas() == 2 &&
                dto.getTotalRecaudado().compareTo(BigDecimal.valueOf(50500))  == 0
        ));
    }
}
















