package org.app.services;
import jakarta.persistence.criteria.*;
import org.app.dtos.*;
import org.app.models.*;

import org.app.utils.HibernateUtil;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class AlquilerAutosService {
    private static AlquilerAutosService instance;

    public AlquilerAutosService() {
    }

    public static AlquilerAutosService getInstance(){
        if (instance == null){
            instance = new AlquilerAutosService();
        }
        return instance;
    }

    public VehicleDTO nuevaReserva(String nombreCliente, Long idVehiculo, LocalDate fechaInicio, LocalDate fechaFin){
        try(Session session = HibernateUtil.getSession()){
            session.beginTransaction();
            Vehicle vehicle = session.get(Vehicle.class, idVehiculo);
            if (vehicle == null || !vehicle.isAvailable()){
                return null;
            }

            Reservation reservation = new Reservation();
            reservation.setVehicle(vehicle);
            reservation.setStatus(Status.RESERVED);
            reservation.setStartDate(fechaInicio);
            reservation.setEndDate(fechaFin);
            reservation.setClientName(nombreCliente);
            reservation.setTotalCost(vehicle.getDailyRate().multiply(BigDecimal.valueOf(ChronoUnit.DAYS.between(fechaInicio, fechaFin))));

            vehicle.setAvailable(false);

            session.persist(reservation);
            session.getTransaction().commit();

            return new VehicleDTO(
                    vehicle.getVehicleId(),
                    vehicle.getPlateNumber(),
                    vehicle.getBrand(),
                    vehicle.getModel(),
                    vehicle.getDailyRate(),
                    vehicle.isAvailable()
            );
        }
    }

    public ReservationDTO finalizarReserva(Long idReservation, LocalDate fechaFin){
        try(Session session = HibernateUtil.getSession()){
            session.beginTransaction();
            Reservation reservation = session.get(Reservation.class, idReservation);
            if (reservation == null || reservation.getStatus() == Status.CANCELLED || reservation.getStatus() == Status.COMPLETED){
                return null;
            }
            if (reservation.getEndDate().isBefore(fechaFin)){
                reservation.setTotalCost(reservation.getTotalCost().add((
                        BigDecimal.valueOf(10)
                                .multiply(
                                        BigDecimal.valueOf(
                                                ChronoUnit.DAYS.between(reservation.getEndDate(), fechaFin)
                                        )
                                )
                        )
                ));
            }

            reservation.getVehicle().setAvailable(true);
            reservation.setStatus(Status.COMPLETED);

            session.merge(reservation);
            session.merge(reservation.getVehicle());

            session.getTransaction().commit();

            return new ReservationDTO(
                    reservation.getReservationId(),
                    reservation.getClientName(),
                    reservation.getStartDate(),
                    reservation.getVehicle(),
                    reservation.getEndDate(),
                    reservation.getTotalCost(),
                    reservation.getStatus()
            );
        }
    }

    public List<ReservationDTO> obtenerReservasCliente(FiltrosReservaDTO filtro){
        try(Session session = HibernateUtil.getSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Reservation> cq = cb.createQuery(Reservation.class);
            Root<Reservation> root = cq.from(Reservation.class);

            Join<Reservation, Vehicle> vehicleJoin = root.join("vehicle");

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("clientName"), filtro.getClientName()));

            Optional.ofNullable(filtro.getStatus())
                    .ifPresent(s -> predicates.add(cb.equal(root.get("status"), s)));

            Optional.ofNullable(filtro.getBrand())
                    .ifPresent(b -> predicates.add(cb.equal(vehicleJoin.get("brand"), b)));

            Optional.ofNullable(filtro.getStartDateFrom())
                    .ifPresent(df -> predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), df)));
            Optional.ofNullable(filtro.getStartDateTo())
                    .ifPresent(dt -> predicates.add(cb.lessThanOrEqualTo(root.get("startDate"), dt)));

            cq.where(predicates.toArray(new Predicate[0]))
                    .orderBy(cb.desc(root.get("startDate")));

            List<Reservation> reservations = session.createQuery(cq).list();

            List<ReservationDTO> reservationDTOS = new ArrayList<>();
            for (Reservation reservation : reservations){
                reservationDTOS.add(new ReservationDTO(
                        reservation.getReservationId(),
                        reservation.getClientName(),
                        reservation.getStartDate(),
                        reservation.getVehicle(),
                        reservation.getEndDate(),
                        reservation.getTotalCost(),
                        reservation.getStatus()
                ));
            }
            return reservationDTOS;
        }
    }
}

















