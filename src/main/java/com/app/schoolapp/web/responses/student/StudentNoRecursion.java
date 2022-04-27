package com.app.schoolapp.web.responses.student;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StudentNoRecursion {
    private Integer id;
    private String name;
    private String surname;
}
