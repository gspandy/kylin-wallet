package com.rongcapital.wallet.service.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rongcapital.wallet.api.po.UserLoginHistoryPo;
import com.rongcapital.wallet.api.po.UserLoginPo;
import com.rongcapital.wallet.api.service.LoginAipService;
import com.rongcapital.wallet.api.vo.PersonInfoVo;
import com.rongcapital.wallet.dao.PersonInfoMapper;
import com.rongcapital.wallet.dao.UserLoginHistoryMapper;
import com.rongcapital.wallet.dao.UserLoginMapper;
import com.rongcapital.wallet.po.PersonInfo;
import com.rongcapital.wallet.po.UserLogin;
import com.rongcapital.wallet.po.UserLoginHistory;
import com.rongcapital.wallet.util.validate.ValidateUtils;

@Service("loginAipService")
public class LoginApiServiceImpl implements LoginAipService {

    @Autowired
    private UserLoginHistoryMapper userLoginHistoryMapper;

    @Autowired
    private UserLoginMapper userLoginMapper;

    @Autowired
    private PersonInfoMapper personInfoMapper;

    public void insertLoginHistory(UserLoginHistoryPo po) {
        UserLoginHistory h = new UserLoginHistory();
        h.setLoginIp(po.getLoginIp());
        h.setLongSource(po.getLongSource());
        h.setLoginType(po.getLoginType());
        h.setUserInfoId(po.getUserInfoId());
        userLoginHistoryMapper.insertSelective(h);
    }

    @Override
    public boolean validatePwd(String pwd) {

        return false;
    }

    @Override
    public boolean updatePwd(String pwd) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public UserLoginPo getLoginInfoByName(String userName) {
        UserLoginPo po = null;
        UserLogin userLogin = userLoginMapper.selectByUserName(userName);
        if (null != userLogin) {
            po = new UserLoginPo();
            po.setLoginId(userLogin.getId());
            po.setUserInfoId(userLogin.getUserInfoId());
            po.setLoginName(userLogin.getLoginName());
            po.setPwdSalt(userLogin.getPwdSalt());
            po.setPwd(userLogin.getPwdMd());
            po.setUserType(userLogin.getUserType());
        }
        return po;
    }

    @Override
    public int updateLoginInfo(UserLoginPo userLoginPo) {
        UserLogin record = new UserLogin();
        record.setId(userLoginPo.getLoginId());
        record.setPwdMd(userLoginPo.getPwd());
        record.setPwdSalt(userLoginPo.getPwdSalt());
        return userLoginMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public boolean loginNameIsExist(String loginName) {

        UserLogin userLogin = userLoginMapper.selectByUserName(loginName);

        if (null != userLogin) {
            return true;
        }
        return false;
    }

    @Override
    public PersonInfoVo getPersonInfoByUserId(Long userId) {
        PersonInfo p = personInfoMapper.selectByUserInfoId(userId);
        if (!ValidateUtils.isEmpty(p)) {
            PersonInfoVo vo = new PersonInfoVo();
            vo.setUserCname(p.getUserCname());
            vo.setIdNumber(p.getIdNumber());
            return vo;
        }
        return null;
    }

}
