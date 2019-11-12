package com.mofluid.magento2;

import android.view.View;
import android.widget.CompoundButton;

import com.ebizon.fluid.model.CustomOption;

/**
 * Created by manish on 22/01/16.
 */
public class CustomOptionView {
    private final CustomOption option;
    private final View view;

    public View getView() {
        return view;
    }

    public CustomOptionView(CustomOption option, View view){
        this.option = option;
        this.view = view;

    }

    public int getId(){
        return this.option.getId();
    }

    public void check(Boolean status) {
        if (this.view instanceof CompoundButton) {
            ((CompoundButton) this.view).setChecked(status);
            this.option.setSelected(status);
        }
    }

    public void toggle(){
        this.option.setSelected(!this.option.getSelected());
    }
}
