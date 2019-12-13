package com.cmq.stock.impl;

import com.cmq.entity.Qhcc;
import com.cmq.entity.TotalNetAmount;
import com.cmq.mapper.QhccMapper;
import com.cmq.stock.EasyMoneyService;
import com.cmq.utils.HttpClientUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.util.DateParseException;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static javafx.scene.input.KeyCode.L;

/**
 * Created by Administrator on 2019/1/9.
 */
@Service
public class EasyMoneyServiceImpl implements EasyMoneyService {
    @Autowired
    QhccMapper qhccMapper;

    public String syncQHCC(String code) {
        String relt = "";
        String url =
            "http://datainterface.eastmoney.com/EM_DataCenter/JS.aspx?type=QHCC&sty=JCW&code="
                + code;
        relt = HttpClientUtil.doGet(url);
        //relt="(08]}}])";
        if (!StringUtils.isEmpty(relt) && relt.indexOf(")") != -1) {
            relt = relt.substring(1, relt.indexOf(")"));
            relt = relt.substring(relt.indexOf("[") + 1, relt.lastIndexOf("]"));
        }
        return relt;
    }

    public static Map<String, String> subTypeMap          = new HashMap();
    public static Map<String, String> subTypeMapType      = new HashMap<>();
    public static Map<String, Double> typesLongAmountMap  = new HashMap();
    public static Map<String, Double> typesShortAmountMap = new HashMap();

    {
        subTypeMap.put("if1901", "沪深300股指期货");
        subTypeMap.put("if1902", "沪深300股指期货");
        subTypeMap.put("if1903", "沪深300股指期货");
        subTypeMap.put("if1904", "沪深300股指期货");
        subTypeMap.put("if1905", "沪深300股指期货");
        subTypeMap.put("if1906", "沪深300股指期货");
        subTypeMap.put("ic1901", "中证500股指期货");
        subTypeMap.put("ic1902", "中证500股指期货");
        subTypeMap.put("ic1903", "中证500股指期货");
        subTypeMap.put("ic1904", "中证500股指期货");
        subTypeMap.put("ic1905", "中证500股指期货");
        subTypeMap.put("ic1906", "中证500股指期货");
        subTypeMap.put("ih1901", "上证50股指期货");
        subTypeMap.put("ih1902", "上证50股指期货");
        subTypeMap.put("ih1903", "上证50股指期货");
        subTypeMap.put("ih1904", "上证50股指期货");
        subTypeMap.put("ih1905", "上证50股指期货");
        subTypeMap.put("ih1906", "上证50股指期货");

        String ifName = "if(沪深300股指期货)";
        String icName = "ic(中证500股指期货)";
        String ihName = "ih(上证50股指期货)";
        // todo
        // xxx
        // fixme
        int a = 1;
        subTypeMap.keySet().forEach(type -> {
            if (type.indexOf("if") > -1) {
                subTypeMapType.put(type, ifName);
                typesShortAmountMap.put(ifName, 0D);
            }
            if (type.indexOf("ic") > -1) {
                subTypeMapType.put(type, icName);
                typesShortAmountMap.put(icName, 0D);

            }
            if (type.indexOf("ih") > -1) {
                subTypeMapType.put(type, ihName);
                typesShortAmountMap.put(ihName, 0D);

            }
        });

        typesShortAmountMap.keySet().forEach(type -> {
            typesLongAmountMap.put(type, 0D);
        });

    }

    @Transactional
    public void syncQHCC(boolean isAll) {

        try {
            String date = DateUtil
                .formatDate(DateUtils.addDays(new Date(), 0), "yyyy-MM-dd");//前一天数据
            Map<String, String> param = new HashMap<>();
            param.put("createDate", date);
            List<Qhcc> dataList = qhccMapper.queryList(param);
            if (dataList != null && dataList.size() > 0) {
                return;
            }

            //subTypes.forEach(type->syncQHCC(type));
            Map<String, String> dateMap = new HashMap<>();

            Iterator<String> keys = subTypeMap.keySet().iterator();

            while (keys.hasNext()) {
                String type = keys.next();
                String relt = syncQHCC(type);
                String data = JSONObject.fromObject(relt).getJSONObject("series4")
                    .getString("data");
                JSONArray jsonArray = JSONArray.fromObject(data);
                jsonArray.forEach(arry -> {
                    String[] arrays = arry.toString().split(",");
                    String createDate = arrays[0];
                    if (isAll || createDate.equals(date)) {
                        if (!dateMap.containsKey(createDate)) {
                            dateMap.put(createDate, subTypeMapType.get(type));
                        }
                        saveQHCC(arrays, type);
                    }

                });

            }
            dateMap.keySet().forEach(now -> {
                Map<String, String> queryMap = new HashMap<>();
                queryMap.put("createDate", now);
                queryMap.put("type", dateMap.get(now));
                TotalNetAmount amount = null;
                try {
                    amount = qhccMapper.getOneTotalNetAmount(queryMap);
                } catch (Exception e) {
                    System.out.println(queryMap);
                    e.printStackTrace();
                    throw e;
                }
                if (amount != null) {
                    return;
                }

                TotalNetAmount totalNetAmount = new TotalNetAmount();
                totalNetAmount.setCreateDate(now);
                calTotalNetAmount(totalNetAmount);
                qhccMapper.createTotalNetAmount(totalNetAmount);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveQHCC(String[] arrays, String type) {
        try {
            String createDate = arrays[0];

            Map<String, String> param = new HashMap<>();
            param.put("createDate", createDate);
            param.put("type", type);
            Qhcc cc = qhccMapper.getOne(param);
            if (cc != null) {
                return;
            }

            String price = arrays[1];
            String longAmount = arrays[2];
            String longAmountChange = arrays[3];
            String shortAmount = arrays[4];
            String shortAmountChange = arrays[5];
            String netAmount = arrays[6];
            Qhcc qhcc = new Qhcc();
            qhcc.setCreateDate(DateUtil.parseDate(createDate));
            qhcc.setPrice(price);
            qhcc.setTypeName(subTypeMap.get(type));
            qhcc.setLongAmount(longAmount);
            qhcc.setShortAmount(shortAmount);
            qhcc.setShortAmountChange(shortAmountChange);
            qhcc.setLongAmountChange(longAmountChange);
            qhcc.setNetAmount(netAmount);
            qhcc.setType(type);
            qhccMapper.create(qhcc);
        } catch (DateParseException e) {
            e.printStackTrace();
        }

    }

    public List<String> sortNetAmount() {
        List<String> list = new ArrayList();

        Date now = new Date();
        now = DateUtils.addDays(now, -4);
        String date = DateUtil.formatDate(now, "yyyy-MM-dd");

        //list.add(date + "的总多空净量：" + calTotalNetAmount(date));
        return list;
    }

    public TotalNetAmount calTotalNetAmount(TotalNetAmount totalNetAmount) {

        Double totalLongAmount = 0D;
        Double totalShortAmount = 0D;

        typesLongAmountMap.keySet().forEach(type -> {
            typesLongAmountMap.put(type, 0D);
            typesShortAmountMap.put(type, 0D);
        });

        Map<String, String> param = new HashMap<>();
        param.put("createDate", totalNetAmount.getCreateDate());
        List<Qhcc> dataList = qhccMapper.queryList(param);
        for (Qhcc cc : dataList) {
            Double longAmount = 0D;
            Double shortAmount = 0D;
            longAmount = Double.valueOf(cc.getLongAmount());
            shortAmount = Double.valueOf(cc.getShortAmount());
            totalLongAmount += longAmount;
            totalShortAmount += shortAmount;
            String mapType = subTypeMapType.get(cc.getType());
            typesLongAmountMap.put(mapType, typesLongAmountMap.get(mapType) + longAmount);
            typesShortAmountMap.put(mapType, typesShortAmountMap.get(mapType) + shortAmount);

        }

        Iterator<String> keys = typesLongAmountMap.keySet().iterator();

        while (keys.hasNext()) {
            String key = keys.next();
            TotalNetAmount amount = new TotalNetAmount();
            amount.setCreateDate(totalNetAmount.getCreateDate());
            Double longAmount = typesLongAmountMap.get(key);
            Double shortAmount = typesShortAmountMap.get(key);
            amount.setTotalNetAmount((longAmount - shortAmount) + "");
            amount.setShortAmount(shortAmount + "");
            amount.setLongAmount(longAmount + "");
            amount.setLongShortScale(String.format("%.3f", (longAmount / shortAmount)));
            amount.setType(key);
            qhccMapper.createTotalNetAmount(amount);
        }
        //总的
        Double scale = 0.00;
        totalNetAmount.setTotalNetAmount((totalLongAmount - totalShortAmount) + "");
        totalNetAmount.setShortAmount(totalShortAmount + "");
        totalNetAmount.setLongAmount(totalLongAmount + "");
        scale = totalLongAmount / totalShortAmount;
        totalNetAmount.setLongShortScale(String.format("%.3f", scale));
        totalNetAmount.setType("All");
        return totalNetAmount;
    }

    public static void main(String[] args) {
        new EasyMoneyServiceImpl().syncQHCC(true);
    }
}
