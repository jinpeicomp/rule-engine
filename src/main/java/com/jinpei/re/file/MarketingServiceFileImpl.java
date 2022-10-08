package com.jinpei.re.file;

import com.jinpei.re.common.MarketingService;
import com.jinpei.re.model.Activity;
import com.jinpei.re.model.UserAttribute;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 通过文件规则引擎实现营销服务
 *
 * @author Mingo.Liu
 * @date 2022-09-28
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MarketingServiceFileImpl implements MarketingService {

    @Qualifier("fileKieContainer")
    private final KieContainer fileKieContainer;

    /**
     * 计算满足用户条件的营销活动
     *
     * @param userAttribute 用户属性
     * @return 满足条件的营销活动列表
     */
    @Override
    public List<Activity> calculate(UserAttribute userAttribute) {
        StatelessKieSession kieSession = fileKieContainer.newStatelessKieSession();
        List<Activity> list = new ArrayList<>();
        kieSession.setGlobal("list", list);
        kieSession.execute(userAttribute);
        return list;
    }
}
