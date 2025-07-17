package com.task5.springTask5.models;

import com.task5.springTask5.dto.UserDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StandardResponse {
    UserDto userDto;
    String token;
}
