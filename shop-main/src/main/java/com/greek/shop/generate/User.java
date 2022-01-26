package com.greek.shop.generate;

import java.util.Date;

public class User {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column USER.ID
     *
     * @mbg.generated Tue Jan 25 10:49:02 CST 2022
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column USER.NAME
     *
     * @mbg.generated Tue Jan 25 10:49:02 CST 2022
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column USER.TEL
     *
     * @mbg.generated Tue Jan 25 10:49:02 CST 2022
     */
    private String tel;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column USER.AVATAR_URL
     *
     * @mbg.generated Tue Jan 25 10:49:02 CST 2022
     */
    private String avatarUrl;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column USER.ADDRESS
     *
     * @mbg.generated Tue Jan 25 10:49:02 CST 2022
     */
    private String address;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column USER.CREATE_AT
     *
     * @mbg.generated Tue Jan 25 10:49:02 CST 2022
     */
    private Date createAt;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column USER.UPDATE_AT
     *
     * @mbg.generated Tue Jan 25 10:49:02 CST 2022
     */
    private Date updateAt;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column USER.ID
     *
     * @return the value of USER.ID
     *
     * @mbg.generated Tue Jan 25 10:49:02 CST 2022
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column USER.ID
     *
     * @param id the value for USER.ID
     *
     * @mbg.generated Tue Jan 25 10:49:02 CST 2022
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column USER.NAME
     *
     * @return the value of USER.NAME
     *
     * @mbg.generated Tue Jan 25 10:49:02 CST 2022
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column USER.NAME
     *
     * @param name the value for USER.NAME
     *
     * @mbg.generated Tue Jan 25 10:49:02 CST 2022
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column USER.TEL
     *
     * @return the value of USER.TEL
     *
     * @mbg.generated Tue Jan 25 10:49:02 CST 2022
     */
    public String getTel() {
        return tel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column USER.TEL
     *
     * @param tel the value for USER.TEL
     *
     * @mbg.generated Tue Jan 25 10:49:02 CST 2022
     */
    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column USER.AVATAR_URL
     *
     * @return the value of USER.AVATAR_URL
     *
     * @mbg.generated Tue Jan 25 10:49:02 CST 2022
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column USER.AVATAR_URL
     *
     * @param avatarUrl the value for USER.AVATAR_URL
     *
     * @mbg.generated Tue Jan 25 10:49:02 CST 2022
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl == null ? null : avatarUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column USER.ADDRESS
     *
     * @return the value of USER.ADDRESS
     *
     * @mbg.generated Tue Jan 25 10:49:02 CST 2022
     */
    public String getAddress() {
        return address;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column USER.ADDRESS
     *
     * @param address the value for USER.ADDRESS
     *
     * @mbg.generated Tue Jan 25 10:49:02 CST 2022
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column USER.CREATE_AT
     *
     * @return the value of USER.CREATE_AT
     *
     * @mbg.generated Tue Jan 25 10:49:02 CST 2022
     */
    public Date getCreateAt() {
        return createAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column USER.CREATE_AT
     *
     * @param createAt the value for USER.CREATE_AT
     *
     * @mbg.generated Tue Jan 25 10:49:02 CST 2022
     */
    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column USER.UPDATE_AT
     *
     * @return the value of USER.UPDATE_AT
     *
     * @mbg.generated Tue Jan 25 10:49:02 CST 2022
     */
    public Date getUpdateAt() {
        return updateAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column USER.UPDATE_AT
     *
     * @param updateAt the value for USER.UPDATE_AT
     *
     * @mbg.generated Tue Jan 25 10:49:02 CST 2022
     */
    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}