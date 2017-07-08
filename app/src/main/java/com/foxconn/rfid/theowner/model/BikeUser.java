package com.foxconn.rfid.theowner.model;

import android.content.Context;

import com.foxconn.rfid.theowner.base.App;
import com.foxconn.rfid.theowner.base.PreferenceData;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.sqlite.Table;


import java.util.List;

/**
 * Created by F1010500 on 2016/11/26.
 */

@Table(name="bike_user")
public class BikeUser {
    private int id;
    private long userId;
    private String session;
    private long companyId;
    private long officeId;
    private String IdCard;
    private String idFrontPicPath;
    private String idBackPicPath;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String mobile;
    private String address;
    private String photo;
    private int sex;
    private int age;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getOfficeId() {
        return officeId;
    }

    public void setOfficeId(long officeId) {
        this.officeId = officeId;
    }

    public String getIdCard() {
        return IdCard;
    }

    public void setIdCard(String idCard) {
        IdCard = idCard;
    }

    public String getIdFrontPicPath() {
        return idFrontPicPath;
    }

    public void setIdFrontPicPath(String idFrontPicPath) {
        this.idFrontPicPath = idFrontPicPath;
    }

    public String getIdBackPicPath() {
        return idBackPicPath;
    }

    public void setIdBackPicPath(String idBackPicPath) {
        this.idBackPicPath = idBackPicPath;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static BikeUser getCurUser(Context context)
    {

            long userId= PreferenceData.loadLoginAccount(context);

            FinalDb db = FinalDb.create(context, App.DB_NAME, true,App.DB_VERSION,(FinalDb.DbUpdateListener) context );
            List<BikeUser> list=db.findAllByWhere(BikeUser.class," userId="+String.valueOf(userId));
            if(list.size()>0)
            {
                return list.get(0);
            }else
            {
               return null;
            }
        }

}
