package com.lqm.study.model.pojo;

import java.io.Serializable;

public class ProductBean implements Serializable{

        /**
         * id : 26
         * categoryId : 100002
         * name : Apple iPhone 7 Plus (A1661) 128G 玫瑰金色 移动联通电信4G手机
         * subtitle : iPhone 7，现更以红色呈现。
         * mainImage : 241997c4-9e62-4824-b7f0-7425c3c28917.jpeg
         * price : 6999
         * stock : 9991
         * status : 1
         * createTime : 2019-02-19 13:56:11
         * updateTime : 2017-04-13 21:45:41
         * subImages : 241997c4-9e62-4824-b7f0-7425c3c28917.jpeg,b6c56eb0-1748-49a9-98dc-bcc4b9788a54.jpeg,92f17532-1527-4563-aa1d-ed01baa0f7b2.jpeg,3adbe4f7-e374-4533-aa79-cc4a98c529bf.jpeg
         * detail : <p><img alt="10000.jpg" src="http://img.happymmall.com/00bce8d4-e9af-4c8d-b205-e6c75c7e252b.jpg" width="790" height="553"><br></p><p><img alt="20000.jpg" src="http://img.happymmall.com/4a70b4b4-01ee-46af-9468-31e67d0995b8.jpg" width="790" height="525"><br></p><p><img alt="30000.jpg" src="http://img.happymmall.com/0570e033-12d7-49b2-88f3-7a5d84157223.jpg" width="790" height="365"><br></p><p><img alt="40000.jpg" src="http://img.happymmall.com/50515c02-3255-44b9-a829-9e141a28c08a.jpg" width="790" height="525"><br></p><p><img alt="50000.jpg" src="http://img.happymmall.com/c138fc56-5843-4287-a029-91cf3732d034.jpg" width="790" height="525"><br></p><p><img alt="60000.jpg" src="http://img.happymmall.com/c92d1f8a-9827-453f-9d37-b10a3287e894.jpg" width="790" height="525"><br></p><p><br></p><p><img alt="TB24p51hgFkpuFjSspnXXb4qFXa-1776456424.jpg" src="http://img.happymmall.com/bb1511fc-3483-471f-80e5-c7c81fa5e1dd.jpg" width="790" height="375"><br></p><p><br></p><p><img alt="shouhou.jpg" src="http://img.happymmall.com/698e6fbe-97ea-478b-8170-008ad24030f7.jpg" width="750" height="150"><br></p><p><img alt="999.jpg" src="http://img.happymmall.com/ee276fe6-5d79-45aa-8393-ba1d210f9c89.jpg" width="790" height="351"><br></p>
         */

        private int id;
        private int categoryId;
        private String name;
        private String subtitle;
        private String mainImage;
        private double price;
        private int stock;
        private int status;
        private String createTime;
        private String updateTime;
        private String subImages;
        private String detail;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getMainImage() {
            return mainImage;
        }

        public void setMainImage(String mainImage) {
            this.mainImage = mainImage;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getSubImages() {
            return subImages;
        }

        public void setSubImages(String subImages) {
            this.subImages = subImages;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }
    }
