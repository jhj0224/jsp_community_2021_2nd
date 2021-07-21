package com.jhs.exam.exam2.dto;

import lombok.Data;

@Data
public class Board {
    private int id;
    private String regDate;
    private String updateDate;
    private String code;
    private String name;
    private boolean blindStatus;
    private String blindDate;
    private boolean delStatus;
    private String delDate;
}