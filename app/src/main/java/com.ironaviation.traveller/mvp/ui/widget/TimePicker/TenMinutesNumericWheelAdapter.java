package com.ironaviation.traveller.mvp.ui.widget.TimePicker;

import com.bigkoo.pickerview.adapter.NumericWheelAdapter;

/**
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-04-06 14:32
 * 修改人：starRing
 * 修改时间：2017-04-06 14:32
 * 修改备注：
 */
public class TenMinutesNumericWheelAdapter  extends NumericWheelAdapter {

    private int minValue;
    private int maxValue;

    public TenMinutesNumericWheelAdapter(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public Object getItem(int index) {
        if (index >= 0 && index < getItemsCount()) {
            int value = minValue + (index * 10);
            return value;
        }
        return 0;
    }

    @Override
    public int getItemsCount() {
        return maxValue - minValue + 1;
    }

    @Override
    public int indexOf(Object o) {
        return (int) o - minValue;
    }
}
