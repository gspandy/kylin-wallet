package com.test.dubbo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.Maps;
import com.rkylin.order.service.OrderInfoBaseService;
import com.rongcapital.wallet.api.po.UserInfoPo;
import com.rongcapital.wallet.api.service.UserInfoApiService;
import com.rongcapital.wallet.constants.AccountConstants;
import com.rongcapital.wallet.constants.ErrorCodeEnum;
import com.rongcapital.wallet.constants.OrderCodeEnum;
import com.rongcapital.wallet.constants.UserPresetConstants;
import com.rongcapital.wallet.dubbo.vo.FinanaceAccountVo;
import com.rongcapital.wallet.util.BeanMapper;
import com.rongcapital.wallet.util.json.JsonUtil;
import com.rongcapital.wallet.util.validate.ValidateUtils;
import com.rongcapital.wallet.vo.OrderInfoMap;
import com.rongcapital.wallet.vo.OrderInfoVo;
import com.rongcapital.wallet.vo.UserInfoVo;
import com.test.base.TestBase;

public class OrderDubbo extends TestBase {

    @Autowired
    private OrderInfoBaseService orderInfoBaseService;
    @Autowired
    UserInfoApiService userInfoApiService;

    @Test
    public void searchTest() {

        Date nowDate = new Date(); // 实例化日历类
        Calendar cal = Calendar.getInstance(); // 设置日期
        cal.setTime(nowDate); // 取3个月前的 日期
        cal.add(Calendar.MONTH, -3);
        Date threeMonthAgoDate = cal.getTime(); // 以下时Date类型转换String类型
        String format = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String orderDate = dateFormat.format(threeMonthAgoDate);

        String userId = "201609021546470001"; // 接入机构中设置的用户ID
        String orderno = "";
        Map<String, String> map = Maps.newHashMap();

        map.put("userId", userId);// 发生用户id
        map.put("rootInstCd", UserPresetConstants.ORG_ID);// 发生用户机构
        map.put("productId", UserPresetConstants.PRO_ID);// 发生用户产品号
        // map.put("amount", );//发生金额
        // map.put("userRelateId", );//交易相关方
        // map.put("orderTime", DateUtil.getDateTime());// 交易请求时间
        map.put("orderDate", orderDate);// 交易账期
        if (!ValidateUtils.isEmpty(orderno)) {
            map.put("userOrderId", orderno);// 第三方商户流水
        }
        // map.put("statusId", );//状态
        map.put("maxresult", "5");// 每页显示数量
        map.put("currentpage", "1");// 当前页数
        map.put("checkCode", "0");// 当前页数

        try {
            List<Map<String, Object>> loi = orderInfoBaseService.queryAllForRongShu(map);
            List<OrderInfoVo> change = change(loi, userId);
            List<LinkedHashMap<String, Object>> list = converList(change);
            System.out.println(JsonUtil.bean2JsonStr(list));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static List<LinkedHashMap<String, Object>> converList(List<OrderInfoVo> loi) {

        List<LinkedHashMap<String, Object>> list = new ArrayList<>();

        Map<String, List<OrderInfoVo>> map2 = aa(loi);

        for (String key : map2.keySet()) {

            LinkedHashMap<String, Object> mapaa = new LinkedHashMap<>();
            mapaa.put("date", key);
            mapaa.put("list", map2.get(key));
            list.add(mapaa);
        }

        return list;

    }

    private static LinkedHashMap<String, List<OrderInfoVo>> aa(List<OrderInfoVo> loi) {
        LinkedHashMap<String, List<OrderInfoVo>> map = new LinkedHashMap<>();
        for (OrderInfoVo o : loi) {
            String date = getDateTime("yyyy-MM", o.getOrderDate());
            if (map.containsKey(date)) {
                map.get(date).add(o);
            } else {
                List<OrderInfoVo> list = new ArrayList<>();
                list.add(o);
                map.put(date, list);
            }

        }
        return map;
    }

    private static final String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df;
        String returnValue = "";
        if (aDate == null) {         
        } else {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }
        return returnValue;
    }

    private final List<OrderInfoVo> change(List<Map<String, Object>> list, String userId) throws Exception {
        List<OrderInfoVo> olist = new ArrayList<>();
        for (Map<String, Object> map : list) {
            OrderInfoVo orderInfo = BeanMapper.map(BeanMapper.map(map, OrderInfoMap.class), OrderInfoVo.class);
            String orderTypeId = orderInfo.getOrderTypeId();
            if ("3001".equals(orderTypeId)) {
                try {
                    UserInfoPo userInfoById = null;
                    if (userId.equals(orderInfo.getUserId())) {

                        if (StringUtils.isNotEmpty(orderInfo.getUserRelateId())) {

                            userInfoById = userInfoApiService.getUserInfoByUserId(orderInfo.getUserRelateId());
                        } else {

                        }
                        orderInfo.setType("1");
                    } else {
                        if (StringUtils.isNotEmpty(orderInfo.getUserId())) {
                            userInfoById = userInfoApiService.getUserInfoByUserId(orderInfo.getUserId());
                        }
                        orderInfo.setType("2");
                    }
                    if (userInfoById != null) {
                        orderInfo.setUserRelateId(userInfoById.getUserRealName());
                    } else {
                        orderInfo.setUserRelateId("");
                        System.out.println("方法：userInfoApiService.getUserInfoByUserId,userId:"
                                + orderInfo.getUserRelateId() + "没查询到用户信息");
                    }
                    orderInfo.setGoodsDetail("余额付款");
                    orderInfo.setOrderTypeId(OrderCodeEnum.MAP.get("3001"));

                } catch (Exception e) {
                    System.out.println("取得用户真实姓名错误" + e);
                }
            } else if (orderTypeId != null && orderTypeId.length() > 1
                    && (orderTypeId.startsWith("B1") || orderTypeId.startsWith("BX"))) {

                orderInfo.setGoodsDetail(orderInfo.getBankHeadName() + "("
                        + orderInfo.getAccountNumber().substring(orderInfo.getAccountNumber().length() - 4) + ")");
                orderInfo.setOrderTypeId(OrderCodeEnum.MAP.get("B1"));
            } else if (orderTypeId != null && orderTypeId.length() > 1 && orderTypeId.startsWith("B2")) {
                orderInfo.setOrderTypeId(OrderCodeEnum.MAP.get("B2"));
                orderInfo.setRemark("余额提现");
                orderInfo.setGoodsDetail(orderInfo.getBankHeadName() + "("
                        + orderInfo.getAccountNumber().substring(orderInfo.getAccountNumber().length() - 4) + ")");
            } else {
                orderInfo.setUserRelateId("");
                orderInfo.setGoodsDetail("余额");
            }

            orderInfo.setStatusId(OrderCodeEnum.MAP.get(orderInfo.getStatusId()));
            orderInfo.setGoodsName(OrderCodeEnum.PAY_TYPE_MAP.get(orderInfo.getGoodsName()));

            orderInfo.setBankHeadName(null);
            orderInfo.setAccountNumber(null);
            olist.add(orderInfo);
        }
        return olist;
    }
}
