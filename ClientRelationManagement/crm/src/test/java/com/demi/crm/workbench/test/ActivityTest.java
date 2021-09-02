package com.demi.crm.workbench.test;

import com.demi.crm.utils.ServiceFactory;
import com.demi.crm.utils.UUIDUtil;
import com.demi.crm.workbench.domain.Activity;
import com.demi.crm.workbench.service.ActivityService;
import com.demi.crm.workbench.service.impl.ActivityServiceImpl;
import org.junit.Test;

public class ActivityTest {
    @Test
    public void testSave() {
        Activity a = new Activity();
        a.setId(UUIDUtil.getUUID());
        a.setName("公司宣传活动");
        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean save = service.save(a);
        System.out.println(save);
    }
}
