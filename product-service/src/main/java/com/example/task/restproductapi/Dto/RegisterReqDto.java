package com.example.task.restproductapi.Dto;

import com.example.task.restproductapi.Enum.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterReqDto {
    private String name;
    private String email;
    private String password;
    private UserRole userRole;

}
