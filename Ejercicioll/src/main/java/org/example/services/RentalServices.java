package org.example.services;

import jakarta.persistence.criteria.*;

import org.example.dto.*;
import org.example.models.*;
import org.example.utils.HibernateUtil;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RentalServices {
    private static RentalServices instance;

    public RentalServices(){}
    public static RentalServices getInstance(){
        if (instance == null){
            instance = new RentalServices();
        }
        return instance;
    }

    public RentalContractDTO nuevoRentPayment(Long id, BigDecimal monto){
        try(Session session = HibernateUtil.getSession()){
            session.beginTransaction();
            RentalContract contract = session.get(RentalContract.class, id);
            if(contract == null){
                return null;
            }

            RentPayment payment = new RentPayment();
            payment.setContract(contract);
            payment.setAmount(monto);
            payment.setPayDate(LocalDate.now());

            BigDecimal suma = contract.getPayments().stream()
                    .map(RentPayment::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Long meses = ChronoUnit.MONTHS.between(contract.getStartDate(), contract.getEndDate());
            BigDecimal minimo = suma.multiply(BigDecimal.valueOf(meses));

            if (monto.compareTo(minimo) >= 0){
                contract.setStatus(ContractStatus.COMPLETED);
            }else if(contract.getEndDate().isAfter(LocalDate.now())){
                contract.setStatus(ContractStatus.OVERDUE);
            }else{
                contract.setStatus(ContractStatus.ACTIVE);
            }

            session.merge(contract);
            session.getTransaction().commit();

            return new RentalContractDTO(
                    contract.getContractId(),
                    contract.getTenantName(),
                    contract.getPropertyType(),
                    contract.getMonthlyRent(),
                    contract.getStartDate(),
                    contract.getEndDate(),
                    contract.getStatus()
            );
        }
    }

    public List<RentalContractDTO>obtenerContratosDeInquilinoConFiltros(FiltrosDTO filtros){
        try(Session session = HibernateUtil.getSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<RentalContract> cq = cb.createQuery(RentalContract.class);
            Root<RentalContract> root = cq.from(RentalContract.class);

            List<Predicate> predicados = new ArrayList<>();
            predicados.add(cb.equal(root.get("tenantName"), filtros.getTenantName()));

            Optional.ofNullable(filtros.getPropertyType())
                    .ifPresent(pt -> predicados.add(cb.equal(root.get("propertyType"), pt)));

            Optional.ofNullable(filtros.getStartDate())
                    .ifPresent(sd -> predicados.add(cb.greaterThanOrEqualTo(root.get("startDate"), sd)));
            Optional.ofNullable(filtros.getEndDate())
                    .ifPresent(ed -> predicados.add(cb.lessThanOrEqualTo(root.get("startDate"), ed)));

            Optional.ofNullable(filtros.getMontoMinimo())
                    .ifPresent(min -> predicados.add(cb.greaterThanOrEqualTo(root.get("monthlyRent"), min)));
            Optional.ofNullable(filtros.getMontoMaximo())
                    .ifPresent(max -> predicados.add(cb.lessThanOrEqualTo(root.get("monthlyRent"), max)));

            cq.where(predicados.toArray(new Predicate[0]))
                    .orderBy(cb.desc(root.get("startDate")));
            List<RentalContract> contratos = session.createQuery(cq).list();

            List<RentalContractDTO> resultado = new ArrayList<>();
            // Mapear los resultados a LibroDTO
            for (RentalContract rc : contratos) {
                resultado.add(new RentalContractDTO(
                        rc.getContractId(),
                        rc.getTenantName(),
                        rc.getPropertyType(),
                        rc.getMonthlyRent(),
                        rc.getStartDate(),
                        rc.getEndDate(),
                        rc.getStatus()
                ));
            }

            return resultado;
        }
    }

    public List<ContratosCompletadosDTO> obtenerContratosCompletados(LocalDate fechaInicio, LocalDate fechaFin){
        try(Session session = HibernateUtil.getSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ContratosCompletadosDTO> cq = cb.createQuery(ContratosCompletadosDTO.class);
            Root<RentalContract> root = cq.from(RentalContract.class);

            cq.select(cb.construct(
                    ContratosCompletadosDTO.class,
                    root.get("propertyType"),
                    cb.count(root),
                    cb.sum(
                            cb.prod(
                                    root.get("monthlyRent"),
                                    cb.function(
                                            "datediff",
                                            Integer.class,
                                            cb.literal("MONTH"),
                                            root.get("startDate"),
                                            root.get("endDate")
                                    )
                            )
                    )
            )).where(cb.and(
                    cb.equal(root.get("status"), ContractStatus.COMPLETED),
                    cb.between(root.get("startDate"), fechaInicio, fechaFin)
            )).groupBy(root.get("propertyType"));

            return session.createQuery(cq).list();
        }
    }

    public List<ContratoNoCompletadosDTO> obtenerContratosNoCompletados(){
        try(Session session = HibernateUtil.getSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ContratoNoCompletadosDTO> cq = cb.createQuery(ContratoNoCompletadosDTO.class);
            Root<RentalContract> root = cq.from(RentalContract.class);

            Join<RentalContract, RentPayment> join = root.join("payments", JoinType.LEFT);

            cq.select(cb.construct(
                    ContratoNoCompletadosDTO.class,
                    root.get("contractId"),
                    cb.sum(
                            cb.prod(
                                    root.get("monthlyRent"),
                                    cb.function(
                                            "datediff",
                                            Integer.class,
                                            cb.literal("MONTH"),
                                            root.get("startDate"),
                                            root.get("endDate")
                                    )
                            )
                    ),
                    cb.coalesce(cb.sum(join.get("amount")), BigDecimal.ZERO)
            )).where(cb.notEqual(root.get("status"), ContractStatus.COMPLETED))
              .groupBy(root.get("contractId"));

            return session.createQuery(cq).list();
        }
    }
}
