package com.host_go.host_go.Dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImgurResponse {
    private ImgurData data;
    private boolean success;
    private int status;
}
