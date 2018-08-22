package com.ckjs.ck.Tool;

import android.content.Context;

import com.ckjs.ck.Application.CkApplication;

/**
 * Created by NiPing and Airr Wang on 2017/7/17.
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class MoudleTwoTool {
    public static void initLoginHxAndJpush() {
        try {
            int name = (int) SPUtils.get("user_id", 0);
            String pwd = (String) SPUtils.get("easepsd", "");
            if (name > 0 && (name + "") != null && !(name + "").equals("") && pwd != null && !pwd.equals("")) {
                MoudleUtils.hxLG((name + ""), pwd);
            }
            MoudleUtils.mySetAlias(SPUtils.get(CkApplication.getInstance(), "user_id", 0) + "");//设置极光推送别名
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解决实名认证和支付时的type的名字重复问题将vip1且有电话号的人进行type存为1
     *
     * @param context
     */
    public static void initToLoginType(Context context) {
        try {
            if (MoudleUtils.getVersionCode(context) == 28) {
                String vip = (String) SPUtils.get(context, "vip", "");
                String tel = (String) SPUtils.get(context, "tel", "");
                if (vip == null) {
                    return;
                }
                if (vip.equals("1")) {
                    if (tel == null) {
                        return;
                    }
                    if (!tel.equals("")) {
                        SavaDataLocalUtils.saveDataString(context, "type", "1");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
