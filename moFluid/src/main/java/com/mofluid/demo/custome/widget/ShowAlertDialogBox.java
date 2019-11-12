package com.mofluid.magento2.custome.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.mofluid.magento2.R;

/**
 * Created by ebizon on 26/10/15.
 */
public class ShowAlertDialogBox {

    public void showAlertDialog(Context context, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        alertDialog.setIcon(R.drawable.warning);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void showCustomeDialogBox(Context context, String title, String message)
    {
        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

        wmlp.gravity = Gravity.CENTER|Gravity.CENTER;
       /* wmlp.x = 50;   //x position
        wmlp.y = 50;   //y position*/


        dialog.setContentView(R.layout.dialog_validation_layout);

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Button btnBialog = (Button) dialog.findViewById(R.id.btnBialog);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.tvMessage);
        tvMessage.setText(message);
        dialog.setCancelable(false);

        btnBialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();

            }
        });
        dialog.show();

    }
    public void showCustomeDialogBoxWithoutTitle(Context context,  String message)
    {
        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

        wmlp.gravity = Gravity.CENTER;
       /* wmlp.x = 50;   //x position
        wmlp.y = 50;   //y position*/


        dialog.setContentView(R.layout.dialogboxwithouttitle);

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        Button btnBialog = (Button) dialog.findViewById(R.id.btnBialog);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.tvMessage);
        tvMessage.setText(message);
        dialog.setCancelable(false);

        btnBialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();

            }
        });
        dialog.show();

    }
}
