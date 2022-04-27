package com.app.schoolapp.web.repositories.builders;


import com.app.schoolapp.domain.Student;
import com.app.schoolapp.web.repositories.specifications.SearchCriteria;
import com.app.schoolapp.web.repositories.specifications.StudentSpecification;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class StudentSpecificationBuilder {
    private final List<StudentSpecification> params;

    public StudentSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public StudentSpecificationBuilder with(SearchCriteria criteria) {
        params.add(new StudentSpecification(criteria));
        return this;
    }

    public Specification<Student> build() {
        if (params.isEmpty()) {
            return null;
        }

        List<Specification<Student>> specs = new ArrayList<>(params);

        Specification<Student> result = specs.get(0);

        for (int i = 1; i < specs.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }

        return result;
    }
}
