package com.demi;

import com.demi.bean.Admin;
import com.demi.global.Section;
import com.demi.global.AdminDetail;
import com.demi.global.plugin.SectionManager;
import com.demi.service.AdminService;
import com.demi.global.plugin.ViewManager;
import com.gn.decorator.GNDecorator;
import com.gn.decorator.options.ButtonType;
import com.demi.module.loader.Loader;
import com.demi.module.main.Main;
import com.demi.service.impl.AdminServiceImpl;
import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private float  increment = 0;
    private float  progress = 0;
    private Section section;
    private Admin admin;

    public static final GNDecorator decorator = new GNDecorator();
    public static final Scene scene = decorator.getScene();

    public static ObservableList<String>    stylesheets;
    public static HostServices              hostServices;
    private static AdminDetail adminDetail = null;

    @Override
    public synchronized void init(){
        section = SectionManager.get();

        AdminService adminService = new AdminServiceImpl();

        if(section.isLogged()){
            admin = adminService.get(section.getUserLogged());
            adminDetail = new AdminDetail(section.getUserLogged(), admin.getUserName(), "subtitle");
        } else {
            adminDetail = new AdminDetail();
        }

        float total = 43; // the difference represents the views not loaded
        increment = 100f / total;

        load("book", "BookView");
        load("book", "BookHandleView");
        load("book", "BookLendView");

        load("user","UserView");
        load("user","UserHandleView");
        load("user","UserSelectView");
        load("user","UserChargeView");

        load("lend","LendView");
        load("lend","LendHandleView");

        load("dashboard", "dashboard");

        load("charts", "piechart");

        load("main",     "main");

        load("profile", "profile");

        load("Login", "Login");
        load("Login", "account");

        // delay
        try {
            wait(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop(){

    }
    @Override
    public  void start(Stage primary) {

        configServices();
        initialScene();

        stylesheets = decorator.getScene().getStylesheets();

        stylesheets.addAll(
                getClass().getResource("/com/demi/theme/css/fonts.css").toExternalForm(),
                getClass().getResource("/com/demi/theme/css/material-color.css").toExternalForm(),
                getClass().getResource("/com/demi/theme/css/skeleton.css").toExternalForm(),
                getClass().getResource("/com/demi/theme/css/light.css").toExternalForm(),
                getClass().getResource("/com/demi/theme/css/bootstrap.css").toExternalForm(),
                getClass().getResource("/com/demi/theme/css/shape.css").toExternalForm(),
                getClass().getResource("/com/demi/theme/css/typographic.css").toExternalForm(),
                getClass().getResource("/com/demi/theme/css/helpers.css").toExternalForm(),
                getClass().getResource("/com/demi/theme/css/master.css").toExternalForm()
        );

        decorator.setMaximized(true);
        decorator.getStage().getIcons().add(new Image("/com/demi/module/media/logo2.png"));
        decorator.show();

//        ScenicView.show(decorator.getScene());
    }

    private void initialScene(){

        decorator.setTitle("Í¼Êé¹ÜÀí");
//        decorator.setIcon(null);
        decorator.addButton(ButtonType.FULL_EFFECT);
        decorator.initTheme(GNDecorator.Theme.DEFAULT);
//        decorator.fullBody();

        String log = logged();

        //decorator.setContent(ViewManager.getInstance().get("Login"));
        if (log.equals("account") || log.equals("Login")) {
            decorator.setContent(ViewManager.getInstance().get(log));
        }

        decorator.getStage().setOnCloseRequest(event -> {
            App.getAdminDetail().getPopOver().hide();
            if(Main.popConfig.isShowing()) Main.popConfig.hide();
            if(Main.popup.isShowing()) Main.popup.hide();
            Platform.exit();
        });
    }


    public static void main(String[] args) {
        LauncherImpl.launchApplication(App.class, Loader.class, args);
    }

    private void load(String module, String name){
        try {
            ViewManager.getInstance().put(
                    name,
                    FXMLLoader.load(getClass().getResource("/com/demi/module/" + module + "/" + name + ".fxml"))
            );
            preloaderNotify();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void preloaderNotify() {
        progress += increment;
        LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(progress));
    }

    private String logged(){

        return "Login";
    }

    public static GNDecorator getDecorator(){
        return decorator;
    }

    private void configServices(){
        hostServices = getHostServices();
    }



    public static AdminDetail getAdminDetail() {
        return adminDetail;
    }
}
