package com.app.schoolapp.web.mappers.student;

import com.app.schoolapp.web.repositories.specifications.SearchCriteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@AllArgsConstructor
@Component
public class GetStudentsRequestMapper {
    public List<SearchCriteria> map(String search){
        List<SearchCriteria> criteria = new ArrayList<>();
        if(search != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                criteria.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
            }
        }
        return criteria;
    }
}
