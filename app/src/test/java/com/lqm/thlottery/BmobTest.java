package com.lqm.thlottery;

import com.lqm.study.util.OssUtils;
import com.lqm.study.util.T;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class BmobTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String a = "jQuery172034829608945419177_1531127399385({\"按时大苏打实打实\"})";
        String  bbb = a.substring(a.indexOf("(")+1, a.lastIndexOf(")"));
        System.out.println(bbb);

    }


    public static void main(String[] args){


    }



}