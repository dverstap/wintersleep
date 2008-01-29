package org.wintersleep.usermgmt.model.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wintersleep.usermgmt.model.Role;
import org.wintersleep.usermgmt.model.RoleRepository;
import org.wintersleep.usermgmt.model.UserManagementService;

@Service
@Transactional
public class UserManagementServiceImpl implements UserManagementService {

    @Autowired
    private RoleRepository roleRepository;

    public Role findRole(Long id) {
        return roleRepository.findById(id);
    }

}