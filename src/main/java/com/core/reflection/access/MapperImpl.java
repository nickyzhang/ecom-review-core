package com.core.reflection.access;

import java.util.List;

public class MapperImpl implements Mapper<Model> {

    @Override
    @Transaction
    public Integer save(Model entity) {
        return null;
    }

    @Override
    public Integer saveBatch(List<Model> entityList) {
        return null;
    }

    @Override
    public Integer delete(Model entity) {
        return null;
    }

    @Override
    public Integer deleteById(Long id) {
        return null;
    }

    @Override
    public Integer deleteAll() {
        return null;
    }

    @Override
    public Integer update(Model entity) {
        return null;
    }

    @Override
    public Integer updateBatchById(List<Model> entityList) {
        return null;
    }

    @Override
    public Model selectOne(Long id) {
        return null;
    }

    @Override
    public List<Model> selectAll() {
        return null;
    }

    @Override
    public Long count() {
        return null;
    }
}
