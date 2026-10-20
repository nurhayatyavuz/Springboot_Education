package com.example.jpqlexmp.controller;


import com.example.jpqlexmp.dto.UserNameAndAgeDto;
import com.example.jpqlexmp.entity.User;
import com.example.jpqlexmp.repository.UserRepository;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserRepository userRepository;
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
//    @GetMapping
//    public ResponseEntity<?> mesajYazdir(){
//        List<User> users = userRepository.tumKullaniciYazdir();
//        return ResponseEntity.ok(users);
//    }
//    @GetMapping
//    public ResponseEntity<?> isimListesi(){
//        List<String> usersList = userRepository.isimListes();
//        return ResponseEntity.ok(usersList);
//    }
//    @PostMapping
//    public ResponseEntity<User> kullaniciEkle(@RequestBody User user) {
//        User kaydedilenUser = userRepository.save(user);
//        return ResponseEntity.ok(kaydedilenUser);
//    }
//    @PostMapping("/yas")
//    public ResponseEntity<Optional<User>> kullaniciYasi(@RequestParam Integer age) {
//        Optional<User> users = userRepository.yas(age);
//        return ResponseEntity.ok(users);
//    }
//
//    @PostMapping
//    public ResponseEntity<?> sayiBul(@RequestParam Integer yas){
//        Integer i = userRepository.sayiBul(yas);
//        return ResponseEntity.ok().body(i);

//    @GetMapping
//    public ResponseEntity<?> yasToplam(){
//    Integer toplam=userRepository.yasToplam();
//    return ResponseEntity.ok().body(toplam);

//    @GetMapping
//    public ResponseEntity<?> NameAndAge(){
//        List<UserNameAndAgeDto> nameAndAgeFind = userRepository.findNameAndAgeFind();
//        return ResponseEntity.ok().body(nameAndAgeFind);
//    }
@GetMapping
public ResponseEntity<?> NameAndAge( @RequestParam String name, @RequestParam Integer age) {
        List<UserNameAndAgeDto> nameAndAgeFind = userRepository.findNameAndAgeFindParams(name, age);
        return ResponseEntity.ok(nameAndAgeFind); }
    }

