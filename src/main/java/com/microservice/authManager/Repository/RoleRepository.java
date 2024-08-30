package com.microservice.authManager.Repository;

import com.microservice.authManager.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    @Query(value = "SELECT * FROM role br WHERE br.provide = :provide",
            nativeQuery = true)
    Optional<Role> findByProvide(Long provide);
}
