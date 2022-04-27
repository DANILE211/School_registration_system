package com.app.schoolapp.web.repositories.builders;


import com.app.schoolapp.domain.Course;
import com.app.schoolapp.web.repositories.specifications.CourseSpecification;
import com.app.schoolapp.web.repositories.specifications.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CourseSpecificationBuilder {
    private final List<CourseSpecification> params;

    public CourseSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public CourseSpecificationBuilder with(SearchCriteria criteria) {
        params.add(new CourseSpecification(criteria));
        return this;
    }

    public Specification<Course> build() {
        if (params.isEmpty()) {
            return null;
        }

        List<Specification<Course>> specs = new ArrayList<>(params);

        Specification<Course> result = specs.get(0);

        for (int i = 1; i < specs.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }

        return result;
    }
}
