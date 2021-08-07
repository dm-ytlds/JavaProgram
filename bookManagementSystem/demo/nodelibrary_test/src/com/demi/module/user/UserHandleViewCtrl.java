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

    //修改的user对象
    private User user;
    // 实例化UserServiceImpl对象
    private UserService userService = new UserServiceImpl();

    /*
        添加或修改数据
     */
    @FXML
    private void addOrEditUser() {
        try {
            String id = userIdField.getText();
            if ("".equals(id) || null == id) {
                //添加操作
                User user = new User();
                populate(user);
                //设置状态为正常
                user.setStatus(Constant.USER_OK);
                // 调用service将数据持久化到文件中
                userService.add(user);
                users.add(user);

            }else {
                //修改操作
                populate(this.user);
                // 修改代码调用
                userService.update(user);
            }
            //刷新
            userTableView.refresh();
            stage.close();
            Alerts.success("成功", "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            Alerts.error("失败","操作失败");
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
            //填充修改界面的值
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
