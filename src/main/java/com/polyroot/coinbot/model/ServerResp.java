package com.polyroot.coinbot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ServerResp {

    SAVE_CJ_OK("success","CJ config stored successfully"),
    IMPL_CJ_OK("success", "CJ started implementation successfully"),

    SAVE_EXP_OK("success", "Experiment config stored successfully"),
    IMPL_EXP_OK("success", "Experiment config started implementation successfully"),

    TEMPLATE_SAVE_OK("success","Templates stored successfully");

    private String statusCode;
    private String statusDesc;

}
