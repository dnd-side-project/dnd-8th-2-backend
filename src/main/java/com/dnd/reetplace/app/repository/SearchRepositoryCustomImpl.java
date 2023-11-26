package com.dnd.reetplace.app.repository;

import javax.persistence.EntityManager;

public class SearchRepositoryCustomImpl implements SearchRepositoryCustom {

    private final EntityManager em;

    public SearchRepositoryCustomImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void deleteLatestSearchByMemberId(Long memberId) {
        String sql = "delete from search s " +
                "where s.search_id in " +
                "(select * from " +
                "(select ss.search_id from search ss " +
                "where ss.member_id = :memberId and ss.deleted_at is null order by ss.created_at limit 1) as ds)";

        em.createNativeQuery(sql)
                .setParameter("memberId", memberId)
                .executeUpdate();
    }
}
