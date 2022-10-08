package com.jinpei.re.core.db.domain;

import com.jinpei.re.core.db.domain.ActivityService;
import com.jinpei.re.core.model.Activity;
import com.jinpei.re.dto.ActivityCreateDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mingo.Liu
 * @date 2022-10-08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityServiceTest {

    @Autowired
    private ActivityService activityService;

    @Test
    public void create() {
        ActivityCreateDTO createDTO = ActivityCreateDTO.builder()
                .name("巴西默认")
                .region("巴西")
                .weight(1)
                .discount(BigDecimal.valueOf(0.95))
                .build();
        Activity activity = activityService.create(createDTO);
        assertThat(activity.getName(), equalTo(createDTO.getName()));
        assertThat(activity.getDiscount(), equalTo(createDTO.getDiscount()));
        assertThat(activity.getChannelTypes(), anyOf(nullValue(), empty()));
        assertThat(activity.getTerminalType(), emptyOrNullString());

        createDTO = ActivityCreateDTO.builder()
                .name("巴西,设备类型:网站,登陆类型:Email")
                .region("巴西")
                .terminalType("website")
                .loginType(1)
                .weight(5)
                .discount(BigDecimal.valueOf(0.94))
                .build();
        activity = activityService.create(createDTO);
        assertThat(activity.getName(), equalTo(createDTO.getName()));
        assertThat(activity.getDiscount(), equalTo(createDTO.getDiscount()));

        createDTO = ActivityCreateDTO.builder()
                .name("巴西,设备类型:box,登陆类型:Account")
                .region("巴西")
                .terminalType("box")
                .loginType(2)
                .weight(5)
                .discount(BigDecimal.valueOf(0.941))
                .build();
        activity = activityService.create(createDTO);
        assertThat(activity.getName(), equalTo(createDTO.getName()));
        assertThat(activity.getDiscount(), equalTo(createDTO.getDiscount()));

        String strChannelTypes = "2000100_ALL,2000300_ALL,2000400_ALL,2000500_ALL,3000800_ALL,3001100_ALL,3001900_ALL," +
                "3002300_ALL,3002800_ALL,3002900_ALL,3003000_ALL,3003200_ALL,3003300_ALL,3003500_ALL,3003600_ALL";
        createDTO = ActivityCreateDTO.builder()
                .name("巴西,设备类型:网站,登陆类型:Email,渠道类型:2000100")
                .region("巴西")
                .terminalType("website")
                .loginType(1)
                .channelTypes(Arrays.asList(strChannelTypes.split(",")))
                .weight(10)
                .discount(BigDecimal.valueOf(0.90))
                .build();
        activity = activityService.create(createDTO);
        assertThat(activity.getName(), equalTo(createDTO.getName()));
        assertThat(activity.getDiscount(), equalTo(createDTO.getDiscount()));
        assertThat(activity.getChannelTypes(), not(empty()));
    }

    @Test
    public void queryAll() {
        List<Activity> activityList = activityService.queryAll();
        assertThat(activityList, hasSize(4));
    }
}