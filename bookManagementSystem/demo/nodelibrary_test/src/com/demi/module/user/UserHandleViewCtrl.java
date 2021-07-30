package com.demi.module.user;

import com.demi.bean.Constant;
import com.demi.bean.User;
import com.demi.global.util.Alerts;
import com.demi.service.UserService;
import com.demi.service.impl.UserServiceImpl;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;


public class UserHandleViewCtrl {

    @FXML
    private TextField userIdField;

    @FXML
    private TextField userNameField;

    @FXML
    private TextField moneyField;

    private Stage stage;

    private TableView<User> userTableView;

    private ObservableList<User> users;

    //�޸ĵ�user����
    private User user;
    // ʵ����UserServiceImpl����
    private UserService userService = new UserServiceImpl();

    /*
        ���ӻ��޸�����
     */
    @FXML
    private void addOrEditUser() {
        try {
            String id = userIdField.getText();
            if ("".equals(id) || null == id) {
                //���Ӳ���
                User user = new User();
                populate(user);
                //����״̬Ϊ����
                user.setStatus(Constant.USER_OK);
                // ����service�����ݳ־û����ļ���
                userService.add(user);
                users.add(user);

            }else {
                //�޸Ĳ���
                populate(this.user);
                // �޸Ĵ������
                userService.update(user);
                //ˢ��
                userTableView.refresh();
            }

            stage.close();
            Alerts.success("�ɹ�", "�����ɹ�");
        } catch (Exception e) {
            e.printStackTrace();
            Alerts.error("ʧ��","����ʧ��");
        }

    }

    private void populate(User user) {
        user.setMoney(new BigDecimal(moneyField.getText()));
        user.setName(userNameField.getText());
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

    public ObservableList<User> getUsers() {
        return users;
    }

    public void setUsers(ObservableList<User> users) {
        this.users = users;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            //����޸Ľ����ֵ
            userNameField.setText(user.getName());
            userIdField.setText(String.valueOf(user.getId()));
            moneyField.setText(user.getMoney().toString());
        }

    }

    public TableView<User> getUserTableView() {
        return userTableView;
    }

    public void setUserTableView(TableView<User> userTableView) {
        this.userTableView = userTableView;
    }
}