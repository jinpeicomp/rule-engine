package com.jinpei.re.db.engine;

import com.jinpei.re.db.domain.ActivityService;
import com.jinpei.re.model.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.drools.compiler.kie.builder.impl.KieContainerImpl;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 规则可以动态更新的规则引擎
 *
 * @author Mingo.Liu
 * @date 2022-09-30
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DynamicRuleEngine {
    private final ActivityService activityService;

    @Qualifier("dynamicKieFileSystem")
    private final KieFileSystem dynamicKieFileSystem;

    @Qualifier("dynamicKieContainer")
    private final KieContainer dynamicKieContainer;

    @PostConstruct
    public void init() {
        log.info("=========> Start init dynamic rule engine......");
        List<Activity> activityList = activityService.queryAll();
        if (CollectionUtils.isEmpty(activityList)) {
            log.info("Cannot find any activity, the init operation will be ignored");
        }

        activityList.forEach(activity -> {
            DynamicRule dynamicRule = DynamicRule.build(activity);
            if (null != dynamicRule) {
                log.info("Dynamic rule is {}", dynamicRule);
                dynamicKieFileSystem.write(dynamicRule.getFileName(), dynamicRule.getContent());
            } else {
                log.error("Cannot build dynamic rule on activity {}", activity);
            }
        });

        KieBuilder kieBuilder = KieServices.get().newKieBuilder(dynamicKieFileSystem);
        kieBuilder.buildAll();
        ((KieContainerImpl) dynamicKieContainer).updateToKieModule((InternalKieModule) kieBuilder.getKieModule());
        log.info("==========> Finish init dynamic rule engine.......");
    }

    /**
     * 添加或更新规则
     * @param activity 营销活动
     */
    public void addOrUpdateRule(Activity activity) {
        DynamicRule dynamicRule = DynamicRule.build(activity);
        if (null == dynamicRule) {
            log.error("Cannot build dynamic rule on activity {}", activity);
        } else {
            log.info("Add or update dynamic rule {}", dynamicRule);
            dynamicKieFileSystem.write(dynamicRule.getFileName(), dynamicRule.getContent());
            KieBuilder kieBuilder = KieServices.get().newKieBuilder(dynamicKieFileSystem);
            kieBuilder.buildAll();
            ((KieContainerImpl) dynamicKieContainer).updateToKieModule((InternalKieModule) kieBuilder.getKieModule());
        }
    }

    /**
     * 删除规则
     * @param activityName 营销活动名称
     */
    public void deleteRule(String activityName) {
        log.info("Start to delete rule by name {}", activityName);
        KieBase kieBase = dynamicKieContainer.getKieBase();
        kieBase.removeRule("com.jinpei.re.dynamic", activityName);
    }
}
