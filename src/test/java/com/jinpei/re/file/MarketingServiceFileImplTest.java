package com.jinpei.re.file;

import com.jinpei.re.model.Activity;
import com.jinpei.re.model.UserAttribute;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * 文件规则引擎营销接口单元测试
 *
 * @author Mingo.Liu
 * @date 2022-09-29
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MarketingServiceFileImplTest {

    @Qualifier("marketingServiceFileImpl")
    @Autowired
    private MarketingServiceFileImpl marketingService;

    @Test
    public void calculate() {
        UserAttribute userAttribute = UserAttribute.builder()
                .region("巴西")
                .build();
        List<Activity> activities = marketingService.calculate(userAttribute);
        assertThat(activities, hasSize(2));
    }
}