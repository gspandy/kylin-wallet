package com.rongcapital.wallet.service.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Rop.api.internal.cache.ValidateUtil;
import com.rkylin.util.bean.BeanMapper;
import com.rongcapital.wallet.api.po.CompanyPo;
import com.rongcapital.wallet.api.po.UserInfoPo;
import com.rongcapital.wallet.api.po.UserLoginPo;
import com.rongcapital.wallet.api.po.UserWhitePo;
import com.rongcapital.wallet.api.service.UserInfoApiService;
import com.rongcapital.wallet.api.vo.UserWhiteVo;
import com.rongcapital.wallet.constant.UserInfoConstants;
import com.rongcapital.wallet.dao.CompanyInfoMapper;
import com.rongcapital.wallet.dao.PersonInfoMapper;
import com.rongcapital.wallet.dao.UserInfoMapper;
import com.rongcapital.wallet.dao.UserLoginMapper;
import com.rongcapital.wallet.dao.UserWhiteMapper;
import com.rongcapital.wallet.po.CompanyInfo;
import com.rongcapital.wallet.po.PersonInfo;
import com.rongcapital.wallet.po.UserInfo;
import com.rongcapital.wallet.po.UserLogin;
import com.rongcapital.wallet.po.UserWhite;
import com.rongcapital.wallet.util.validate.ValidateUtils;

@Service("userInfoApiService")
public class UserInfoApiServiceImpl implements UserInfoApiService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private PersonInfoMapper personInfoMapper;

    @Autowired
    private CompanyInfoMapper companyInfoMapper;

    @Autowired
    private UserLoginMapper userLoginMapper;

    @Autowired
    private UserWhiteMapper userWhiteMapper;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public boolean register(UserInfoPo userInfo) {
        UserInfo user = new UserInfo();
        user.setUserType(userInfo.getUserType());
        user.setStatus(UserInfoConstants.ACCOUNT_OK); // 状态
        user.setUserId(userInfo.getUserId());
        user.setUserCode(userInfo.getUserCode());
        int resultId = userInfoMapper.insertSelective(user);
        if (resultId == 1) {
            Long userInfoId = user.getUserInfoId(); // 主键ID
            if (userInfo.getUserType() == 2) {
                PersonInfo person = new PersonInfo();
                person.setUserInfoId(userInfoId);
                person.setUserCname(userInfo.getUserRealName());
                person.setIdNumber(userInfo.getIdCard());
                personInfoMapper.insertSelective(person);

                UserLogin userLogin = new UserLogin();
                userLogin.setUserInfoId(userInfoId);
                userLogin.setPwdSalt(userInfo.getSalt());
                userLogin.setLoginName(userInfo.getUserName());
                userLogin.setPwdMd(userInfo.getPwd());
                userLogin.setUserType(userInfo.getUserType());
                userLoginMapper.insertSelective(userLogin);
                return true;
            }
        }
        return false;
    }

    public List<UserInfoPo> getList() throws Exception {

        return null;
    }

    public boolean login(UserInfoPo userInfo) {

        return false;
    }

    @Override
    public UserLoginPo getLoginInfo(String userName) throws Exception {

        return null;
    }

    @Override
    public UserInfoPo getUserInfoById(Long userId) throws Exception {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        if (null != userInfo) {

            UserInfoPo po = new UserInfoPo();
            po.setUserInfoId(userInfo.getUserInfoId());
            po.setUserId(userInfo.getUserId());
            po.setStatus(userInfo.getStatus());
            po.setUserType(userInfo.getUserType());
            po.setUserCode(userInfo.getUserCode());

            if (UserInfoConstants.ACCOUNT_PROPERTY_ENTERPRISE_INT == userInfo.getUserType()) { // 企业用户
                CompanyInfo com = userInfo.getCompanyInfo();
                if (null != com) {
                    po.setCompanyName(com.getCompanyName());
                    po.setBuslince(com.getBuslince());
                    po.setUserRealName(com.getCorporateName());
                    po.setIdCard(com.getCorporateIdentity());
                    po.setCorporateTel(com.getCorporateTel());
                }

            } else if (UserInfoConstants.ACCOUNT_PROPERTY_PERSONAGE_INT == userInfo.getUserType()) { // 个人
                PersonInfo p = userInfo.getPersonInfo();
                if (null != p) {
                    po.setUserRealName(p.getUserCname());
                    po.setIdCard(p.getIdNumber());
                }
            }

            return po;
        }
        return null;
    }

    @Override
    public UserInfoPo getUserInfoByUserId(String userId) throws Exception {
        UserInfo userInfo = userInfoMapper.selectByUserId(userId);
        if (userInfo == null) {
            return null;
        }
        UserInfoPo po = null;
        if (UserInfoConstants.ACCOUNT_PROPERTY_ENTERPRISE_INT == userInfo.getUserType()) {
            po = new UserInfoPo();
            CompanyInfo companyInfo = companyInfoMapper.selectByUserInfoId(userInfo.getUserInfoId());
            po.setUserInfoId(userInfo.getUserInfoId());
            po.setUserId(userInfo.getUserId());
            po.setStatus(userInfo.getStatus());
            po.setUserType(userInfo.getUserType());
            po.setUserCode(userInfo.getUserCode());
            if (null != companyInfo) {
                po.setUserRealName(companyInfo.getCompanyName());
                po.setIdCard(companyInfo.getBuslince());
            }
        } else if (UserInfoConstants.ACCOUNT_PROPERTY_PERSONAGE_INT == userInfo.getUserType()) {

            po = new UserInfoPo();
            PersonInfo p = personInfoMapper.selectByUserInfoId(userInfo.getUserInfoId());

            po.setUserInfoId(userInfo.getUserInfoId());
            po.setUserId(userInfo.getUserId());
            po.setStatus(userInfo.getStatus());
            po.setUserType(userInfo.getUserType());
            po.setUserCode(userInfo.getUserCode());
            if (null != p) {
                po.setUserRealName(p.getUserCname());
                po.setIdCard(p.getIdNumber());
            }

        }
        return po;
    }

    public List<UserWhiteVo> getUserWhiteList(UserWhitePo userWhitePo) throws Exception {
        List<UserWhiteVo> listVo = null;
        UserWhite userWhite = new UserWhite();
        userWhite.setUserId(userWhitePo.getUserId());
        userWhite.setRefId(userWhite.getRefId());
        userWhite.setApplyId(userWhitePo.getApplyId());
        userWhite.setRefType(userWhitePo.getRefType());
        List<UserWhite> list = userWhiteMapper.getList(userWhite);
        if (null != list && list.size() > 0) {
            listVo = new ArrayList<>();
            for (UserWhite u : list) {
                String refId = u.getRefId(); // 用户ID
                UserInfo userInfo = userInfoMapper.selectByUserIdJoin(refId);

                String userName = "";
                String userRealname = "";
                if (!ValidateUtils.isEmpty(userInfo)) {
                    // 企业用户
                    if (UserInfoConstants.ACCOUNT_PROPERTY_ENTERPRISE_INT == userInfo.getUserType()) {
                        userRealname = userInfo.getCompanyInfo().getCompanyName();
                    } else {
                        userRealname = userInfo.getPersonInfo().getUserCname();
                    }

                    UserLogin userLogin = userLoginMapper.selectByUserInfoId(userInfo.getUserInfoId());
                    userName = userLogin.getLoginName();
                }
                listVo.add(new UserWhiteVo(u.getRefId(), u.getRefName(), u.getRefType(), userName, userRealname));
            }
        }
        return listVo;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public boolean registerCompany(CompanyPo companyPo) throws Exception {
        UserInfo user = new UserInfo();
        user.setUserType(companyPo.getLoginUserType()); // 企业
        user.setStatus(UserInfoConstants.ACCOUNT_OK); // 状态
        user.setUserId(companyPo.getUserId());
        int resultId = userInfoMapper.insertSelective(user);
        if (resultId == 1) {
            Long userInfoId = user.getUserInfoId(); // 主键ID
            CompanyInfo com = new CompanyInfo();
            com.setUserInfoId(userInfoId); // 关联ID
            com.setCompanyName(companyPo.getCompanyName()); // 企业名称
            com.setUserName(companyPo.getUserName()); // 用户名称及接入机构的用户名
            com.setBuslince(companyPo.getBusLince()); // 营业执照
            com.setCorporateName(companyPo.getCorporateName()); // 法人姓名
            com.setCorporateIdentity(companyPo.getCorporateIdentity()); // 法人身份证
            com.setCorporateTel(companyPo.getCorporateTel()); // 法人手机号

            com.setCompanyType(Integer.parseInt(companyPo.getUserType())); // 企业类型
            companyInfoMapper.insertSelective(com);
            UserLogin userLogin = new UserLogin();
            userLogin.setUserInfoId(userInfoId);
            userLogin.setPwdSalt(companyPo.getSalt());
            userLogin.setLoginName(companyPo.getLoginName());
            userLogin.setPwdMd(companyPo.getPwd());
            userLogin.setUserType(companyPo.getLoginUserType());
            userLoginMapper.insertSelective(userLogin);
            return true;
        }
        return false;
    }

    /**
     * 批量插入
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public int bathWhiteList(List<UserWhitePo> list) throws Exception {
        if (null != list && list.size() > 0) {
            List<UserWhite> listA = new ArrayList<>();
            for (UserWhitePo po : list) {
                UserWhite p = new UserWhite();
                BeanMapper.copy(po, p);
                listA.add(p);
            }
            return userWhiteMapper.bathWhiteList(listA);
        }
        return 0;

    }

    /**
     * 白名单是否存在
     */
    public boolean whiteExists(UserWhitePo userWhitePo) throws Exception {
        UserWhite userWhite = new UserWhite();
        BeanMapper.copy(userWhitePo, userWhite);
        List<UserWhite> list = userWhiteMapper.getList(userWhite);
        if (null != list && list.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 保存白名单
     */
    public void saveWhite(UserWhitePo userWhitePo) throws Exception {
        UserWhite userWhite = new UserWhite();
        BeanMapper.copy(userWhitePo, userWhite);
        userWhiteMapper.insertSelective(userWhite);
    }

    /**
     * 删除
     */
    public void delWhite(UserWhitePo userWhitePo) throws Exception {

        UserWhite userWhite = new UserWhite();
        BeanMapper.copy(userWhitePo, userWhite);
        List<UserWhite> list = userWhiteMapper.getList(userWhite);
        if (null != list && list.size() > 0) {
            UserWhite u = list.get(0);
            userWhiteMapper.deleteByPrimaryKey(u.getId());
        }
    }
}
