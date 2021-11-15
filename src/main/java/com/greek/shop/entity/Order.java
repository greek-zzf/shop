package com.greek.shop.entity;

import java.util.Date;

public class Order {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORDER.ID
     *
     * @mbg.generated Mon Nov 15 14:36:24 GMT+08:00 2021
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORDER.USER_ID
     *
     * @mbg.generated Mon Nov 15 14:36:24 GMT+08:00 2021
     */
    private Long userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORDER.TOTAL_PRICE
     *
     * @mbg.generated Mon Nov 15 14:36:24 GMT+08:00 2021
     */
    private Long totalPrice;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORDER.ADDRESS
     *
     * @mbg.generated Mon Nov 15 14:36:24 GMT+08:00 2021
     */
    private String address;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORDER.EXPRESS_COMPANY
     *
     * @mbg.generated Mon Nov 15 14:36:24 GMT+08:00 2021
     */
    private String expressCompany;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORDER.EXPRESS_ID
     *
     * @mbg.generated Mon Nov 15 14:36:24 GMT+08:00 2021
     */
    private String expressId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORDER.STATUS
     *
     * @mbg.generated Mon Nov 15 14:36:24 GMT+08:00 2021
     */
    private String status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORDER.CREATED_AT
     *
     * @mbg.generated Mon Nov 15 14:36:24 GMT+08:00 2021
     */
    private Date createdAt;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORDER.UPDATED_AT
     *
     * @mbg.generated Mon Nov 15 14:36:24 GMT+08:00 2021
     */
    private Date updatedAt;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORDER.ID
     *
     * @return the value of ORDER.ID
     *
     * @mbg.generated Mon Nov 15 14:36:24 GMT+08:00 2021
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORDER.ID
     *
     * @param id the value for ORDER.ID
     *
     * @mbg.generated Mon Nov 15 14:36:24 GMT+08:00 2021
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORDER.USER_ID
     *
     * @return the value of ORDER.USER_ID
     *
     * @mbg.generated Mon Nov 15 14:36:24 GMT+08:00 2021
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORDER.USER_ID
     *
     * @param userId the value for ORDER.USER_ID
     *
     * @mbg.generated Mon Nov 15 14:36:24 GMT+08:00 2021
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORDER.TOTAL_PRICE
     *
     * @return the value of ORDER.TOTAL_PRICE
     *
     * @mbg.generated Mon Nov 15 14:36:24 GMT+08:00 2021
     */
    public Long getTotalPrice() {
        return totalPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORDER.TOTAL_PRICE
     *
     * @param totalPrice the value for ORDER.TOTAL_PRICE
     *
     * @mbg.generated Mon Nov 15 14:36:24 GMT+08:00 2021
     */
    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORDER.ADDRESS
     *
     * @return the value of ORDER.ADDRESS
     *
     * @mbg.generated Mon Nov 15 14:36:24 GMT+08:00 2021
     */
    public String getAddress() {
        return address;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORDER.ADDRESS
     *
     * @param address the value for ORDER.ADDRESS
     *
     * @mbg.generated Mon Nov 15 14:36:24 GMT+08:00 2021
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORDER.EXPRESS_COMPANY
     *
     * @return the value of ORDER.EXPRESS_COMPANY
     *
     * @mbg.generated Mon Nov 15 14:36:24 GMT+08:00 2021
     */
    public String getExpressCompany() {
        return expressCompany;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORDER.EXPRESS_COMPANY
     *
     * @param expressCompany the value for ORDER.EXPRESS_COMPANY
     *
     * @mbg.generated Mon Nov 15 14:36:24 GMT+08:00 2021
     */
    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany == null ? null : expressCompany.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORDER.EXPRESS_ID
     *
     * @return the value of ORDER.EXPRESS_ID
     *
     * @mbg.generated Mon Nov 15 14:36:24 GMT+08:00 2021
     */
    public String getExpressId() {
        return expressId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORDER.EXPRESS_ID
     *
     * @param expressId the value for ORDER.EXPRESS_ID
     *
     * @mbg.generated Mon Nov 15 14:36:24 GMT+08:00 2021
     */
    public void setExpressId(String expressId) {
        this.expressId = expressId == null ? null : expressId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORDER.STATUS
     *
     * @return the value of ORDER.STATUS
     *
     * @mbg.generated Mon Nov 15 14:36:24 GMT+08:00 2021
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORDER.STATUS
     *
     * @param status the value for ORDER.STATUS
     *
     * @mbg.generated Mon Nov 15 14:36:24 GMT+08:00 2021
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORDER.CREATED_AT
     *
     * @return the value of ORDER.CREATED_AT
     *
     * @mbg.generated Mon Nov 15 14:36:24 GMT+08:00 2021
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORDER.CREATED_AT
     *
     * @param createdAt the value for ORDER.CREATED_AT
     *
     * @mbg.generated Mon Nov 15 14:36:24 GMT+08:00 2021
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORDER.UPDATED_AT
     *
     * @return the value of ORDER.UPDATED_AT
     *
     * @mbg.generated Mon Nov 15 14:36:24 GMT+08:00 2021
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORDER.UPDATED_AT
     *
     * @param updatedAt the value for ORDER.UPDATED_AT
     *
     * @mbg.generated Mon Nov 15 14:36:24 GMT+08:00 2021
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}