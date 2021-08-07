package com.demi.module.user;

import com.demi.bean.Constant;
import com.demi.bean.User;
import com.demi.global.util.Alerts;
import com.demi.service.UserService;
import com.demi.service.impl.UserServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;

public class UserChargeViewCtrl {

    @FXML
    private TextField moneyField;

    private Stage stage;

    private User user;

    private TableView<User> userTableView;

    private UserService userService = new UserServiceImpl();
    /*
        ��ֵ
     */
    @FXML
    private void charge() {
        try {

            //���γ�ֵ�Ľ��
            BigDecimal money = new BigDecimal(moneyField.getText());
            user = userService.charge(user, money);
            userTableView.refresh();
            stage.close();
            Alerts.success("�ɹ�", "�����ɹ�");
        } catch (Exception e) {
            e.printStackTrace();
            Alerts.error("ʧ��","����ʧ��");
        }

    }

    @FXML
    private void closeView() {
        stage.close();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public TableView<User> getUserTableView() {
        return userTableView;
    }

    public void setUserTableView(TableView<User> userTableView) {
        this.userTableView = userTableView;
    }
}
