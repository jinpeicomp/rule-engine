package com.jinpei.re.dto;

import com.jinpei.re.core.model.Activity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 营销活动冲突检测结果
 *
 * @author Mingo.Liu
 * @date 2022-10-09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityAnalyzeResponse implements Serializable {
    /**
     * 优先的营销活动，即优先级高于要创建的营销活动，会导致要创建的营销活动失效
     */
    private List<Activity> priorActivities;

    /**
     * 失效的营销活动，即优先级低于要创建的营销活动，这些营销活动会失效
     */
    private List<Activity> brokenActivities;

    private static final ActivityAnalyzeResponse EMPTY = ActivityAnalyzeResponse.builder()
            .brokenActivities(Collections.emptyList())
            .priorActivities(Collections.emptyList())
            .build();

    public static ActivityAnalyzeResponse empty() {
        return EMPTY;
    }
}
