package com.example.specificationexmp.controller;

import com.example.specificationexmp.dto.UserDto;
import com.example.specificationexmp.entity.User;
import com.example.specificationexmp.filter.UserFilter;
import com.example.specificationexmp.mapper.UserMapper;
import com.example.specificationexmp.repository.UserRepository;
import com.example.specificationexmp.specs.UserSpecification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.config.RootResourceInformationHandlerMethodArgumentResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/userss")
public class UserController {

    private final RootResourceInformationHandlerMethodArgumentResolver rootResourceInformationHandlerMethodArgumentResolver;

    public UserController(UserRepository userRepository, UserMapper userMapper, RootResourceInformationHandlerMethodArgumentResolver rootResourceInformationHandlerMethodArgumentResolver) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.rootResourceInformationHandlerMethodArgumentResolver = rootResourceInformationHandlerMethodArgumentResolver;
    }

    public final UserRepository userRepository;
    public final UserMapper userMapper;

    @PostMapping("/bulk")
    public List<User> createAll(@RequestBody List<User> users) {
        return userRepository.saveAll(users);
    }

    @GetMapping("/spec")
    public ResponseEntity<List<User>> specUser(
            @RequestParam (defaultValue = "",required = false)String name,
            @RequestParam (defaultValue = "",required = false)String surname,
            @RequestParam (defaultValue = "-1",required = false)int age,
            @RequestParam (defaultValue = "",required = false)String city
    ) {

        Specification<User> spec = (Root<User> root,
                                    CriteriaQuery<?> query,
                                    CriteriaBuilder cb) -> cb.conjunction();


        if (name != null) {
            spec = spec.and(UserSpecification.hasName(name));
        }
        if (surname != null) {
            spec = spec.and(UserSpecification.hasSurname(surname));
        }
        if (age != 0) {
            spec = spec.and(UserSpecification.hasAge(age));
        }
        if (city != null) {
            spec = spec.and(UserSpecification.hasCity(city));
        }

        List<User> all = userRepository.findAll(spec);

        return ResponseEntity.ok(all);
    }

    @PostMapping("/filter")
    public ResponseEntity<List<User>> getFilter(@Valid @RequestBody UserFilter userFilterDto) {
        Specification<User> specification = UserSpecification.userFilter(userFilterDto);
        List<User> all = userRepository.findAll(specification);
        return ResponseEntity.ok().body(all);

    }

    @GetMapping("/filter-spec")
    public ResponseEntity<Page<UserDto>> getFilterSpecs() {

        // İlk sayfada 3 kullanıcı getir
        Pageable pageable = PageRequest.of(0, 3);

        // Yaşı 18 ile 30 arasında olan kullanıcıları filtrele
        Specification<User> spec = UserSpecification.aralik(18, 30);

        // Filtreyi veritabanında uygula ve sayfalı sonuçları getir
        Page<User> page = userRepository.findAll(spec, pageable);

        // Entity listesini al
        List<User> content = page.getContent();

        // Entity listesini DTO listesine dönüştür
        List<UserDto> userDtoList = userMapper.toDtoList(content);

        // DTO listesini sayfalı yapıya dönüştür
        Page<UserDto> userDtos = new PageImpl<>(
                userDtoList,
                pageable,
                page.getTotalElements()
        );

        // HTTP 200 ile sonucu döndür
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/filter-spec-sort")
    public ResponseEntity<Page<User>> getFilterSpecsSort(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String surname,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection


    ) {
        Specification<User> specification = (root, query1, criteriaBuilder) -> criteriaBuilder.conjunction();
        if (city != null && !city.isEmpty()) {
            specification = specification.and(UserSpecification.hasName(city));
        }
        if (surname != null && !surname.isEmpty()) {
            specification = specification.and(UserSpecification.hasSurname(surname));
        }
        Sort sort = sortDirection.equalsIgnoreCase("desc") ?
                Sort.by(sortField).descending() :
                Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> all = userRepository.findAll(specification, pageable);
        return ResponseEntity.ok(all);
        //verilerin görünmesi için content yapmamız gerekir.
//        // Sayfadaki kullanıcıları al
//        List<User> content = page.getContent();
//
//        // User listesini DTO listesine dönüştür
//        List<UserDto> userDtoList = userMapper.toDtoList(content);
//
//        // DTO listesini sayfalı yapıya dönüştür
//        Page<UserDto> result = new PageImpl<>(
//                userDtoList,
//                pageable,
//                page.getTotalElements()
//        );
//
//        return ResponseEntity.ok(result);
//    }

    }
    @GetMapping("/pagination-jpql-test1")
    public ResponseEntity<?> getUserByName(@RequestParam String name){
        Pageable pageable=PageRequest.of(0,2,Sort.by("id").ascending());
        Page<User> page = userRepository.allUserForName(name,pageable);
        return ResponseEntity.ok(page);
    }
    @GetMapping("/pagination-jpql-test2")
    public ResponseEntity<?> getAllCars(){
        Pageable pageable=PageRequest.of(0,2,Sort.by("id").ascending());
        Page<User> page = userRepository.allUser(pageable);
        return ResponseEntity.ok(page);
    }
}
