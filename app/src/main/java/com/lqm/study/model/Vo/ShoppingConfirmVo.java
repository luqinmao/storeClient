package com.lqm.study.model.Vo;

import com.lqm.study.model.pojo.OrderItemVoListBean;

import java.util.List;

public class ShoppingConfirmVo {

        /**
         * orderItemVoList : [{"orderNo":"","productId":29,"productName":"Haier/海尔HJ100-1HU1 10公斤滚筒洗衣机全自动带烘干家用大容量 洗烘一体","productImage":"173335a4-5dce-4afd-9f18-a10623724c4e.jpeg","currentUnitPrice":4299,"quantity":3,"totalPrice":12897,"createTime":""},{"orderNo":"","productId":32,"productName":"Haier/海尔HJ100-1HU1 10公斤滚筒洗衣机全自动带烘干家用大容量 洗烘一体","productImage":"173335a4-5dce-4afd-9f18-a10623724c4e.jpeg","currentUnitPrice":4299,"quantity":2,"totalPrice":8598,"createTime":""}]
         * productTotalPrice : 21495
         * imageHost : ftp://116.62.147.110/img/
         */

        private int productTotalPrice;
        private String imageHost;
        private List<OrderItemVoListBean> orderItemVoList;

        public int getProductTotalPrice() {
            return productTotalPrice;
        }

        public void setProductTotalPrice(int productTotalPrice) {
            this.productTotalPrice = productTotalPrice;
        }

        public String getImageHost() {
            return imageHost;
        }

        public void setImageHost(String imageHost) {
            this.imageHost = imageHost;
        }

        public List<OrderItemVoListBean> getOrderItemVoList() {
            return orderItemVoList;
        }

        public void setOrderItemVoList(List<OrderItemVoListBean> orderItemVoList) {
            this.orderItemVoList = orderItemVoList;
        }


}
