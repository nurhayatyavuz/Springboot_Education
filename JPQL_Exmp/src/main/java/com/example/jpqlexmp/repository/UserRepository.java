package com.example.jpqlexmp.repository;

import com.example.jpqlexmp.dto.UserNameAndAgeDto;
import com.example.jpqlexmp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Integer> {


    //methotlar
    @Query("SELECT u from User u")
    List<User> tumKullaniciYazdir();

    //isim listesi döndürme
    @Query("SELECT u.name from User u")
    List<String> isimListes();

    @Query("SELECT u.age from User u WHERE u.age= :ageyas")
    Optional<User> yas(@Param("age") Integer age);


    //yaşı 30 dan büyük olanları bulma
    @Query("SELECT COUNT(u) from User u  where u.age> :age")
    Integer sayiBul(Integer age);

    @Query("SELECT SUM (u.age) FROM User u")
    Integer yasToplam();

    @Query("SELECT new com.example.jpqlexmp.dto.UserNameAndAgeDto(u.name, u.age) FROM User u")
    List<UserNameAndAgeDto> findNameAndAgeFind();

    @Query("SELECT new com.example.jpqlexmp.dto.UserNameAndAgeDto(u.name, u.age) FROM User u WHERE u.name= :name AND u.age= :age")
    List<UserNameAndAgeDto> findNameAndAgeFindParams(@Param("name") String name,@Param("age") Integer age);
}
