package com.huajie.core.subject;


import lombok.Data;

import java.util.List;

@Data
public class Subject {

    private String keyId;

    private List<String> permissions;

}
