package com.demi.crm.vo;

import java.util.List;

/**vo类：
 *  object值对象。通常用于业务层之间的数据传递，和PO一样也是仅仅包含数据而已。
 * @param <T>
 */
public class PaginationVO<T> {
    private int total;
    private List<T> dataList;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
