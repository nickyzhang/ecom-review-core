package com.core.reflection.access;

import java.util.List;

@Service(timeout = 10)
@Transaction(readOnly = false,timeout = 10)
public class ServiceImpl implements IService {

    private Mapper<Model> mapper;

    public ServiceImpl() {}

    public ServiceImpl(Mapper<Model> mapper) {
        this.mapper = mapper;
    }

    @Override
    public Boolean save(Object entity) {
        return null;
    }

    @Override
    public Boolean saveBatch(List entityList) {
        return null;
    }

    @Override
    public Boolean delete(Object entity) {
        return null;
    }

    @Override
    public Boolean deleteById(Long id) {
        return null;
    }

    @Override
    public Boolean deleteAll() {
        return null;
    }

    @Override
    public Boolean update(Object entity) {
        return null;
    }

    @Override
    public Boolean updateBatchById(List entityList) {
        return null;
    }

    @Override
    public Object selectOne(Long id) {
        return null;
    }

    @Override
    public List selectAll() {
        return null;
    }

    @Override
    public Long count() {
        return null;
    }
}
