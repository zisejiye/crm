package com.bipowernode.crm.settings.dao;

import com.bipowernode.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getListByCode(String code);
}
