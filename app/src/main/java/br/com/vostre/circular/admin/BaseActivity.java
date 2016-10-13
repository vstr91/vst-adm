package br.com.vostre.circular.admin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.vostre.circular.admin.utils.ScreenUtils;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScreenUtils.modificaHeaderBandeja(this);

    }
}
