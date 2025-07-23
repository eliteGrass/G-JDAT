package com.itsoku.lesson066.controller;

import com.itsoku.lesson066.dto.NearbyUserDto;
import com.itsoku.lesson066.dto.AddUserLocationReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.domain.geo.Metrics;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/7/18 20:23 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
public class UserLocationController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 批量将用户地理位置信息添加到redis中（实际工作中，大家可以提供一个用户地理位置上报的接口，客户端可以每隔10秒，上报一下地理位置坐标，将其丢到redis中）
     *
     * @param userLocationReqList
     * @return
     */
    @PostMapping("/addUserLocation")
    public boolean addUserLocation(@RequestBody List<AddUserLocationReq> userLocationReqList) {
        String key = "users:locations";
        for (AddUserLocationReq userLocationReq : userLocationReqList) {
            String userId = userLocationReq.getUserId();
            Double longitude = userLocationReq.getLongitude();
            Double latitude = userLocationReq.getLatitude();
            this.stringRedisTemplate.opsForGeo().add(key, new Point(longitude, latitude), userId);
        }
        return true;
    }


    /**
     * 获取附近的人列表，以（longitude,latitude）为圆心，以 radius 为半径，获取count个用户
     *
     * @param longitude 进度
     * @param latitude  纬度
     * @param radius    圆的半径（米）
     * @param count     获取用户的数量
     * @return
     */
    @GetMapping("/findNearbyUserList")
    public List<NearbyUserDto> findNearbyUserList(@RequestParam("longitude") double longitude,
                                                 @RequestParam("latitude") double latitude,
                                                 @RequestParam("radius") double radius,
                                                 @RequestParam("count") int count) {
        List<NearbyUserDto> nearbyUserDtoList = new ArrayList<>();

        //从redis中获取附近的用户列表
        String key = "users:locations";
        Circle circle = new Circle(new Point(longitude, latitude), new Distance(radius, Metrics.METERS));
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs
                .newGeoRadiusArgs()
                .includeCoordinates()
                .includeDistance()
                .sortAscending().limit(count);
        GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults = stringRedisTemplate.opsForGeo().radius(key, circle, args);
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> content = geoResults.getContent();
        for (GeoResult<RedisGeoCommands.GeoLocation<String>> geoResultGeoResult : content) {
            RedisGeoCommands.GeoLocation<String> geoLocation = geoResultGeoResult.getContent();
            Point point = geoLocation.getPoint();
            String userId = geoLocation.getName();

            //拿到用于的id、经纬度、距离
            NearbyUserDto nearbyUserDto = new NearbyUserDto();
            nearbyUserDto.setUserId(userId);
            nearbyUserDto.setLongitude(point.getX());
            nearbyUserDto.setLatitude(point.getY());
            nearbyUserDto.setDistance(geoResultGeoResult.getDistance().getValue());
            nearbyUserDtoList.add(nearbyUserDto);
        }
        return nearbyUserDtoList;
    }
}
