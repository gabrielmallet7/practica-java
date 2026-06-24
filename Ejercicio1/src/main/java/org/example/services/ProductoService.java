package org.example.services;

import org.example.dto.ProductoDTO;
import org.example.models.Producto;
import org.example.utils.HibernateUtil;

import org.hibernate.Session;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class ProductoService {
    private static ProductoService instance;

    private ProductoService(){}

    public static ProductoService getInstance(){
        if (instance == null){
            instance = new ProductoService();
        }
        return instance;
    }

    public ProductoDTO crearProducto(ProductoDTO productoDTO){
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()){
            session.beginTransaction();

            Producto producto = new Producto(productoDTO.getStock(), productoDTO.getNombre(), true);

            session.persist(producto);
            session.getTransaction().commit();

            productoDTO.setId(producto.getId());

           return productoDTO;
        }
    }

    public ProductoDTO obtenerProducto(Long id){
        try(Session session = HibernateUtil.getSessionFactory().getCurrentSession()){
            Producto producto = session.get(Producto.class, id);
            if (producto == null || !producto.getEsActivo()){
                return null;
            }

            return new ProductoDTO(
                    producto.getId(),
                    producto.getNombre(),
                    producto.getStock(),
                    producto.getEsActivo()
            );
        }
    }

    public List<ProductoDTO> obtenerTodosLosProductos(){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Producto> cq = cb.createQuery(Producto.class);
            Root<Producto> root = cq.from(Producto.class);

            Predicate activoPredicate = cb.equal(root.get("esActivo"), true);
            cq.where(activoPredicate);

            List<Producto> productos = session.createQuery(cq).list();

            List<ProductoDTO> productoDTOS = new ArrayList<>();
            for(Producto producto : productos){
                productoDTOS.add(new ProductoDTO(
                        producto.getId(),
                        producto.getNombre(),
                        producto.getStock(),
                        producto.getEsActivo()
                ));
            }
            return productoDTOS;
        }
    }

    public ProductoDTO actualizarProducto(ProductoDTO productoDTO){
        try(Session session = HibernateUtil.getSessionFactory().getCurrentSession()){
            session.beginTransaction();
            Producto producto = session.get(Producto.class, productoDTO.getId());
            if (producto == null || !producto.getEsActivo()){
                return null;
            }

            producto.setNombre(productoDTO.getNombre());
            producto.setStock(productoDTO.getStock());

            session.merge(producto);
            session.getTransaction().commit();

            return productoDTO;
        }
    }

    public void eliminarProducto(Long id){
        try(Session session = HibernateUtil.getSessionFactory().getCurrentSession()){
            session.beginTransaction();
            Producto producto = session.get(Producto.class, id);
            if(producto != null){
                producto.setEsActivo(false);
                session.merge(producto);
            }
            session.getTransaction().commit();
        }
    }
}




























