package com.demi.crm.workbench.service;

import com.demi.crm.workbench.domain.Clue;
import com.demi.crm.workbench.domain.Tran;

public interface ClueService {
    // 存储添加线索相关信息
    boolean save(Clue clue);

    Clue detail(String id);

    boolean unBund(String id);

    boolean bund(String cid, String[] aids);

    boolean convert(String clueId, Tran t, String createBy);
}
