package com.demi.crm.workbench.service.impl;

import com.demi.crm.utils.DateTimeUtil;
import com.demi.crm.utils.SqlSessionUtil;
import com.demi.crm.utils.UUIDUtil;
import com.demi.crm.workbench.dao.*;
import com.demi.crm.workbench.domain.*;
import com.demi.crm.workbench.service.ClueService;
import org.apache.ibatis.session.SqlSessionFactory;

public class ClueServiceImpl implements ClueService {
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao card = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    // 客户相关
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);
    // 联系人相关
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    // 交易相关
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    @Override
    public boolean save(Clue clue) {
        boolean flag = true;
        int count = clueDao.save(clue);
        if (count != 1)
            flag = false;
        return flag;
    }

    @Override
    public Clue detail(String id) {
        Clue clue = clueDao.detail(id);
        return clue;
    }

    @Override
    public boolean unBund(String id) {
        boolean flag = true;
        int count = card.unBund(id);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean bund(String cid, String[] aids) {
        boolean flag = true;
        for (String aid : aids) {
            // 将cid和每一个aid做关联
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(cid);
            car.setActivityId(aid);
            int count = card.bund(car);
            if (count != 1)
                flag = false;
        }
        return flag;
    }

    /**
     * 线索转换操作：传入需要转换的线索id，转换的交易对象，以及创建此次交易的人
     * @return
     */
    @Override
    public boolean convert(String clueId, Tran t, String createBy) {
        // 线索的创建时间
        String createTime = DateTimeUtil.getSysTime();
        boolean flag = true;
        
        // 1. 通过线索id获取线索对象（线索对象当中封装了线索的信息）
        Clue clue = clueDao.getById(clueId);
        // 2. 通过线索对象提取客户信息，当该客户不存在时，新建客户（根据公司的名称精确匹配，判断该客户是否存在）
        String company = clue.getCompany();
        Customer customer = customerDao.getCustomerByName(company);
        // 2.1 如果customer为空，需要新建客户
        if (customer == null) {
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(clue.getAddress());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setOwner(clue.getOwner());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setName(company);
            customer.setDescription(clue.getDescription());
            customer.setCreateTime(createTime);
            customer.setCreateBy(createBy);
            customer.setContactSummary(clue.getContactSummary());

            // 2.2 将新建的客户添加到客户表中
            int count1 = customerDao.save(customer);
            if (count1 != 1) {
                flag = false;
            }
        }

        /*
        * 经过第2步处理，客户的信息已经拥有了，将来在处理其他表的时候，如果要使用到客户id，直接使用customer.getId()即可。
        * */

        // 3. 通过线索对象提取联系人信息，并且保存联系人
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setOwner(clue.getOwner());
        contacts.setSource(clue.getSource());
        contacts.setCustomerId(customer.getId());
        contacts.setFullname(clue.getFullname());
        contacts.setAppellation(clue.getAppellation());
        contacts.setEmail(clue.getEmail());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(createTime);
        contacts.setDescription(clue.getDescription());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setAddress(clue.getAddress());

        int count2 = contactsDao.save(contacts);
        if (count2 != 1) {
            flag = false;
        }

        /*
        * 经过第3步处理，联系人的信息已经拥有了，将来在处理其他表的时候，如果要使用到联系人id，直接使用contacts.getId()即可。
        * */

        return flag;
    }
}
