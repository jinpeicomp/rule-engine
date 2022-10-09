package com.jinpei.re.core.db.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 营销活动配置JPA仓库
 *
 * @author Mingo.Liu
 * @date 2022-09-29
 */
@Repository
public interface ActivityRepository extends JpaRepository<ActivityEntity, Long>, JpaSpecificationExecutor<ActivityEntity> {
    /**
     * 查询地区下所有channel type字段
     *
     * @param region 地区
     * @return channel type列表
     */
    @Query("select t.strChannelTypes from ActivityEntity t where t.region = :region")
    List<String> findChannelTypes(String region);
}
