package com.jinpei.re.db.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * 营销活动配置JPA仓库
 *
 * @author Mingo.Liu
 * @date 2022-09-29
 */
@Repository
public interface ActivityRepository extends JpaRepository<ActivityEntity, Long>, JpaSpecificationExecutor<ActivityEntity> {
}
