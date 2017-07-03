package com.rongcapital.wallet.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.rongcapital.wallet.api.po.CompanyPo;
import com.rongcapital.wallet.vo.CompanyVo;
import com.rongcapital.wallet.vo.UserInfoVo;

public class UserExcelRead {

    /**
     * 读取个人用户信息
     * Discription:
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
     * 读取企业用户信息
     * Discription:
     * @param filePath
     * @return
     * @throws Exception List<UserInfoVo>
     * @author libi
     * @since 2016年10月17日
     */
    public static List<CompanyPo> getListCompany(File file) throws Exception {
        List<CompanyPo> list = new ArrayList<>();

        InputStream input = new FileInputStream(file); // 建立输入流
        Workbook wb = new XSSFWorkbook(input);
        Sheet sheet = wb.getSheetAt(0);
     
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            if (null != row) {
                CompanyPo vo = new CompanyPo();
                
                for (int j = 1; j < row.getLastCellNum(); j++) {
                    Cell c = row.getCell(j);
                    if (null != c) {
                        String v=c.getStringCellValue().trim();
                        if (j == 1) {
                            vo.setCompanyName(v); //公司名
                        } else if (j == 2) {
                            vo.setBusLince(v);  //执照
                        } else if (j == 3) {
                            vo.setUserName(v);  //用户名称，及接入机构的用户名
                        } else if (j == 4) {
                            vo.setLoginName(v);  //系统登陆名                          
                        }else if(j==5){
                            vo.setCorporateName(v); // 法人姓名 Y                         
                        }else if(j==6){
                            vo.setCorporateIdentity(v);
                        }

                    }

                }
                list.add(vo);
            }
           

        }

        return list;
    }
    public static void main(String[] args) throws Exception {
        String filePath = "E:\\33.xlsx";
        File file=new File(filePath);
        List<UserInfoVo> list = getListPerson(file); // 建立输入流
        for (UserInfoVo vo : list) {
            System.out.println(vo.getUserName());
            System.out.println(vo.getUserRealName());
            System.out.println(vo.getIdCard());
        }
    }
}
