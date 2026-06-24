package org.example.services;

import jakarta.persistence.criteria.*;
import org.example.dto.*;
import org.example.models.*;

import org.example.utils.HibernateUtil;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CreditService {
    private static CreditService instance;

    public CreditService(){}

    public static CreditService getInstance(){
        if (instance == null){
            instance = new CreditService();
        }
        return instance;
    }

    public CreditDTO pagarCredito(Long id, BigDecimal monto){
        try(Session session = HibernateUtil.getSession()){
            session.beginTransaction();
            Credit credit = session.get(Credit.class, id);
            if (credit == null){
                return null;
            }

            Payment payment = new Payment();
            payment.setCredit(credit);
            payment.setAmount(monto);
            payment.setPayDate(LocalDate.now());
            session.persist(payment);

            BigDecimal suma = credit.getPayments().stream()
                    .map(Payment::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
            ;

            BigDecimal porcentaje = credit.getPrincipalAmount()
                    .divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP)
            ;
            BigDecimal minimo = credit.getPrincipalAmount()
                    .add(credit.getPrincipalAmount().multiply(porcentaje))
            ;

            if (suma.compareTo(minimo) >= 0){
                credit.setStatus(CreditStatus.PAID);
            } else if (credit.getDueDate().isBefore(LocalDate.now())) {
                credit.setStatus(CreditStatus.OVERDUE);
            }else{
                credit.setStatus(CreditStatus.ACTIVE);
            }

            session.merge(credit);
            session.getTransaction().commit();

            return new CreditDTO(
                    credit.getId(),
                    credit.getClientName(),
                    credit.getCreditType(),
                    credit.getPrincipalAmount(),
                    credit.getInterestRate(),
                    credit.getStart_date(),
                    credit.getDueDate(),
                    credit.getStatus()
            );
        }
    }

    public List<CreditDTO> obtenerCreditosCliente(FiltrosDTO filtro){
        try(Session session = HibernateUtil.getSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Credit> cq = cb.createQuery(Credit.class);
            Root<Credit> root = cq.from(Credit.class);

            List<Predicate> filtros = new ArrayList<>();
            filtros.add(cb.equal(root.get("clientName"), filtro.getClient_name()));

            Optional.ofNullable(filtro.getCreditType())
                    .ifPresent(ct -> filtros.add(cb.equal(root.get("creditType"), ct)));

            Optional.ofNullable(filtro.getStart_date())
                    .ifPresent(sd -> filtros.add(cb.greaterThanOrEqualTo(root.get("start_date"), sd)));

            Optional.ofNullable(filtro.getEnd_date())
                    .ifPresent(ed -> filtros.add(cb.lessThanOrEqualTo(root.get("start_date"), ed)));

            Optional.ofNullable(filtro.getMontoMinimo())
                    .ifPresent(min -> filtros.add(cb.greaterThanOrEqualTo(root.get("principalAmount"), min)));

            Optional.ofNullable(filtro.getMontoMaximo())
                    .ifPresent(max -> filtros.add(cb.lessThanOrEqualTo(root.get("principalAmount"), max)));

            cq.where(filtros.toArray(new Predicate[0]))
                    .orderBy(cb.desc(root.get("start_date")));

            List<Credit> credits = session.createQuery(cq).getResultList();

            List<CreditDTO> creditsDTO = new ArrayList<>();
            for (Credit credit : credits){
                creditsDTO.add(new CreditDTO(
                        credit.getId(),
                        credit.getClientName(),
                        credit.getCreditType(),
                        credit.getPrincipalAmount(),
                        credit.getInterestRate(),
                        credit.getStart_date(),
                        credit.getDueDate(),
                        credit.getStatus()
                ));
            }

            return creditsDTO;
        }
    }

    public List<CreditosPagadosDTO> obtenerCreditosPagadosEntreFechas(LocalDate start_date, LocalDate end_date) {
        try (Session session = HibernateUtil.getSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<CreditosPagadosDTO> cq = cb.createQuery(CreditosPagadosDTO.class);
            Root<Credit> root = cq.from(Credit.class);

            cq.select(cb.construct(
                    CreditosPagadosDTO.class,
                    root.get("creditType"),
                    cb.count(root),
                    cb.sum(
                            cb.sum(
                                    root.get("principalAmount"),
                                    cb.prod(
                                            root.get("principalAmount"),
                                            cb.prod(
                                                    root.get("interestRate"),
                                                    cb.literal(new BigDecimal("0.01"))
                                            ))
                            )
                    )
            )).where(cb.and(
                    cb.equal(root.get("status"), CreditStatus.PAID),
                    cb.between(root.get("start_date"), start_date, end_date)
            )).groupBy(root.get("creditType"));

            return session.createQuery(cq).list();
        }
    }

    public List<CreditosNoFinalizadosDTO> obtenerCreditosNoFinalizados() {
        try (Session session = HibernateUtil.getSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<CreditosNoFinalizadosDTO> cq = cb.createQuery(CreditosNoFinalizadosDTO.class);
            Root<Credit> root = cq.from(Credit.class);

            Join<Credit, Payment> joinPay = root.join("payments", JoinType.LEFT);

            cq.select(cb.construct(
                            CreditosNoFinalizadosDTO.class,
                            cb.sum(
                                    root.get("principalAmount"),
                                    cb.prod(
                                            root.get("principalAmount"),
                                            cb.prod(
                                                    root.get("interestRate"),
                                                    cb.literal(new BigDecimal("0.01"))
                                            )
                                    )
                            ),
                            cb.coalesce(cb.sum(joinPay.get("amount")), BigDecimal.ZERO)
                    ))
                    .where(cb.notEqual(root.get("status"), CreditStatus.PAID))
                    .groupBy(root.get("id"), root.get("principalAmount"), root.get("interestRate"));

            return session.createQuery(cq).getResultList();
        }
    }
}