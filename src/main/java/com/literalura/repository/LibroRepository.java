package com.literalura.repository;

import com.literalura.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    List<Libro> findByIdioma(String idioma);

    List<Libro> findByTituloIgnoreCase(String titulo);

    List<Libro> findTop10ByOrderByNumeroDescargasDesc();

}