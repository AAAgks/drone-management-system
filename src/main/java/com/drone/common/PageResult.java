package com.drone.common;

import com.github.pagehelper.PageInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页查询结果封装。
 * <p>
 * 将 PageHelper 的 {@link PageInfo} 转为统一的分页格式返回给前端。
 */
@Data
@NoArgsConstructor
public class PageResult<T> {

    private long total;
    private int pages;
    private int pageNum;
    private int pageSize;
    private List<T> rows;

    public static <T> PageResult<T> of(PageInfo<T> pageInfo) {
        PageResult<T> result = new PageResult<>();
        result.total = pageInfo.getTotal();
        result.pages = pageInfo.getPages();
        result.pageNum = pageInfo.getPageNum();
        result.pageSize = pageInfo.getPageSize();
        result.rows = pageInfo.getList();
        return result;
    }
}
