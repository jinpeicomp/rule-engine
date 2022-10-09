package com.jinpei.re.core.file;

import com.jinpei.re.core.MarketingService;
import com.jinpei.re.core.model.Activity;
import com.jinpei.re.core.model.UserAttribute;
import com.jinpei.re.dto.ActivityAnalyzeResponse;
import com.jinpei.re.dto.ActivityCreateDTO;
import com.jinpei.re.dto.ActivitySearchDTO;
import com.jinpei.re.dto.ActivityUpdateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
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

    /**
     * 添加营销活动，并更新规则引擎
     *
     * @param createDTO 营销活动添加请求
     * @return 添加成功的营销活动
     */
    @Override
    public Activity add(ActivityCreateDTO createDTO) {
        throw new UnsupportedOperationException("不支持此操作");
    }

    /**
     * 删除营销活动，并更新规则引擎
     *
     * @param id 要删除的营销活动ID
     */
    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("不支持此操作");
    }

    /**
     * 更新营销活动，并更新规则引擎
     *
     * @param updateDTO 营销活动更新请求
     * @return 更新成功的营销活动
     */
    @Override
    public Activity update(ActivityUpdateDTO updateDTO) {
        throw new UnsupportedOperationException("不支持此操作");
    }

    /**
     * 查询营销活动
     *
     * @param searchDTO 查询请求
     * @return 分页查询结果
     */
    @Override
    public Page<Activity> queryPage(ActivitySearchDTO searchDTO) {
        throw new UnsupportedOperationException("不支持此操作");
    }

    /**
     * 分析待创建的营销活动和已存在的营销活动是否冲突
     *
     * @param createDTO 待创建的营销活动
     * @return 冲突分析结果
     */
    @Override
    public ActivityAnalyzeResponse analyze(ActivityCreateDTO createDTO) {
        throw new UnsupportedOperationException("不支持此操作");
    }
}
