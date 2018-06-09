package onlyw.example.com.onlyw;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText editTextAccount=(EditText)findViewById(R.id.registeraccount);
        EditText editTextPassword=(EditText)findViewById(R.id.registerpassword);
        EditText editTextPasswordVal=(EditText)findViewById(R.id.registerpasswordval);
    }
}
