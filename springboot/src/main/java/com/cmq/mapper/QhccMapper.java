package com.cmq.mapper;

import com.base.mapper.common.BaseMapper;
import com.cmq.entity.CustOrder;
import com.cmq.entity.Qhcc;
import com.cmq.entity.TotalNetAmount;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/11/29.
 */
public  interface QhccMapper extends BaseMapper<Qhcc> {
    public Qhcc getOne(Map param);
    public TotalNetAmount getOneTotalNetAmount(Map param);
    public void create(Qhcc qhcc);
    public void createTotalNetAmount(TotalNetAmount totalNetAmount);
    public List<Qhcc> queryList(Map param);
}
