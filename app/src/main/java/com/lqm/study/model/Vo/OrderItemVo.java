package com.lqm.study.model.Vo;

import com.lqm.study.model.pojo.OrderItemVoListBean;
import com.lqm.study.model.pojo.ShippingVoBean;

import java.util.List;

public class OrderItemVo {

        /**
         * orderNo : 1558623115408
         * payment : 17196
         * paymentType : 1
         * paymentTypeDesc : 在线支付
         * postage : 0
         * status : 10
         * statusDesc : 未支付
         * paymentTime :
         * sendTime :
         * endTime :
         * closeTime :
         * createTime :
         * orderItemVoList : [{"orderNo":1558623115408,"productId":29,"productName":"Haier/海尔HJ100-1HU1 10公斤滚筒洗衣机全自动带烘干家用大容量 洗烘一体","productImage":"173335a4-5dce-4afd-9f18-a10623724c4e.jpeg","currentUnitPrice":4299,"quantity":1,"totalPrice":4299,"createTime":""},{"orderNo":1558623115408,"productId":32,"productName":"Haier/海尔HJ100-1HU1 10公斤滚筒洗衣机全自动带烘干家用大容量 洗烘一体","productImage":"173335a4-5dce-4afd-9f18-a10623724c4e.jpeg","currentUnitPrice":4299,"quantity":3,"totalPrice":12897,"createTime":""}]
         * imageHost : ftp://116.62.147.110/img/
         * shippingId : 29
         * receiverName : 吉利
         * shippingVo : {"receiverName":"吉利","receiverPhone":"","receiverMobile":"13800138000","receiverProvince":"北京","receiverCity":"北京","receiverDistrict":"海淀区","receiverAddress":"海淀区中关村","receiverZip":"100000"}
         */

        private long orderNo;
        private double payment;
        private int paymentType;
        private String paymentTypeDesc;
        private double postage;
        private int status;
        private String statusDesc;
        private String paymentTime;
        private String sendTime;
        private String endTime;
        private String closeTime;
        private String createTime;
        private String imageHost;
        private int shippingId;
        private String receiverName;
        private ShippingVoBean shippingVo;
        private List<OrderItemVoListBean> orderItemVoList;



        public long getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(long orderNo) {
            this.orderNo = orderNo;
        }

        public double getPayment() {
            return payment;
        }

        public void setPayment(double payment) {
            this.payment = payment;
        }

        public int getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(int paymentType) {
            this.paymentType = paymentType;
        }

        public String getPaymentTypeDesc() {
            return paymentTypeDesc;
        }

        public void setPaymentTypeDesc(String paymentTypeDesc) {
            this.paymentTypeDesc = paymentTypeDesc;
        }

        public double getPostage() {
            return postage;
        }

        public void setPostage(double postage) {
            this.postage = postage;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getStatusDesc() {
            return statusDesc;
        }

        public void setStatusDesc(String statusDesc) {
            this.statusDesc = statusDesc;
        }

        public String getPaymentTime() {
            return paymentTime;
        }

        public void setPaymentTime(String paymentTime) {
            this.paymentTime = paymentTime;
        }

        public String getSendTime() {
            return sendTime;
        }

        public void setSendTime(String sendTime) {
            this.sendTime = sendTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getCloseTime() {
            return closeTime;
        }

        public void setCloseTime(String closeTime) {
            this.closeTime = closeTime;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getImageHost() {
            return imageHost;
        }

        public void setImageHost(String imageHost) {
            this.imageHost = imageHost;
        }

        public int getShippingId() {
            return shippingId;
        }

        public void setShippingId(int shippingId) {
            this.shippingId = shippingId;
        }

        public String getReceiverName() {
            return receiverName;
        }

        public void setReceiverName(String receiverName) {
            this.receiverName = receiverName;
        }

        public ShippingVoBean getShippingVo() {
            return shippingVo;
        }

        public void setShippingVo(ShippingVoBean shippingVo) {
            this.shippingVo = shippingVo;
        }

        public List<OrderItemVoListBean> getOrderItemVoList() {
            return orderItemVoList;
        }

        public void setOrderItemVoList(List<OrderItemVoListBean> orderItemVoList) {
            this.orderItemVoList = orderItemVoList;
        }



}
