package de.lathspell.test;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class InitViewScope extends CustomScopeConfigurer {

    public InitViewScope() {
        log.info("Init ViewScope");
        Map<String, Object> map = new HashMap<>();
        map.put("view", new com.github.javaplugs.jsf.ViewScope());
        map.put("liferayView", new com.liferay.faces.demos.spring.ViewScope());
        super.setScopes(map);
    }

}
