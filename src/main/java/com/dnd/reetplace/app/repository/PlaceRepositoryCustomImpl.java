package com.dnd.reetplace.app.repository;

import com.dnd.reetplace.app.dto.place.response.KakaoPlaceGetResponse;
import org.qlrm.mapper.JpaResultMapper;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class PlaceRepositoryCustomImpl implements PlaceRepositoryCustom {

    private final EntityManager em;
    private final JpaResultMapper mapper;

    public static final String DISTANCE_LOGIC =
            "(6371*acos(cos(radians(:center_lat))*" +
                    "cos(radians(p.lat))*" +
                    "cos(radians(p.lng)-radians(:center_lng))+" +
                    "sin(radians(:center_lat))*" +
                    "sin(radians(p.lat))))";

    public PlaceRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.mapper = new JpaResultMapper();
    }

    @Override
    public List<KakaoPlaceGetResponse> getReetPlacePopularPlaceList(String lat, String lng) {
        String sql = "select " +
                "p.lot_number_address, " +
                "p.category_group_code, " +
                "p.kakao_category_name, " +
                DISTANCE_LOGIC + " as distance, " +
                "p.kakao_pid, " +
                "p.phone, " +
                "p.name, " +
                "p.url, " +
                "p.road_address, " +
                "p.sido, " +
                "p.sgg, " +
                "p.lng, " +
                "p.lat, " +
                "p.category, " +
                "p.sub_category, " +
                "count(b.bookmark_id) as count " +
                "from place p " +
                "join bookmark b on b.place_id = p.place_id " +
                "group by p.place_id " +
                "having distance <= 1.0 " +
                "order by count desc " +
                "limit 20";

        Query query = em.createNativeQuery(sql)
                .setParameter("center_lat", lat)
                .setParameter("center_lng", lng);

        return mapper.list(query, KakaoPlaceGetResponse.class);
    }
}
