package com.example.learnJava.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResPageDTO {
    private meta meta;
    private Object data;


    @Getter
    @Setter
    public static class meta {
        private int page;
        private int pageSize;
        private int pages;
        private long total;
    }
}
