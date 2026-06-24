package org.example.services;
import org.example.Main;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.math.BigDecimal;
import org.example.services.ProductoService;
import org.example.models.Producto;
import org.example.dto.ProductoDTO;
import org.hibernate.Session;
import org.example.utils.HibernateUtil;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductoServiceTest {
    private ProductoService service;
    private Session session;
    private Producto producto1;
    private Producto producto2;

    @BeforeAll
    void setUp(){
        service = ProductoService.getInstance();

        session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();

        session.createQuery("delete from Producto").executeUpdate();

        producto1 = new Producto(5, "TestA", true);
        session.persist(producto1);
        producto2 = new Producto(10, "TestB", true);
        session.persist(producto2);
    }

    @AfterAll
    void tearDown(){
        if (session != null && session.isOpen()){
            Transaction tx = session.beginTransaction();
            session.createQuery("delete from Producto").executeUpdate();
            tx.commit();
            session.close();
        }
    }

    @Test
    void crearProductoTest(){
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Nuevo");
        dto.setStock(20);

        ProductoDTO productoDTO = service.crearProducto(dto);

        assertNotNull(productoDTO);
        assertEquals("Nuevo", productoDTO.getNombre());
        assertEquals(20, productoDTO.getStock());

        service.eliminarProducto(productoDTO.getId());
    }

    @Test
    void obtenerProductoTest(){
        ProductoDTO dto = service.obtenerProducto(producto1.getId());

        assertNotNull(dto);
        assertEquals(producto1.getId(), dto.getId());
    }

    @Test
    void obtenerTodosLosProductosTest(){
        List<ProductoDTO> dtos = service.obtenerTodosLosProductos();

        assertNotNull(dtos);
        assertFalse(dtos.isEmpty());
        assertTrue(dtos.stream().anyMatch(p -> p.getId().equals(producto1.getId())));
        assertTrue(dtos.stream().anyMatch(p -> p.getId().equals(producto2.getId())));
    }

    @Test
    void actualizarProductoTest(){
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto2.getId());
        dto.setNombre("Modificado");
        dto.setStock(15);

        ProductoDTO actualizado = service.actualizarProducto(dto);

        assertEquals(producto2.getId(), actualizado.getId());
        assertEquals("Modificado", actualizado.getNombre());
        assertEquals(15, actualizado.getStock());
    }

    @Test
    void eliminarProductoTest() {
        // Crear uno para eliminar
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto2.getId());
        dto.setNombre("Para eliminar");
        dto.setStock(15);
        ProductoDTO creado = service.crearProducto(dto);

        // Eliminar y verificar
        service.eliminarProducto(creado.getId());
        ProductoDTO after = service.obtenerProducto(creado.getId());
        assertNull(after, "El producto debe ser null tras eliminar");
    }

}
















