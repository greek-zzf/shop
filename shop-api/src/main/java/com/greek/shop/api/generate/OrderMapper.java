package com.greek.shop.api.generate;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER
     *
     * @mbg.generated Wed Jan 26 15:39:07 CST 2022
     */
    long countByExample(OrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER
     *
     * @mbg.generated Wed Jan 26 15:39:07 CST 2022
     */
    int deleteByExample(OrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER
     *
     * @mbg.generated Wed Jan 26 15:39:07 CST 2022
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER
     *
     * @mbg.generated Wed Jan 26 15:39:07 CST 2022
     */
    int insert(Order record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER
     *
     * @mbg.generated Wed Jan 26 15:39:07 CST 2022
     */
    int insertSelective(Order record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER
     *
     * @mbg.generated Wed Jan 26 15:39:07 CST 2022
     */
    List<Order> selectByExample(OrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER
     *
     * @mbg.generated Wed Jan 26 15:39:07 CST 2022
     */
    Order selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER
     *
     * @mbg.generated Wed Jan 26 15:39:07 CST 2022
     */
    int updateByExampleSelective(@Param("record") Order record, @Param("example") OrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER
     *
     * @mbg.generated Wed Jan 26 15:39:07 CST 2022
     */
    int updateByExample(@Param("record") Order record, @Param("example") OrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER
     *
     * @mbg.generated Wed Jan 26 15:39:07 CST 2022
     */
    int updateByPrimaryKeySelective(Order record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER
     *
     * @mbg.generated Wed Jan 26 15:39:07 CST 2022
     */
    int updateByPrimaryKey(Order record);
}