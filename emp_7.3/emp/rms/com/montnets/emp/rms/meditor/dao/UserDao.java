package com.montnets.emp.rms.meditor.dao;

public interface UserDao {
    Integer hasCreatePublicTemp(Long userId);
    String getAuthority(Long userId);
}
