package DataAndModels;

public class LoginActivity extends androidx.appcompat.app.AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize our data
        androidx.datastore.core.DataStore.init(this);

        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        com.google.firebase.inappmessaging.model.Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String pass = etPassword.getText().toString();

            User user = androidx.datastore.core.DataStore.login(email, pass);
            if (user != null) {
                // Navigate to the Job Listings screen
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Invalid MSU-IIT Credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
}