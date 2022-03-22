package com.polyroot.coinbot.model.dto;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TestDto {

    private String one;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestDto testDto = (TestDto) o;
        return Objects.equals(one + "*", testDto.one + "*");
    }

    @Override
    public int hashCode() {
        return Objects.hash(one+ "*");
    }
}
