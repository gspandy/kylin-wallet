package com.rongcapital.wallet.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.text.log.SysoCounter;
import com.rongcapital.wallet.api.po.CompanyPo;
import com.rongcapital.wallet.api.po.UserWhitePo;
import com.rongcapital.wallet.vo.CompanyVo;
import com.rongcapital.wallet.vo.UserInfoVo;

public class UserExcelRead {

    /**
     * 读取个人用户信息 Discription:
     *
     * @param filePath
     * @return
     * @throws Exception List<UserInfoVo>
     * @author libi
     * @since 2016年10月17日
     */
    public static List<UserInfoVo> getListPerson(File file) throws Exception {

        List<UserInfoVo> list = new ArrayList<>();

        InputStream input = new FileInputStream(file); // 建立输入流

        Workbook wb = new XSSFWorkbook(input);
        Sheet sheet = wb.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            if (null != row) {
                UserInfoVo vo = new UserInfoVo();

                for (int j = 1; j < row.getLastCellNum(); j++) {
                    Cell c = row.getCell(j);
                    if (null != c) {

                        if (j == 1) {
                            vo.setUserName(c.getStringCellValue().trim());
                        } else if (j == 2) {
                            vo.setUserRealName(c.getStringCellValue().trim());
                        } else if (j == 3) {
                            vo.setIdCard(c.getStringCellValue().trim());
                        }

                    }

                }
                list.add(vo);
            }

        }

        return list;
    }

    /**
     * 读取企业用户信息 Discription:
     *
     * @param filePath
     * @return
     * @throws Exception List<UserInfoVo>
     * @author libi
     * @since 2016年10月17日
     */
    public static void getListCompany(File file,List<CompanyPo> list,Map<String,String[]> mapWhite,Map<String,String> telMap) throws Exception {
        
        InputStream input = new FileInputStream(file); // 建立输入流
        Workbook wb = new XSSFWorkbook(input);
        Sheet sheet = wb.getSheetAt(0);  //公司注册信息
        Sheet sheet1 = wb.getSheetAt(1);  //白名单信息

        
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            if (null != row) {
                CompanyPo vo = new CompanyPo();

                for (int j = 0; j < row.getLastCellNum(); j++) {
                    Cell c = row.getCell(j);
                    if (null != c) {
                        String v = c.getStringCellValue().trim();
                        if(v.equals("")){
                            continue;
                        }
                        if (j == 0) {
                            vo.setCompanyName(v); // 公司名
                            vo.setLoginName(v);  // 登陆名
                        } else if (j == 1) {
                            vo.setBusLince(v); // 执照
                        } else if (j == 2) {
                            vo.setCorporateTel(v); // 授权人手机号
                        } else if (j == 3) {
                            vo.setBankName(v);  //企业开户行名称
                        }else if(j==4){
                            
                            vo.setBankCode(v);  //企业开户行代码
                        }else if(j==5){
                           
                            vo.setComBankNum(v); // 企业开户行卡号
                        } else if (j == 6) {
                            vo.setCorporateName(v); // 法人姓名 Y
                        } else if (j == 7) {
                            
                            vo.setCorporateIdentity(v); // 法人身份证
                            
                        } else if (j == 8) {
                            
                            vo.setBindBankName(v);  //绑卡人银行名称
                           
                        }else if(j==9){
                            
                            vo.setBindBankCode(v);  //绑卡人银行code
                            
                        }else if(j==10){
                            
                            vo.setBindBankNum(v); // 绑卡卡号
                           
                            
                        }else if(j==11){
                            vo.setBindIdCard(v);  //绑卡人身份证号
                        }else if(j==12){
                            
                            vo.setBindRealName(v);  //绑卡人姓名
                        }else if(j==13){
                            
                            vo.setBankbranchname(v);  //分行名称
                        }else if(j==14){
                            
                            vo.setBankBranch(v);  //分行code
                        }else if(j==15){
                            
                            vo.setBankProvince(v);  //开户行省
                        }else if(j==16){
                            
                            vo.setBankCity(v);  //开户行市
                        }

                    }

                }
                vo.setUserName("万众企业账户"); // 用户名称，及接入机构的用户名
                telMap.put(vo.getCompanyName(), vo.getCorporateTel());
                list.add(vo);
            } 
        }

        
      //白名单
        
        for (int i = 0; i <= sheet1.getLastRowNum(); i++) {
            Row row = sheet1.getRow(i);    
            if (null != row) {
              String key = null;
              String[] value = null;
              for (int j = 0; j < row.getLastCellNum(); j++) {
                  Cell c = row.getCell(j);
                  if (null != c) {
                      String v = c.getStringCellValue().trim();
                      if (j == 0) {
                          key=v;
                      } else if(j==1){                      
                          value=v.split("##");
                      }
                  }

              }
              mapWhite.put(key, value);
            } 
        }
      
    }

    
    /**
     * 读取企业用户信息 Discription:
     *
     * @param filePath
     * @return
     * @throws Exception List<UserInfoVo>
     * @author libi
     * @since 2016年10月17日
     */
    public static void getListCompanyWhite(File file,List<CompanyPo> list) throws Exception {
        
        InputStream input = new FileInputStream(file); // 建立输入流
        Workbook wb = new XSSFWorkbook(input);
        Sheet sheet = wb.getSheetAt(0);  //公司注册信息
  
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            if (null != row) {
                CompanyPo vo = new CompanyPo();

                for (int j = 0; j < row.getLastCellNum(); j++) {
                    Cell c = row.getCell(j);
                    if (null != c) {
                        String v = c.getStringCellValue().trim();
                        if(v.equals("")){
                            continue;
                        }
                        if (j == 0) {
                            vo.setCompanyName(v); // 公司名
                            vo.setLoginName(v);  // 登陆名
                        } else if (j == 1) {
                            vo.setBusLince(v); // 执照
                        } else if (j == 2) {
                            vo.setBankName(v);  //企业开户行名称
                        } else if (j == 3) {
                            vo.setBankCode(v);  //企业开户行代码
                        }else if(j==4){
                            
                            vo.setComBankNum(v); // 企业开户行卡号
                        }else if(j==5){
                           
                           
                        } 

                    }

                }
                vo.setUserName("万众白名单账户"); // 用户名称，及接入机构的用户名
               
                list.add(vo);
            } 
        }
  
    }
    
    
    /**
     * 批量设置白名单
     * Discription:
     * @param file
     * @return
     * @throws Exception List<UserWhitePo>
     * @author libi
     * @since 2016年12月14日
     */
    public static List<UserWhitePo> getWhiteList(File file) throws Exception {

        List<UserWhitePo> list = new ArrayList<>();

        InputStream input = new FileInputStream(file); // 建立输入流

        Workbook wb = new XSSFWorkbook(input);
        Sheet sheet = wb.getSheetAt(0);
     
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (null != row) {
                UserWhitePo po = new UserWhitePo();             
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    Cell c = row.getCell(j);
                    if (null != c) {
                        String v = c.getStringCellValue().trim();
                        if (j == 0) {
                            po.setComName(v);
                        } else if(j==1){
                            po.setRefComNameArry(v.split("##"));
                           
                        }
                    }

                }              
                list.add(po);
            }

        }

        return list;
    }

    public static void main(String[] args) throws Exception {
        String filePath = "E:\\regist_company.xlsx";
        File file = new File(filePath);
        List<CompanyPo> list =new ArrayList<>();// 建立输入流
        Map<String,String[]> mapWhite=new HashMap<>();
        Map<String,String> mapTel=new HashMap();
        getListCompany(file,list,mapWhite,mapTel);
        
        for ( int i=0; i<1; i++) {
            CompanyPo vo=list.get(i);
            System.out.println("企业名:" + vo.getCompanyName());
            System.out.println("执照:" + vo.getBusLince());
            System.out.println("手机号:" + vo.getCorporateTel());
            System.out.println("开户行:" + vo.getBankName());
            System.out.println("开户行code:" + vo.getBankCode());
            System.out.println("企业开户行卡号:" + vo.getComBankNum()); // 企业开户行卡号
            System.out.println("法人姓名:" + vo.getCorporateName());
            System.out.println("法人身份证:" + vo.getCorporateIdentity());
            System.out.println("绑私卡卡号:" + vo.getBindBankNum());
            System.out.println("绑私卡人姓名:" + vo.getBindRealName());
            System.out.println("绑私卡人身份证:" + vo.getBindIdCard());
            System.out.println("绑私卡人银行名:" + vo.getBindBankName());
            System.out.println("绑私卡人银行code:" + vo.getBindBankCode());
            System.out.println("绑私卡人银行卡号:" + vo.getBindBankNum());
        }
        
        for(String v:mapWhite.keySet()){
            System.out.println(v);
            for(String a:mapWhite.get(v)){
               System.out.println(a);
            }
        }
        
        for(String v:mapTel.keySet()){
            System.out.println(v);
            System.out.println(mapTel.get(v));
        }
        
//        String filePath = "E:\\bath_white.xlsx";
//        File file = new File(filePath);
//        List<UserWhitePo> list=getWhiteList(file);
//        for(UserWhitePo po:list){
//            System.out.println(po.getComName());
//            for(String v:po.getRefComNameArry()){
//                System.out.println(v);
//            }
//        }
      
        
//      String filePath = "E:\\regist_company_white.xlsx";
//      File file = new File(filePath);
//      List<CompanyPo> list =new ArrayList<>();// 建立输入流
//      getListCompanyWhite(file, list);
//      for (CompanyPo vo : list) {
//          System.out.println("企业名:" + vo.getCompanyName());
//          System.out.println("执照:" + vo.getBusLince());
//          System.out.println("开户行:" + vo.getBankName());
//          System.out.println("开户行code:" + vo.getBankCode());
//          System.out.println("企业开户行卡号:" + vo.getComBankNum()); // 企业开户行卡号
//      }
       
    }

}
