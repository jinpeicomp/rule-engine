package com.jinpei.re.db.engine;

import com.jinpei.re.model.Activity;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 动态规则
 *
 * @author Mingo.Liu
 * @date 2022-09-30
 */
@Data
@Slf4j
public class DynamicRule {
    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件内容
     */
    private String content;

    private static String RULE_TEMPLATE = null;

    static {
        try (InputStream is = DynamicRule.class.getResourceAsStream("/dynamic/template.drl");
             BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(is), StandardCharsets.UTF_8))) {
            RULE_TEMPLATE = br
                    .lines()
                    .collect(Collectors.joining("\n"));
        } catch (Exception e) {
            log.error("Cannot load rule template with name", e);
        }
    }

    /**
     * 构造动态规则配置
     *
     * @param activity 营销活动
     * @return 动态营销规则
     */
    public static DynamicRule build(Activity activity) {
        if (StringUtils.isNotBlank(RULE_TEMPLATE)) {
            return null;
        }

        String fileName = String.format("src/main/resources/rules/%d.drl", activity.getId());
        DynamicRule dynamicRule = new DynamicRule();
        dynamicRule.setFileName(fileName);

        String lhs = buildLhs(activity);
        Map<String, Object> variables = new HashMap<>(4);
        variables.put("id", activity.getId());
        variables.put("name", activity.getName());
        variables.put("weight", activity.getWeight());
        variables.put("lhs", lhs);

        StringSubstitutor st = new StringSubstitutor(variables);
        String fileContent = st.replace(RULE_TEMPLATE);
        dynamicRule.setContent(fileContent);

        return dynamicRule;
    }

    /**
     * 构造规则请求LHS部分
     *
     * @param activity 营销活动
     * @return LHS字符串
     */
    private static String buildLhs(Activity activity) {
        List<String> conditions = new ArrayList<>();
        if (StringUtils.isNotBlank(activity.getRegion())) {
            conditions.add("region == \"${region}\"");
        }
        if (StringUtils.isNotBlank(activity.getTerminalType())) {
            conditions.add("terminalType == \"${terminalType}\"");
        }
        if (null != activity.getLoginType()) {
            conditions.add("loginType == \"${loginType}\"");
        }
        if (CollectionUtils.isNotEmpty(activity.getChannelTypes())) {
            String strValues = "(\"" + String.join(",", activity.getChannelTypes()) + "\")";
            conditions.add("channelType in " + strValues);
        }
        return String.join(", ", conditions);
    }
}
