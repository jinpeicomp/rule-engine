package com.jinpei.re.db.engine;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 动态规则引擎配置类
 *
 * @author Mingo.Liu
 * @date 2022-09-30
 */
@Configuration
public class DynamicRuleEngineConfiguration {
    @Bean
    public KieFileSystem dynamicKieFileSystem() {
        return KieServices.get().newKieFileSystem();
    }

    @Bean
    public KieContainer dynamicKieContainer(KieFileSystem dynamicKieFileSystem) {
        KieServices kieServices = KieServices.get();
        KieBuilder kieBuilder = kieServices.newKieBuilder(dynamicKieFileSystem);
        kieBuilder.buildAll();
        KieModule kieModule = kieBuilder.getKieModule();
        return kieServices.newKieContainer(kieModule.getReleaseId());
    }
}