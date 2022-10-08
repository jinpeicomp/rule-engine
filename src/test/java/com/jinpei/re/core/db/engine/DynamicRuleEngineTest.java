package com.jinpei.re.core.db.engine;

import com.jinpei.re.core.model.Activity;
import com.jinpei.re.core.model.UserAttribute;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mingo.Liu
 * @date 2022-10-08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamicRuleEngineTest {

    @Autowired
    private DynamicRuleEngine ruleEngine;

    @Test
    public void execute() {
        UserAttribute userAttribute = UserAttribute.builder()
                .region("巴西")
                .terminalType("website")
                .loginType(1)
                .channelType("2000100_ALL,")
                .build();
        List<Long> activityIds = ruleEngine.execute(userAttribute);
        assertThat(activityIds, not(empty()));

        userAttribute = UserAttribute.builder()
                .region("巴西")
                .terminalType("website")
                .loginType(1)
                .channelType("2000100_ALL")
                .build();
        activityIds = ruleEngine.execute(userAttribute);
        assertThat(activityIds, not(empty()));
    }

    @Test
    public void addRule() {
        Activity activity = Activity.builder()
                .id(99L)
                .name("默认添加规则测试1111")
                .region("巴西")
                .loginType(1)
                .weight(19)
                .build();
        ruleEngine.addOrUpdateRule(activity);

        UserAttribute userAttribute = UserAttribute.builder()
                .region("巴西")
                .terminalType("website")
                .loginType(1)
                .channelType("2000100_ALL")
                .build();
        List<Long> activityIds = ruleEngine.execute(userAttribute);
        assertThat(activityIds, not(empty()));
        assertThat(activityIds, hasSize(4));
    }

    @Test
    public void updateRule() {
        Activity activity = Activity.builder()
                .id(10L)
                .name("默认添加规则测试1111")
                .region("巴西")
                .weight(19)
                .build();
        ruleEngine.addOrUpdateRule(activity);
        UserAttribute userAttribute = UserAttribute.builder()
                .region("巴西")
                .terminalType("website")
                .loginType(1)
                .channelType("2000100_ALL")
                .build();
        List<Long> activityIds = ruleEngine.execute(userAttribute);
        assertThat(activityIds, not(empty()));
        assertThat(activityIds, hasSize(3));
    }

    @Test
    public void deleteRule() {
        ruleEngine.deleteRule("巴西默认");
        UserAttribute userAttribute = UserAttribute.builder()
                .region("巴西")
                .terminalType("website")
                .loginType(1)
                .channelType("2000100_ALL")
                .build();
        List<Long> activityIds = ruleEngine.execute(userAttribute);
        assertThat(activityIds, not(empty()));
        assertThat(activityIds, hasSize(2));
    }
}