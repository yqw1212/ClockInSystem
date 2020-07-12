package service;

import entity.ClockInfo;

/**
 * @author: yqw
 * @date: 2020/7/6
 * @description: 观察者模式接口
 */
public interface Department {
    //添加打卡信息类
    public void addClockInfo(ClockInfo clockInfo);

    //展示打卡信息
    public void showClockInfo();
}
