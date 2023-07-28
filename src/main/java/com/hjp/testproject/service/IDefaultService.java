package com.hjp.testproject.service;

import java.util.ArrayList;
import java.util.List;

/**
 * 서비스 기본 인터페이스
 */
public interface IDefaultService<T, S> {

    /**
     * 등록
     * 
     * @param vo
     * @return
     * @throws Exception
     */
    int insert(T vo) throws Exception;

    /**
     * 수정
     * 
     * @param vo
     * @return
     */
    int update(T vo) throws Exception;

    /**
     * 삭제
     * 
     * @param vo
     * @return
     */
    int delete(T vo) throws Exception;

    /**
     * 조회
     * 
     * @param vo
     * @return
     */
    default T getData(T vo) {
        return null;
    }

    /**
     * 목록 카운트
     * 
     * @param search
     * @return
     */
    default int getCount(S search) {
        return 0;
    }

    /**
     * 목록 조회
     * 
     * @param search
     * @return
     */
    default List<T> getList(S search) {
        return new ArrayList<>();
    }

}
