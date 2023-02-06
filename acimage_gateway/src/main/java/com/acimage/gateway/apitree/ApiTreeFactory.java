package com.acimage.gateway.apitree;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Data
public class ApiTreeFactory {
    private ApiTree prefixMatchApiTree;
    private ApiTree exactMatchApiTree;
    private ApiTree apiTree;
}
