package com.example.specificationexmp.repository;

import com.example.specificationexmp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserRepository extends JpaRepository<User,Integer>, JpaSpecificationExecutor<User> {

    @Query("SELECT c FROM User c")
    Page<User> allUser(Pageable pageable);

    @Query("SELECT c FROM User c where c.name=:name")
    Page<User> allUserForName(@Param(("name"))String name,Pageable pageable);
}
