package com.cmq.service.impl;

import com.base.properties.PropertiesUtils;
import com.cmq.bean.Person;
import com.cmq.entity.Cust;
import com.cmq.entity.CustOrder;
import com.cmq.entity.Users;
import com.cmq.service.CustOrderService;
import com.cmq.service.CustService;
import com.cmq.service.SpiTestService;
import com.cmq.service.TestService;
import com.cmq.service.UsersService;
import com.cmq.utils.BatchUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ServiceLoader;

/**
 * Created by chenmq on 2018/8/10.
 */
@Service
public class TestServiceImpl implements TestService {
    @Autowired
    PropertiesUtils  propertiesUtils;
    @Autowired
    Person           person;
    @Autowired
    UsersService     usersService;
    @Autowired
    CustService      custService;
    @Autowired
    CustOrderService custOrderService;

    @Override
    @Transactional
    public String getName()throws Exception {
        System.out.println(propertiesUtils.getCustContact().getContactName());



        Users users = usersService.getOne(1L);
        Cust cust = custService.getOne(1L);
        CustOrder custOrder = custOrderService.getOne(1L);

        PageInfo<Users> pageInfo = usersService.pageQuery();
        PageInfo<Cust> custPageInfo = custService.pageQuery();
        PageInfo<CustOrder> custOrderPageInfo = custOrderService.pageQuery();


        PageInfo<Cust> custOrderList = custService.queryCustOrderList(null);

        List<Cust> custList= new ArrayList<>();

        for(int i=0;i<2;i++){
            int max = 200;
            int min = 100;
            Random random = new Random();
            Integer seq = random.nextInt(max) % (max - min + 1) + min;
            Cust cust1 = new Cust();
            cust1.setCustId(seq.longValue());
            cust1.setCustName("批量插入1");
            cust1.setCustType("1000");
            cust1.setShardingId(cust1.getCustId() );
            custList.add(cust1);
        }

//         BatchUtil.batchInsert(custList,"custId",Cust.class);


        return "Chen ....";
    }

    @Override
    public String getAge() {
        return "18";
    }

    @Override
    public Person getPerson( Person person) {
        return person;
    }
}
