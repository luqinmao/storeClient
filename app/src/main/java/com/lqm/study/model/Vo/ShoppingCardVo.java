package com.lqm.study.model.Vo;

import com.lqm.study.model.pojo.ShoppingCardBean;

import java.util.List;

public class ShoppingCardVo {


        /**
         * cartProductVoList : [{"id":127,"userId":1,"productId":30,"quantity":5,"productName":"Haier/海尔HJ100-1HU1 10公斤滚筒洗衣机全自动带烘干家用大容量 洗烘一体","productSubtitle":"门店机型 德 ","productMainImage":"173335a4-5dce-4afd-9f18-a10623724c4e.jpeg","productPrice":4299,"productStatus":1,"productTotalPrice":21495,"productStock":9993,"productChecked":1,"limitQuantity":"LIMIT_NUM_SUCCESS"}]
         * cartTotalPrice : 21495
         * allChecked : true
         * imageHost : ftp://116.62.147.110/img/
         */

        private int cartTotalPrice;
        private boolean allChecked;
        private String imageHost;
        private List<ShoppingCardBean> cartProductVoList;

        public int getCartTotalPrice() {
            return cartTotalPrice;
        }

        public void setCartTotalPrice(int cartTotalPrice) {
            this.cartTotalPrice = cartTotalPrice;
        }

        public boolean isAllChecked() {
            return allChecked;
        }

        public void setAllChecked(boolean allChecked) {
            this.allChecked = allChecked;
        }

        public String getImageHost() {
            return imageHost;
        }

        public void setImageHost(String imageHost) {
            this.imageHost = imageHost;
        }

        public List<ShoppingCardBean> getCartProductVoList() {
            return cartProductVoList;
        }

        public void setCartProductVoList(List<ShoppingCardBean> cartProductVoList) {
            this.cartProductVoList = cartProductVoList;
        }

        public static class CartProductVoListBean {
            /**
             * id : 127
             * userId : 1
             * productId : 30
             * quantity : 5
             * productName : Haier/海尔HJ100-1HU1 10公斤滚筒洗衣机全自动带烘干家用大容量 洗烘一体
             * productSubtitle : 门店机型 德
             * productMainImage : 173335a4-5dce-4afd-9f18-a10623724c4e.jpeg
             * productPrice : 4299
             * productStatus : 1
             * productTotalPrice : 21495
             * productStock : 9993
             * productChecked : 1
             * limitQuantity : LIMIT_NUM_SUCCESS
             */

            private int id;
            private int userId;
            private int productId;
            private int quantity;
            private String productName;
            private String productSubtitle;
            private String productMainImage;
            private int productPrice;
            private int productStatus;
            private int productTotalPrice;
            private int productStock;
            private int productChecked;
            private String limitQuantity;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getProductId() {
                return productId;
            }

            public void setProductId(int productId) {
                this.productId = productId;
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public String getProductSubtitle() {
                return productSubtitle;
            }

            public void setProductSubtitle(String productSubtitle) {
                this.productSubtitle = productSubtitle;
            }

            public String getProductMainImage() {
                return productMainImage;
            }

            public void setProductMainImage(String productMainImage) {
                this.productMainImage = productMainImage;
            }

            public int getProductPrice() {
                return productPrice;
            }

            public void setProductPrice(int productPrice) {
                this.productPrice = productPrice;
            }

            public int getProductStatus() {
                return productStatus;
            }

            public void setProductStatus(int productStatus) {
                this.productStatus = productStatus;
            }

            public int getProductTotalPrice() {
                return productTotalPrice;
            }

            public void setProductTotalPrice(int productTotalPrice) {
                this.productTotalPrice = productTotalPrice;
            }

            public int getProductStock() {
                return productStock;
            }

            public void setProductStock(int productStock) {
                this.productStock = productStock;
            }

            public int getProductChecked() {
                return productChecked;
            }

            public void setProductChecked(int productChecked) {
                this.productChecked = productChecked;
            }

            public String getLimitQuantity() {
                return limitQuantity;
            }

            public void setLimitQuantity(String limitQuantity) {
                this.limitQuantity = limitQuantity;
            }
        }
}
