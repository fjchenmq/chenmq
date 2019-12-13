package com.cmq.stock;

import java.util.List;

/**
 * Created by Administrator on 2019/1/9.
 */
public interface EasyMoneyService {
    public void  syncQHCC(boolean isAll);
    public List<String> sortNetAmount();
}
