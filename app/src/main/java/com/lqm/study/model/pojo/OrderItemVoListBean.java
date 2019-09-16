package com.lqm.study.model.pojo;

public class OrderItemVoListBean {

        /**
         * orderNo :
         * productId : 29
         * productName : Haier/海尔HJ100-1HU1 10公斤滚筒洗衣机全自动带烘干家用大容量 洗烘一体
         * productImage : 173335a4-5dce-4afd-9f18-a10623724c4e.jpeg
         * currentUnitPrice : 4299
         * quantity : 3
         * totalPrice : 12897
         * createTime :
         */

        private Long orderNo;
        private int productId;
        private String productName;
        private String productImage;
        private int currentUnitPrice;
        private int quantity;
        private int totalPrice;
        private String createTime;

        public Long getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(Long orderNo) {
            this.orderNo = orderNo;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductImage() {
            return productImage;
        }

        public void setProductImage(String productImage) {
            this.productImage = productImage;
        }

        public int getCurrentUnitPrice() {
            return currentUnitPrice;
        }

        public void setCurrentUnitPrice(int currentUnitPrice) {
            this.currentUnitPrice = currentUnitPrice;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(int totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
}
