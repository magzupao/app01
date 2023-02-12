package com.source.app.repository;

import com.source.app.domain.Conversacion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Conversacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConversacionRepository extends JpaRepository<Conversacion, Long> {}
