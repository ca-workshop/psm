package com.ca.passwordmanager;

import android.app.Application;

import com.ca.passwordmanager.data.AppDatabase;
import com.ca.passwordmanager.data.PasswordRepository;

public class PasswordManagerApp extends Application {

    private AppContainer appContainer;

    @Override
    public void onCreate() {
        super.onCreate();
        appContainer = new AppContainer(this);
    }

    public AppContainer getAppContainer() {
        return appContainer;
    }

    // Simple DI container
    public static class AppContainer {
        public final AppDatabase database;
        public final PasswordRepository passwordRepository;

        public AppContainer(Application application) {
            database = AppDatabase.getInstance(application.getApplicationContext());

            // ✅ Repository now builds internally from the DB via Application
            passwordRepository = new PasswordRepository(application);
        }
    }
}
