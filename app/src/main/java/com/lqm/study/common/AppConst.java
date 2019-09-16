package com.lqm.study.common;

/**
 * user：lqm
 * desc：
 */

public class AppConst {

//    public static final String SERVER_ADDRESS = "http://127.0.0.1:8080/";
//    public static final String SERVER_ADDRESS = "http://10.0.2.2:8080"; //模拟器访问本地服务器地址
	public static final String SERVER_ADDRESS = "http://192.168.0.142:8080"; //手机访问本地服务器地址
//    public static final String SERVER_ADDRESS = "http://外网IP:8080/"; //外网



    public static final String TOKEN_KEY = "token_key";
    public static final String IMAGE_HOST = "http://lqmdemo.oss-cn-beijing.aliyuncs.com/";


    public static final String getStsToken = SERVER_ADDRESS + "/file/getStsToken";


    public interface User{

         String login = SERVER_ADDRESS+"/user/login.do";
         String logout = SERVER_ADDRESS+"/user/logout.do";
         String register = SERVER_ADDRESS+"/user/register.do";
         String get_user_info = SERVER_ADDRESS+"/user/get_user_info.do";

    }


    public interface Category{


        String list = SERVER_ADDRESS+"/category/list.do";
        String get_category = SERVER_ADDRESS+"/category/get_category.do";


    }



    public interface Product{

        String list = SERVER_ADDRESS+"/product/list.do";
        String detail = SERVER_ADDRESS+"/product/detail.do";

    }


    public interface Order{
        String pay = SERVER_ADDRESS+"/order/pay.do";
        String getOrderCartProduct = SERVER_ADDRESS+"/order/get_order_cart_product.do";
        String list = SERVER_ADDRESS+"/order/list.do";
        String cancel = SERVER_ADDRESS+"/order/cancel.do";
        String confirmReceivedGoods = SERVER_ADDRESS+"/order/confirm_received_goods.do";
        String detail = SERVER_ADDRESS+"/order/detail.do";
        //从购物车中创建订单
        String create = SERVER_ADDRESS+"/order/create.do";
        //直接购买创建订单
        String createOrderDirect = SERVER_ADDRESS+"/order/create_order_direct.do";
    }

    public interface Shipping{
        String list = SERVER_ADDRESS+"/shipping/list.do";
        String add = SERVER_ADDRESS+"/shipping/add.do";
        String update = SERVER_ADDRESS+"/shipping/update.do";
        String del = SERVER_ADDRESS+"/shipping/del.do";

    }

    public interface Cart{
        String list = SERVER_ADDRESS+"/cart/list.do";
        String add = SERVER_ADDRESS+"/cart/add.do";
        String update = SERVER_ADDRESS+"/cart/update.do";
        String deleteProduct = SERVER_ADDRESS+"/cart/delete_product.do";

        String select = SERVER_ADDRESS+"/cart/select.do";
        String unSelect = SERVER_ADDRESS+"/cart/un_select.do";
        String selectAll = SERVER_ADDRESS+"/cart/select_all.do";
        String unSelectAll = SERVER_ADDRESS+"/cart/un_select_all.do";


    }


}
