package com.jinpei.re.api;

import com.jinpei.re.core.MarketingService;
import com.jinpei.re.core.model.Activity;
import com.jinpei.re.core.model.UserAttribute;
import com.jinpei.re.dto.ActivityCreateDTO;
import com.jinpei.re.dto.ActivitySearchDTO;
import com.jinpei.re.dto.ActivityUpdateDTO;
import com.jinpei.re.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 营销API接口
 *
 * @author Mingo.Liu
 * @date 2022-10-08
 */
@RestController
@RequestMapping("/api/marketing")
@RequiredArgsConstructor
public class MarketingController {
    @Qualifier("marketingServiceDbImpl")
    private final MarketingService marketingService;

    /**
     * 计算满足用户条件的营销活动
     *
     * @param userAttribute 用户属性
     * @return 满足条件的营销活动列表
     */
    @RequestMapping(path = "/calculate", method = RequestMethod.POST)
    public BaseResponse<List<Activity>> calculate(@RequestBody UserAttribute userAttribute) {
        if (!userAttribute.isValid()) {
            return BaseResponse.error("参数错误");
        }
        List<Activity> activities = marketingService.calculate(userAttribute);
        return BaseResponse.success(activities);
    }

    /**
     * 查询营销活动
     *
     * @param searchDTO 查询请求
     * @return 分页查询结果
     */
    @RequestMapping(method = RequestMethod.GET)
    public BaseResponse<Page<Activity>> query(ActivitySearchDTO searchDTO) {
        Page<Activity> page = marketingService.queryPage(searchDTO);
        return BaseResponse.success(page);
    }

    /**
     * 添加营销活动，并更新规则引擎
     *
     * @param createDTO 营销活动添加请求
     * @return 添加成功的营销活动
     */
    @RequestMapping(method = RequestMethod.POST)
    public BaseResponse<Activity> add(@RequestBody ActivityCreateDTO createDTO) {
        Activity activity = marketingService.add(createDTO);
        return BaseResponse.success(activity);
    }

    /**
     * 删除营销活动，并更新规则引擎
     *
     * @param id 要删除的营销活动ID
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public BaseResponse<Boolean> delete(@PathVariable Long id) {
        marketingService.delete(id);
        return BaseResponse.success(true);
    }

    /**
     * 更新营销活动，并更新规则引擎
     *
     * @param updateDTO 营销活动更新请求
     * @return 更新成功的营销活动
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public BaseResponse<Activity> update(@PathVariable Long id, @RequestBody ActivityUpdateDTO updateDTO) {
        updateDTO.setId(id);
        Activity activity = marketingService.update(updateDTO);
        return BaseResponse.success(activity);
    }
}
