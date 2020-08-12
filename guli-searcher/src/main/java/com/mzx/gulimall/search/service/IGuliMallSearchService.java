package com.mzx.gulimall.search.service;

import com.mzx.gulimall.search.vo.SearchParam;
import com.mzx.gulimall.search.vo.SearchResult;

/**
 * @author ZhenXinMa
 * @date 2020/8/12 17:00
 */
public interface IGuliMallSearchService {

    /**
     * 通过参数searchParam来进行动态条件查询.
     *
     * @param searchParam
     * @return
     */
    SearchResult search(SearchParam searchParam);

}
