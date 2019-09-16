package com.lqm.study.helper;

public enum ResponseCode {


        SUCCESS(0),
        ERROR(1),
        NEED_LOGIN(10),
        ILLEGAL_ARGUMENT(2);

        private final int code;


        ResponseCode(int code){
            this.code = code;
        }

        public int getCode(){
            return code;
        }
}
