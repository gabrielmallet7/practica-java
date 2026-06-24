package org.app.services;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.app.dtos.*;
import org.app.models.*;
import org.app.utils.HibernateUtil;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;

public class DonationService {
    private static DonationService instance;

    public DonationService() {
    }
    public static DonationService getInstance(){
        if (instance == null){
            instance = new DonationService();
        }
        return  instance;
    }

    public DonationDTO registrarDonacion(DonationDTO donationDTO){
        try(Session session = HibernateUtil.getSession()){
            session.beginTransaction();
            Donation donation = new Donation(
                    donationDTO.getDonorName(),
                    donationDTO.getDonorType(),
                    donationDTO.getAmount(),
                    donationDTO.getDonationDate(),
                    donationDTO.getCategory()
            );

            session.persist(donation);
            session.getTransaction().commit();

            donationDTO.setDonationId(donation.getDonationId());
            donationDTO.setStatus(donation.getStatus());
            return donationDTO;
        }
    }

    public DonationAssignmentDTO asignarDonacion(Long idDonacion, LocalDate fechaAsignacion, String notas){
        try(Session session = HibernateUtil.getSession()){
            session.beginTransaction();
            Donation donation = session.get(Donation.class, idDonacion);
            if (donation == null || donation.getStatus() == DonationStatus.ASSIGNED){
                return null;
            }

            DonationAssignment donationAssignment = new DonationAssignment(
                    donation,
                    notas,
                    fechaAsignacion
            );

            session.persist(donationAssignment);
            session.getTransaction().commit();

            return new DonationAssignmentDTO(
                    donationAssignment.getDonation(),
                    donationAssignment.getAssignmentId(),
                    donationAssignment.getNotes(),
                    donationAssignment.getAssignedDate()
            );
        }
    }

    public List<RecaudacionDonanteDTO> totalRecaudadoPorTipoDeDonante(){
        try(Session session = HibernateUtil.getSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<RecaudacionDonanteDTO> cq = cb.createQuery(RecaudacionDonanteDTO.class);
            Root<Donation> root = cq.from(Donation.class);

            cq.select(cb.construct(
                    RecaudacionDonanteDTO.class,
                    root.get("donorType"),
                    cb.count(root),
                    cb.sum(root.get("amount"))
            )).groupBy(root.get("donorType")).orderBy(cb.desc(cb.sum(root.get("amount"))));

            return session.createQuery(cq).list();
        }
    }

    public List<RecaudacionCategoriaDTO> totalRecaudadoPorCategoria(){
        try(Session session = HibernateUtil.getSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<RecaudacionCategoriaDTO> cq = cb.createQuery(RecaudacionCategoriaDTO.class);
            Root<Donation> root = cq.from(Donation.class);

            cq.select(cb.construct(
                    RecaudacionCategoriaDTO.class,
                    root.get("category"),
                    cb.sum(
                            cb.<Long>selectCase()
                                    .when(cb.equal(root.get("status"), DonationStatus.RECEIVED), 1L)
                                    .otherwise(0L)
                    ),
                    cb.sum(
                            cb.<Long>selectCase()
                                    .when(cb.equal(root.get("status"), DonationStatus.ASSIGNED), 1L)
                                    .otherwise(0L)
                    ),
                    cb.sum(root.get("amount"))
            )).groupBy(root.get("category")).orderBy(cb.desc(cb.sum(root.get("amount"))));

            return session.createQuery(cq).list();
        }
    }
}



























