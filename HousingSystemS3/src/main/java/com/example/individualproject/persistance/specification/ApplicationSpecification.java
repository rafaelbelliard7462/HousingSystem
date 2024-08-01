package com.example.individualproject.persistance.specification;

import com.example.individualproject.domain.enums.Status;
import com.example.individualproject.persistance.entity.ApplicationEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ApplicationSpecification implements Specification<ApplicationEntity> {

    private Long userId;
    private Status status;
    private String searchString;
    private  String property= "property";
    private  String address= "address";

    public ApplicationSpecification(Long userId, Status status, String searchString) {
        this.userId = userId;
        this.status = status;
        this.searchString = searchString;
    }

    @Override
    public Predicate toPredicate(Root<ApplicationEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        if (userId != null) {
            predicates.add(builder.equal(root.get(property).get("user").get("id"), userId));
        }

        if (status != null) {
            predicates.add(builder.equal(root.get("status"), status));
        }

        if (searchString != null) {
            String[] searchItems = searchString.split(" ");
            for (String item : searchItems) {
                predicates.add(builder.or(
                        builder.like(root.get(property).get(address).get("street"), "%" + item + "%"),
                        builder.like(root.get(property).get(address).get("city"), "%" + item + "%"),
                        builder.like(root.get(property).get(address).get("postCode"), "%" + item + "%")
                ));
            }
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }

}

