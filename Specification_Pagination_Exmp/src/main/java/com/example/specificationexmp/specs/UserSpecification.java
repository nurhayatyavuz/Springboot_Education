package com.example.specificationexmp.specs;

import com.example.specificationexmp.entity.User;
import com.example.specificationexmp.filter.UserFilter;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> hasName(String name) { //neye göre arama yapılacaksa o yazılır
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("name"),name);
    }
    public static Specification<User> hasSurname(String surname) { //neye göre arama yapılacaksa o yazılır
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("surname"),surname);
    }
    public static Specification<User> hasAge(Integer age) { //neye göre arama yapılacaksa o yazılır
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("age"),age);
    }
    public static Specification<User> hasCity(String city) { //neye göre arama yapılacaksa o yazılır
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("city"),city);
    }
    public static Specification<User> userFilter(UserFilter dto){
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("name"),dto.getName()),
                    criteriaBuilder.equal(root.get("surname"),dto.getSurname()),
                    criteriaBuilder.equal(root.get("age"),dto.getAge()),
                    criteriaBuilder.equal(root.get("city"),dto.getCity()),
                    criteriaBuilder.equal(root.get("active"),dto.getActive())

            );
    }

    public static Specification<User> aralik(Integer min, Integer max) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("age"), min, max);
    }

}
